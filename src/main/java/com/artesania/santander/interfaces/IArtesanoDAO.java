package com.artesania.santander.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.artesania.santander.entity.Artesano;

public interface IArtesanoDAO extends PagingAndSortingRepository<Artesano, Long> {
	
	@Query(value = "SELECT * FROM artesano WHERE artesano.id LIKE %:q% or artesano.nombre LIKE %:q% or artesano.apellido LIKE %:q% or artesano.telefono LIKE %:q% or artesano.email LIKE %:q%", nativeQuery = true)
	List<Artesano> findByBusqueda(@Param("q") String q);
	
	@Query(value = "SELECT * FROM artesano WHERE artesano.nombre LIKE %:q% or artesano.apellido LIKE %:q% or artesano.telefono LIKE %:q% or artesano.email LIKE %:q%", nativeQuery = true)
	List<Artesano> findByBusquedaUsuario(@Param("q") String q);

}
