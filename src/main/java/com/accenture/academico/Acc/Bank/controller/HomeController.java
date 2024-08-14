package com.accenture.academico.Acc.Bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
    public String documentacao(Model model) {
        return "home";
    }
}
