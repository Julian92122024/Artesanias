package com.artesania.santander.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.artesania.santander.entity.Usuario;

public interface IUsuarioService {
	
	//Metodos para gestionar los datos de los usuario
	
	//Metodo para encontrar todos los datos de todos los usuarios
	public List<Usuario> findAll();
	
	//Metodo para renderizar el paginator
	public Page<Usuario> findAll(Pageable pageable);
	
	//Metodo para encontrar un solo usuario
	public Usuario findOne(Long id);
	
	//Metodo para encontrar a un usuario por su username
	public Usuario findByUsername(String username);
	
	//Metodo para guardar los datos de un nuevo usuario
	public void save(Usuario usuario);

	//Metodo para eliminar un usuario
	public void delete(Long id);
	
	//Metodo para buscar
	public List<Usuario> findByBusqueda(String q) throws Exception;
}
