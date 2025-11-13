package com.artesania.santander.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.artesania.santander.entity.Artesano;

public interface IArtesanoService {
	
	//Metodos para gestionar los datos de los artesanos
	
	//Metodo para encontrar todos los datos de todos los artesanos
	public List<Artesano> findAll();
	
	//Metodo para renderizar el paginator
	public Page<Artesano> findAll(Pageable pageable);
	
	//Metodo para guardar los datos de un nuevo artesano
	public void save(Artesano artesano);
	
	//Metodo para encontrar un solo artesano
	public Artesano findOne(Long id);
	
	//Metodo para eliminar un artesano
	public void delete(Long id);

	//Metodo para buscar
	public List<Artesano> findByBusqueda(String q) throws Exception;
	
	//Metodo para buscar
	public List<Artesano> findByBusquedaUsuario(String q) throws Exception;
}
