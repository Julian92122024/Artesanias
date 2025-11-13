package com.artesania.santander.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	
	//Clase controlador para la vista index
	
	@GetMapping({"/","/index"})
	public String index(Model model) {
		model.addAttribute("titulo", "Inicio");
		
		return("index");
	}

}
