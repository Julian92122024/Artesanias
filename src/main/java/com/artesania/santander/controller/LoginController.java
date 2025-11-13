package com.artesania.santander.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.artesania.santander.entity.Usuario;
import com.artesania.santander.paginator.PageRender;
import com.artesania.santander.service.IUsuarioService;

@Controller
public class LoginController {
	
	//Clase controlador para la vista login y usuario
	
	@Autowired
	private IUsuarioService usuarioService;
	
	//Metodo para retornar la vista login
	@GetMapping("/login")
	public String formLogin(Model model) {
		model.addAttribute("titulo", "Log In");
		model.addAttribute("usuario", new Usuario());
		
		return "login";
	}
	
	//Metodo para retornar la vista listarAdmin de usuario
	@RequestMapping(value = "/admin/listarUsuario", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		//Cantidad de datos que se renderizarán
		Pageable pageRequest = PageRequest.of(page, 6);

		Page<Usuario> usuarios = usuarioService.findAll(pageRequest);
		
		//Se crear el renderizado de la pagina con el objeto PageRender de tipo usuarios en la URL /admin/listarUsuario
		PageRender<Usuario> pageRender = new PageRender<>("/admin/listarUsuario", usuarios);

		model.addAttribute("titulo", "Listado de Usuarios");
		model.addAttribute("accion","listarUsuario");
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("page", pageRender);

		return "usuario/listarAdmin";
	}
	
	//Metodo para retornar la vista crear de usuario
	@RequestMapping(value = "/admin/crearUsuario")
	public String crear(Map<String, Object> model) {

		Usuario usuario = new Usuario();

		model.put("usuario", usuario);
		model.put("titulo", "Crear Usuario");

		return "usuario/crear";
	}
	
	//Metodo para guardar los nuevos usuarios en la BD
	@RequestMapping(value = "/admin/crearUsuario", method = RequestMethod.POST)
	public String guardar(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {

		//Captura de errores y redirección a la vista crear de usuario en caso de que sucedan
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Usuario");
			return "/usuario/crear";
		}

		String mensajeFlash = (usuario.getId() != null) ? "Usuario Editado con Exito!" : "Usuario Creado con Exito!";

		usuarioService.save(usuario);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:listarUsuario";
	}
	
	//Metodo para editar un uusario creado con anterioridad
	@RequestMapping(value = "/admin/crearUsuario/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Usuario usuario = null;

		if (id > 0) {
			usuario = usuarioService.findOne(id);
			if (usuario == null) {
				flash.addFlashAttribute("error", "El ID del Usuario NO Existe en la BBDD!");
				return "redirect:/admin/listarUsuario";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del Usuario NO Pueder Ser 0!");
			return "redirect:/admin/listarUsuario";
		}

		model.put("usuario", usuario);
		model.put("titulo", "Editar Usuario");

		return "usuario/crear";
	}
	
	//Metodo para eliminar un usuario
	@RequestMapping(value = "/admin/eliminarUsuario/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			usuarioService.delete(id);
			flash.addFlashAttribute("success", "Usuario Eliminado con Exito!");
		}

		return "redirect:/admin/listarUsuario";
	}
	
	//Metodo para buscar productos por una entrada del usuario
	@RequestMapping(value = "/admin/listarUsuario/busqueda", method = RequestMethod.GET)
	public String busquedaUsuario(Model model, @RequestParam(value = "query", required = false) String q) {
		
		try {
			List<Usuario> usuarios = this.usuarioService.findByBusqueda(q);
			model.addAttribute("usuarios", usuarios);
			model.addAttribute("accion","listarUsuario");
			model.addAttribute("titulo", "Busqueda: " + q);
			model.addAttribute("resultados", "Resultados para " + q + ":");
			model.addAttribute("vacio", "No hay resultados para " + q);
			model.addAttribute("vacioInfo", "Revisa la ortografía o busca por términos términos generales");
			return "usuario/busquedaAdmin";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

}
