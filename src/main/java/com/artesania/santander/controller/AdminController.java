package com.artesania.santander.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.artesania.santander.entity.Usuario;
import com.artesania.santander.service.IUsuarioService;

@Controller
public class AdminController {
	
	//Clase controlador para la vista admin
	 
	
	@Autowired
	private IUsuarioService usuarioService;
	
	//Metodo para retornar la vista admin junto con el username del usuario que ingresa
	@GetMapping("/admin")
	public String admin(Model model, Authentication auth, HttpSession session) {
		model.addAttribute("titulo", "Admin");
		
		String username = auth.getName();
		
		if(session.getAttribute("usuario") == null) {
			Usuario usuario = usuarioService.findByUsername(username);
			usuario.setPassword(null);
			session.setAttribute("usuario", usuario);
		}
		
		return "admin";
	}

}
