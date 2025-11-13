package com.artesania.santander.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.artesania.santander.entity.Producto;
import com.artesania.santander.interfaces.IProductoDAO;
import com.artesania.santander.service.IProductoService;

@Service
public class ProductoServiceImpl implements IProductoService {
	
	//Clase donde se implementa la funcionalidad de los metodos de la interface IProductoService
	
	@Autowired
	private IProductoDAO productoDao;
	
	//Metodo para encontrar todos los productos
	@Override
	@Transactional(readOnly=true)
	public List<Producto> findAll() {
		return (List<Producto>) productoDao.findAll();
	}

	//Metodo para guardar los datos del producto
	@Override
	@Transactional
	public void save(Producto producto) {
		productoDao.save(producto);
	}

	//Metodo para encontrar un solo producto
	@Override
	@Transactional(readOnly=true)
	public Producto findOne(Long id) {
		return productoDao.findById(id).orElse(null);
	}

	//Metodo para eliminar un producto
	@Override
	@Transactional
	public void delete(Long id) {
		productoDao.deleteById(id);
	}

	//Metodo para crear el renderizado del paginator
	@Override
	@Transactional(readOnly=true)
	public Page<Producto> findAll(Pageable pageable) {
		return productoDao.findAll(pageable);
	}
	
	//Metodo que busca por una variable q entrada por el usuario
	@Transactional
	public List<Producto> findByBusqueda(String q) throws Exception {
		
		try {
			List<Producto> entities = this.productoDao.findByBusqueda(q);
			return entities;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
	@Transactional
	public List<Producto> findByBusquedaUsuario(String q) throws Exception {
		
		try {
			List<Producto> entities = this.productoDao.findByBusquedaUsuario(q);
			return entities;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

}
