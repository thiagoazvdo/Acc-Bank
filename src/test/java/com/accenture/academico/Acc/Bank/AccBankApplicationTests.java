package com.accenture.academico.Acc.Bank;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AccBankApplicationTests {

	@Test
	void main() {
		AccBankApplication.main(new String[] {});
	}

	@Test
	void contextLoads(ApplicationContext context) {
		assertThat(context).isNotNull();
		assertThat(context.getBean(ModelMapper.class)).isNotNull();
	}

}
