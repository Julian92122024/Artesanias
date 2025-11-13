package com.artesania.santander.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.artesania.santander.service.SendMailService;



@Controller
public class SendMailController 
{
	@Autowired
	private SendMailService sendMailservice;
	
	@GetMapping("/contactenos")
	public String contactenos(Model model){
		model.addAttribute("titulo", "Contactenos");
		return "contactenos";
	}
	
	@PostMapping("/sendMail")
	public String sendMail(@RequestParam("nombre")String nombre, @RequestParam("apellido")String apellido, @RequestParam("telefono")String telefono, @RequestParam("correo")String correoReceptor, @RequestParam("mensaje")String mensaje)
	{	
		String correoRemitente = "juliocesarartesanias2022@gmail.com";
		String cuerpoCorreoDuda = "Datos del Cliente:\n\n" + "Nombre: " + nombre + " " + apellido + "\nTelefono: " + telefono + "\nCorreo electronico: " + correoReceptor + "\n\nSolicitud:\n\n" + mensaje;
		String cuerpoCorreo = "Gracias por contactarte con nosotros, antenderemos tu solicitud para dar respuesta pronto \n\n - Julio Cesar Artesanías.";
		try 
		{
			sendMailservice.sendMail(correoRemitente, correoReceptor, cuerpoCorreo);
			sendMailservice.sendMail(correoRemitente, correoRemitente, cuerpoCorreoDuda);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "redirect:/index";
	}
}
