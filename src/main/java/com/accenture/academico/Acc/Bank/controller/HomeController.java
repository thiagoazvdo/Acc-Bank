package com.accenture.academico.Acc.Bank.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@Value("${server.url}")
    private String serverUrl;
	
	@GetMapping("/")
    public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("serverUrl", serverUrl);
        return modelAndView;
    }
}
