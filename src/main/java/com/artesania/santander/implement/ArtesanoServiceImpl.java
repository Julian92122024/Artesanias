package com.artesania.santander.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.artesania.santander.entity.Artesano;
import com.artesania.santander.interfaces.IArtesanoDAO;
import com.artesania.santander.service.IArtesanoService;

@Service
public class ArtesanoServiceImpl implements IArtesanoService {
	
	//Clase donde se implementa la funcionalidad de los metodos de la interface IArtesanoService
	
	@Autowired
	private IArtesanoDAO artesanoDao;
	
	//Metodo para encontrar todos los artesanos
	@Override
	@Transactional(readOnly=true)
	public List<Artesano> findAll() {
		return (List<Artesano>) artesanoDao.findAll();
	}

	//Metodo para guardar los datos del artesano
	@Override
	@Transactional
	public void save(Artesano artesano) {
		artesanoDao.save(artesano);
	}

	//Metodo para encontrar un solo artesano
	@Override
	@Transactional(readOnly=true)
	public Artesano findOne(Long id) {
		return artesanoDao.findById(id).orElse(null);
	}

	//Metodo para eliminar un artesano
	@Override
	@Transactional
	public void delete(Long id) {
		artesanoDao.deleteById(id);
	}

	//Metodo para crear el renderizado del paginator
	@Override
	@Transactional(readOnly=true)
	public Page<Artesano> findAll(Pageable pageable) {
		return artesanoDao.findAll(pageable);
	}
	
	//Metodo que busca por una variable q entrada por el usuario
	@Transactional
	public List<Artesano> findByBusqueda(String q) throws Exception {
		
		try {
			List<Artesano> entities = this.artesanoDao.findByBusqueda(q);
			return entities;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
	//Metodo que busca por una variable q entrada por el usuario
		@Transactional
		public List<Artesano> findByBusquedaUsuario(String q) throws Exception {
			
			try {
				List<Artesano> entities = this.artesanoDao.findByBusquedaUsuario(q);
				return entities;
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			
		}

}
