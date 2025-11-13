package com.artesania.santander.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.artesania.santander.entity.Usuario;
import com.artesania.santander.interfaces.IUsuarioDAO;
import com.artesania.santander.service.IUsuarioService;

@Service
public class UsuarioServiceImp implements IUsuarioService {
	
	//Clase donde se implementa la funcionalidad de los metodos de la interface IUsuarioService
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private IUsuarioDAO usuarioDao;

	//Metodo para encontrar un usuario por el username
	@Override
	public Usuario findByUsername(String username) {
		return usuarioDao.findByUsername(username);
	}
	
	//Metodo para guardar las credenciales del usuario encriptando la contraseña
	@Override
	@Transactional
	public void save(Usuario usuario) {
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		usuarioDao.save(usuario);
	}
	
	//Metodo para encontrar todos los usuarios
	@Override
	@Transactional(readOnly=true)
	public List<Usuario> findAll() {
		return (List<Usuario>) usuarioDao.findAll();
	}
	
	//Meotodo para encontrar un solo usuario
	@Override
	@Transactional(readOnly=true)
	public Usuario findOne(Long id) {
		return usuarioDao.findById(id).orElse(null);
	}

	//Meotodo para eliminar usuario
	@Override
	@Transactional
	public void delete(Long id) {
		usuarioDao.deleteById(id);
	}

	//Metodo para crear el renderizado del paginator
	@Override
	@Transactional(readOnly=true)
	public Page<Usuario> findAll(Pageable pageable) {
		return usuarioDao.findAll(pageable);
	}
	
	//Metodo que busca por una variable q entrada por el usuario
	@Transactional
	public List<Usuario> findByBusqueda(String q) throws Exception {
		
		try {
			List<Usuario> entities = this.usuarioDao.findByBusqueda(q);
			return entities;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

}
