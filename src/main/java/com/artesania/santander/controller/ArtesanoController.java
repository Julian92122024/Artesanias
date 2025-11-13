package com.artesania.santander.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.artesania.santander.entity.Artesano;
import com.artesania.santander.paginator.PageRender;
import com.artesania.santander.service.IArtesanoService;
import com.artesania.santander.service.IUploadFileService;

@Controller
@SessionAttributes("artesano")
public class ArtesanoController {

	// Clase controlador para la vista artesano

	@Autowired
	private IArtesanoService artesanoService;

	@Autowired
	private IUploadFileService uploadFileService;

	// Metodo para poder ver la foto del artesano
	@GetMapping(value = "/uploadsArtesano/{filename:.+}") // Enviará el nombre de la foro sin la extensión (.jpg)
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		/*
		 * Recibe la foto como un path variable para convertirla en un recurso input
		 * String y se enviará como respuesta al http como un un response entity
		 */

		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename); // Llamamos el metodo load para obtener el recurso de la imagen
														// y pasarla a la respuesta
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Retornamos el nombre de la foto concatenado al encabezado del http, junto con
		// el recurso (foto)
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);

	}

	// Metodo para retornar la vista ver de artesano
	@GetMapping(value = "/admin/verArtesano/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		// Se busca al artesano por el id
		Artesano artesano = artesanoService.findOne(id);

		if (artesano == null) {
			flash.addFlashAttribute("error", "El Artesano NO Existe en la Base de Datos");
			return "redirect:/listarArtesano";
		}

		model.put("artesano", artesano);
		model.put("titulo", "Detalle Artesano: " + artesano.getNombre());

		return "artesano/ver";
	}

	// Metodo para retornar la vista listarAdmin de artesano
	@RequestMapping(value = "/admin/listarArtesano", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		// Cantidad de datos que se renderizarán
		Pageable pageRequest = PageRequest.of(page, 6);

		Page<Artesano> artesanos = artesanoService.findAll(pageRequest);

		// Se crear el renderizado de la pagina con el objeto PageRender de tipo
		// artesano en la URL /admin/listarArtesano
		PageRender<Artesano> pageRender = new PageRender<>("/admin/listarArtesano", artesanos);

		model.addAttribute("accion", "listarArtesano");
		model.addAttribute("titulo", "Listado de Artesanos");
		model.addAttribute("artesanos", artesanos);
		model.addAttribute("page", pageRender);

		return "artesano/listarAdmin";
	}

	// Metodo para retornar la vista listar de artesano
	@RequestMapping(value = "/quienesSomos", method = RequestMethod.GET)
	public String listarVistaUsuario(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		// Cantidad de datos que se renderizarán
		Pageable pageRequest = PageRequest.of(page, 4);

		Page<Artesano> artesanos = artesanoService.findAll(pageRequest);

		// Se crear el renderizado de la pagina con el objeto PageRender de tipo
		// artesano en la URL /admin/listarArtesano
		PageRender<Artesano> pageRender = new PageRender<>("/quienesSomos", artesanos);

		model.addAttribute("titulo", "Listado de Artesanos");
		model.addAttribute("artesanos", artesanos);
		model.addAttribute("page", pageRender);

		return "artesano/listar";
	}

	// Metodo para retornar la vista crear de artesano
	@RequestMapping(value = "/admin/crearArtesano")
	public String crear(Map<String, Object> model) {

		Artesano artesano = new Artesano();

		model.put("artesano", artesano);
		model.put("titulo", "Crear Artesano");

		return "artesano/crear";
	}

	// Metodo para editar un artesano creado con anterioridad
	@RequestMapping(value = "/admin/crearArtesano/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Artesano artesano = null;

		if (id > 0) {
			artesano = artesanoService.findOne(id);
			if (artesano == null) {
				flash.addFlashAttribute("error", "El ID del Artesano NO Existe en la BBDD!");
				return "redirect:/admin/listarArtesano";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del Artesano NO Pueder Ser 0!");
			return "redirect:/admin/listarArtesano";
		}

		model.put("artesano", artesano);
		model.put("titulo", "Editar Artesano");

		return "artesano/crear";
	}

	// Metodo para guardar los nuevos artesanos en la BD
	@RequestMapping(value = "/admin/crearArtesano", method = RequestMethod.POST)
	public String guardar(@Valid Artesano artesano, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		// Captura de errores y redirección a la vista crear de artesano en caso de que
		// sucedan
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Artesano");
			return "/artesano/crear";
		}

		if (!foto.isEmpty()) {

			if (artesano.getId() != null && artesano.getId() > 0 && artesano.getFoto() != null
					&& artesano.getFoto().length() > 0) {

				uploadFileService.delete(artesano.getFoto());

			}

			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Has Subido Correctamente '" + uniqueFilename + "'");

			artesano.setFoto(uniqueFilename);

		}
		String mensajeFlash = (artesano.getId() != null) ? "Artesano Editado con Exito!" : "Artesano Creado con Exito!";

		artesanoService.save(artesano);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:listarArtesano";
	}

	// Metodo para eliminar un artesano y su foto
	@RequestMapping(value = "/admin/eliminarArtesano/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Artesano artesano = artesanoService.findOne(id);

			artesanoService.delete(id);
			flash.addFlashAttribute("success", "Artesano Eliminado con Exito!");

			if (artesano.getFoto() != null) {
				if (uploadFileService.delete(artesano.getFoto())) {
					flash.addFlashAttribute("info", "Foto " + artesano.getFoto() + " Eliminada Con Exito!");
				}
			}
		}
		
		return "redirect:/admin/listarArtesano";
	}

	@RequestMapping(value = "/admin/listarArtesano/busqueda", method = RequestMethod.GET)
	public String busquedaArtesano(Model model, @RequestParam(value = "query", required = false) String q) {

		try {
			List<Artesano> artesanos = this.artesanoService.findByBusqueda(q);
			model.addAttribute("artesanos", artesanos);
			model.addAttribute("accion", "listarArtesano");
			model.addAttribute("titulo", "Busqueda: " + q);
			model.addAttribute("resultados", "Resultados para " + q + ":");
			model.addAttribute("vacio", "No hay resultados para " + q);
			model.addAttribute("vacioInfo",
					"Revisa la ortografía o busca por términos separados (busca nombre o apellido, etc)");
			return "artesano/busquedaAdmin";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

	@RequestMapping(value = "/quienesSomos/busqueda", method = RequestMethod.GET)
	public String busquedaArtesanoUsuario(Model model, @RequestParam(value = "query", required = false) String q) {

		try {
			List<Artesano> artesanos = this.artesanoService.findByBusquedaUsuario(q);
			model.addAttribute("artesanos", artesanos);
			model.addAttribute("titulo", "Busqueda: " + q);
			model.addAttribute("resultados", "Resultados para " + q + ":");
			model.addAttribute("vacio", "No hay resultados para " + q);
			model.addAttribute("vacioInfo",
					"Revisa la ortografía o busca por términos separados (busca nombre o apellido, etc)");
			return "artesano/busqueda";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

}
