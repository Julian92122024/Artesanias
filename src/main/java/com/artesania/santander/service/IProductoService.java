package com.artesania.santander.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.artesania.santander.entity.Producto;

public interface IProductoService {
	
	//Metodos para gestionar los datos de los productos
	
	//Metodo para encontrar todos los datos de todos los productos
	public List<Producto> findAll();
	
	//Metodo para renderizar el paginator
	public Page<Producto> findAll(Pageable pageable);
	
	//Metodo para guardar los datos de un nuevo producto
	public void save(Producto producto);
	
	//Metodo para encontrar un solo producto
	public Producto findOne(Long id);
	
	//Metodo para eliminar un producto
	public void delete(Long id);
	
	//Metodo para buscar
	public List<Producto> findByBusqueda(String q) throws Exception;
	
	//Metodo para buscar
		public List<Producto> findByBusquedaUsuario(String q) throws Exception;

}
