package com.accenture.academico.Acc.Bank.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.accenture.academico.Acc.Bank.exception.ConexaoBancoDadosException;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.asm.tree.TryCatchBlockNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.academico.Acc.Bank.dto.ContaCorrenteRequestDTO;
import com.accenture.academico.Acc.Bank.dto.ContaCorrenteResponseDTO;
import com.accenture.academico.Acc.Bank.dto.SaqueDepositoRequestDTO;
import com.accenture.academico.Acc.Bank.dto.TransferenciaRequestDTO;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaCorrenteComSaldoException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaCorrenteJaCadastradoException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ContaCorrenteNaoEncontradaException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.SaldoInsuficienteException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.TransferenciaEntreContasIguaisException;
import com.accenture.academico.Acc.Bank.exception.contacorrente.ValorInvalidoException;
import com.accenture.academico.Acc.Bank.model.Agencia;
import com.accenture.academico.Acc.Bank.model.Cliente;
import com.accenture.academico.Acc.Bank.model.ContaCorrente;
import com.accenture.academico.Acc.Bank.model.TipoTransacao;
import com.accenture.academico.Acc.Bank.model.Transacao;
import com.accenture.academico.Acc.Bank.repository.ContaCorrenteRepository;
import com.accenture.academico.Acc.Bank.repository.TransacaoRepository;

import javax.xml.crypto.Data;

@Service
public class ContaCorrenteService {

	private static final String MSG_CONEXAO_BD_PERDIDA = "Falha na conexão com o banco de dados. Tente novamente mais tarde.";

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ContaCorrenteRepository contaCorrenteRepository;
	
	@Autowired
	private AgenciaService agenciaService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	@Transactional
	public ContaCorrenteResponseDTO criarContaCorrente(ContaCorrenteRequestDTO contaDTO) {
		verificaSeClientePossuiConta(contaDTO.getIdCliente());

		Agencia agencia = agenciaService.buscarAgencia(contaDTO.getIdAgencia());
		Cliente cliente = clienteService.buscarCliente(contaDTO.getIdCliente());
		ContaCorrente conta = new ContaCorrente(agencia, cliente);

		try {
			conta = contaCorrenteRepository.save(conta);
			String numeroConta = Long.toString(conta.getId() + 10000);
			conta.setNumero(numeroConta);
			return converterParaContaCorrenteResponseDTO(conta);
		} catch (DataAccessResourceFailureException e) {
			throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
		}
	}

	public ContaCorrenteResponseDTO buscarContaCorrenteResponseDTO(Long id) {
		ContaCorrente conta = buscarContaCorrente(id);
		return converterParaContaCorrenteResponseDTO(conta);
	}
	
	public ContaCorrente buscarContaCorrente(Long id) {
		try {
			return contaCorrenteRepository.findById(id)
					.orElseThrow(() -> new ContaCorrenteNaoEncontradaException(id));
		} catch (DataAccessResourceFailureException e) {
			throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
		}
	}
	
	public ContaCorrente buscarContaCorrentePorNumero(String numeroConta) {
		try{
			return contaCorrenteRepository.findByNumero(numeroConta)
					.orElseThrow(() -> new ContaCorrenteNaoEncontradaException(numeroConta));
		} catch (DataAccessResourceFailureException e){
			throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
		}
	}
	
	public void removerContaCorrente(Long id) {
		try {
			ContaCorrente conta = buscarContaCorrente(id);
			if (conta.getSaldo().compareTo(BigDecimal.ZERO) > 0) throw new ContaCorrenteComSaldoException();
			contaCorrenteRepository.delete(conta);
		} catch (DataAccessResourceFailureException e) {
			throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
		}
	}
	
	@Transactional
    public void sacar(Long id, SaqueDepositoRequestDTO saqueDTO) {
		verificaSeValorEhMaiorQueZero(saqueDTO.getValor());

		ContaCorrente conta = buscarContaCorrente(id);
		verificaSeContaPossuiSaldoSuficiente(conta, saqueDTO.getValor());
		conta.sacar(saqueDTO.getValor());
		registrarTransacao(conta, null, TipoTransacao.SAQUE, saqueDTO.getValor(), saqueDTO.getDescricao());

		try {
			contaCorrenteRepository.save(conta);
		} catch (DataAccessResourceFailureException e) {
			throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
		}
    }

    @Transactional
    public void depositar(Long id, SaqueDepositoRequestDTO depositoDTO) {
    	verificaSeValorEhMaiorQueZero(depositoDTO.getValor());

    	ContaCorrente conta = buscarContaCorrente(id);
    	conta.depositar(depositoDTO.getValor());
    	registrarTransacao(conta, null, TipoTransacao.DEPOSITO, depositoDTO.getValor(), depositoDTO.getDescricao());

		try {
			contaCorrenteRepository.save(conta);
		} catch (DataAccessResourceFailureException e) {
			throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
		}
    }

    @Transactional
	public void transferir(Long idOrigem, TransferenciaRequestDTO transferenciaDTO) {
    	verificaSeValorEhMaiorQueZero(transferenciaDTO.getValor());

        ContaCorrente contaOrigem = buscarContaCorrente(idOrigem);
        ContaCorrente contaDestino = buscarContaCorrentePorNumero(transferenciaDTO.getNumeroContaDestino());
        if (contaOrigem.equals(contaDestino)) throw new TransferenciaEntreContasIguaisException();
        
        verificaSeContaPossuiSaldoSuficiente(contaOrigem, transferenciaDTO.getValor());
        contaOrigem.sacar(transferenciaDTO.getValor());
        contaDestino.depositar(transferenciaDTO.getValor());

		try{
			contaCorrenteRepository.save(contaOrigem);
			contaCorrenteRepository.save(contaDestino);
			registrarTransacao(contaOrigem, contaDestino, TipoTransacao.TRANSFERENCIA_ENVIADA, transferenciaDTO.getValor(), transferenciaDTO.getDescricao());
			registrarTransacao(contaDestino, contaOrigem, TipoTransacao.TRANSFERENCIA_RECEBIDA, transferenciaDTO.getValor(), transferenciaDTO.getDescricao());

		} catch (DataAccessResourceFailureException e) {
			throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
		}

	}
    
    private void registrarTransacao(ContaCorrente contaOrigem, ContaCorrente contaDestino, TipoTransacao tipo, BigDecimal valor, String descricao) {
        Transacao transacao = new Transacao();
        transacao.setContaCorrente(contaOrigem);
        transacao.setContaCorrenteRelacionada(contaDestino);
        transacao.setTipo(tipo);
        transacao.setValor(valor);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setDescricao(descricao);

		try{
			transacaoRepository.save(transacao);
		} catch (DataAccessResourceFailureException e) {
			throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
		}
    }
    
	private void verificaSeClientePossuiConta(Long idCliente) {
		try {
			if (contaCorrenteRepository.findByClienteId(idCliente).isPresent())
				throw new ContaCorrenteJaCadastradoException(idCliente);
		} catch (DataAccessResourceFailureException e) {
			throw new ConexaoBancoDadosException(MSG_CONEXAO_BD_PERDIDA);
		}
	}
	
	private void verificaSeContaPossuiSaldoSuficiente(ContaCorrente conta, BigDecimal valorSaque) {
		if (conta.getSaldo().compareTo(valorSaque) < 0) 
			throw new SaldoInsuficienteException();
	}
	
	private void verificaSeValorEhMaiorQueZero(BigDecimal valor) {
		if (valor.compareTo(BigDecimal.ZERO) <= 0)
    		throw new ValorInvalidoException();
	}
	
	private ContaCorrenteResponseDTO converterParaContaCorrenteResponseDTO(ContaCorrente conta) {
		return modelMapper.map(conta, ContaCorrenteResponseDTO.class);
	}
	
}
