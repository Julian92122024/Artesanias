package com.artesania.santander.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.artesania.santander.entity.Usuario;

@Repository
public interface IUsuarioDAO extends PagingAndSortingRepository<Usuario, Long> {
	
	public Usuario findByUsername(String username);

	@Query(value = "SELECT * FROM usuario WHERE usuario.username LIKE %:q% or usuario.id LIKE %:q%", nativeQuery = true)
	List<Usuario> findByBusqueda(@Param("q") String q);
}
