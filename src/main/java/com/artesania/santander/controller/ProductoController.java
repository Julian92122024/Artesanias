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

import com.artesania.santander.entity.Producto;
import com.artesania.santander.paginator.PageRender;
import com.artesania.santander.service.IProductoService;
import com.artesania.santander.service.IUploadFileService;

@Controller
@SessionAttributes("producto")
public class ProductoController {

	// Clase controlador para la vista artesano

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUploadFileService uploadFileService;

	// Metodo para poder ver la foto del producto
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);

	}

	// Metodo para retornar la vista ver de producto
	@GetMapping(value = "/admin/verProducto/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		// Se busca el producto por el id
		Producto producto = productoService.findOne(id);

		if (producto == null) {
			flash.addFlashAttribute("error", "El Producto NO Existe en la Base de Datos");
			return "redirect:/listarProducto";
		}

		model.put("producto", producto);
		model.put("titulo", "Detalle Producto: " + producto.getNombre());

		return "producto/ver";
	}

	// Metodo para retornar la vista listarAdmin de producto
	@RequestMapping(value = "/admin/listarProducto", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(value = "query", required = false) String q, Model model) {

		// Cantidad de datos que se renderizarán
		Pageable pageRequest = PageRequest.of(page, 6);

		Page<Producto> productos = productoService.findAll(pageRequest);

		// Se crear el renderizado de la pagina con el objeto PageRender de tipo
		// producto en la URL /admin/listarProducto
		PageRender<Producto> pageRender = new PageRender<>("/admin/listarProducto", productos);

		model.addAttribute("titulo", "Listado de Productos");
		model.addAttribute("accion", "listarProducto");
		model.addAttribute("productos", productos);
		model.addAttribute("page", pageRender);

		return "producto/listarAdmin";
	}

	// Metodo para retornar la vista listar de producto
	@RequestMapping(value = "/artesanias", method = RequestMethod.GET)
	public String listarVistaUsuario(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		// Cantidad de datos que se renderizarán
		Pageable pageRequest = PageRequest.of(page, 6);

		Page<Producto> productos = productoService.findAll(pageRequest);

		// Se crear el renderizado de la pagina con el objeto PageRender de tipo
		// producto en la URL /admin/listarProducto
		PageRender<Producto> pageRender = new PageRender<>("/artesanias", productos);

		model.addAttribute("titulo", "Listado de Productos");
		model.addAttribute("productos", productos);
		model.addAttribute("page", pageRender);

		return "producto/listar";
	}

	// Metodo para retornar la vista crear de producto
	@RequestMapping(value = "/admin/crearProducto")
	public String crear(Map<String, Object> model) {

		Producto producto = new Producto();

		model.put("producto", producto);
		model.put("titulo", "Crear Producto");

		return "producto/crear";
	}

	// Metodo para editar un artesano creado con anterioridad
	@RequestMapping(value = "/admin/crearProducto/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Producto producto = null;

		if (id > 0) {
			producto = productoService.findOne(id);
			if (producto == null) {
				flash.addFlashAttribute("error", "El ID del Producto NO Existe en la BBDD!");
				return "redirect:/admin/listarProducto";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del Producto NO Pueder Ser 0!");
			return "redirect:/admin/listarProducto";
		}

		model.put("producto", producto);
		model.put("titulo", "Editar Producto");

		return "producto/crear";
	}

	// Metodo para guardar los nuevos artesanos en la BD
	@RequestMapping(value = "/admin/crearProducto", method = RequestMethod.POST)
	public String guardar(@Valid Producto producto, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		// Captura de errores y redirección a la vista crear de producto en caso de que
		// sucedan
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Producto");
			return "/producto/crear";
		}

		if (!foto.isEmpty()) {

			if (producto.getId() != null && producto.getId() > 0 && producto.getFoto() != null
					&& producto.getFoto().length() > 0) {

				uploadFileService.delete(producto.getFoto());

			}

			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Has Subido Correctamente '" + uniqueFilename + "'");

			producto.setFoto(uniqueFilename);

		}
		String mensajeFlash = (producto.getId() != null) ? "Producto Editado con Exito!" : "Producto Creado con Exito!";

		productoService.save(producto);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:listarProducto";
	}

	// Metodo para eliminar un producto y su foto
	@RequestMapping(value = "/admin/eliminarProducto/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		
		if (id > 0) {
			Producto producto = productoService.findOne(id);

			productoService.delete(id);
			flash.addFlashAttribute("success", "Producto Eliminado con Exito!");

			if(producto.getFoto() != null) {
				if (uploadFileService.delete(producto.getFoto())) {
					flash.addFlashAttribute("info", "Foto " + producto.getFoto() + " Eliminada Con Exito!");
				}
			}
		}

		return "redirect:/admin/listarProducto";
	}

	// Metodo para buscar productos por una entrada del usuario
	@RequestMapping(value = "/admin/listarProducto/busqueda", method = RequestMethod.GET)
	public String busquedaProducto(Model model, @RequestParam(value = "query", required = false) String q) {

		try {
			List<Producto> productos = this.productoService.findByBusqueda(q);
			model.addAttribute("productos", productos);
			model.addAttribute("accion", "listarProducto");
			model.addAttribute("titulo", "Busqueda: " + q);
			model.addAttribute("resultados", "Resultados para " + q + ":");
			model.addAttribute("vacio", "No hay resultados para " + q);
			model.addAttribute("vacioInfo",
					"Revisa la ortografía o busca por términos términos separados (busca nombre o precio, etc)");
			return "producto/busquedaAdmin";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

	// Metodo para buscar productos por una entrada del usuario
	@RequestMapping(value = "/artesanias/busqueda", method = RequestMethod.GET)
	public String busquedaProductoUsuario(Model model, @RequestParam(value = "query", required = false) String q) {

		try {
			List<Producto> productos = this.productoService.findByBusquedaUsuario(q);
			model.addAttribute("productos", productos);
			model.addAttribute("accion", "listarProductoUsuario");
			model.addAttribute("titulo", "Busqueda: " + q);
			model.addAttribute("resultados", "Resultados para " + q + ":");
			model.addAttribute("vacio", "No hay resultados para " + q);
			model.addAttribute("vacioInfo",
					"Revisa la ortografía o busca por términos términos separados (busca nombre o precio, etc)");
			return "producto/busqueda";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

}
