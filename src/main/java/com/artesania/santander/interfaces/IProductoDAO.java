package com.artesania.santander.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.artesania.santander.entity.Producto;

@Repository
public interface IProductoDAO extends PagingAndSortingRepository<Producto, Long> {

	@Query(value = "SELECT * FROM producto WHERE producto.id LIKE %:q% or producto.nombre LIKE %:q% or producto.precio LIKE %:q% or producto.descripcion LIKE %:q%", nativeQuery = true)
	List<Producto> findByBusqueda(@Param("q") String q);
	
	@Query(value = "SELECT * FROM producto WHERE producto.nombre LIKE %:q% or producto.precio LIKE %:q% or producto.descripcion LIKE %:q%", nativeQuery = true)
	List<Producto> findByBusquedaUsuario(@Param("q") String q);
	
}
