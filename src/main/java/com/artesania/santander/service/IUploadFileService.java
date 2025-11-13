package com.artesania.santander.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
	
	//Metodos para gestionar las fotos
	
	//Carga la imagen
	public Resource load(String filename) throws MalformedURLException;
	
	//Retorna el nombre unico de la foto
	public String copy(MultipartFile file) throws IOException;
	
	//Retorna un bool para saber si se eliminó o no
	public boolean delete(String filename);
	
	//Metodo para eliminar todas las imagenes
	public void deleteAll();
	
	//Metodo para crear la carpeta uploads donde se guardarán las imagenes
	public void init() throws IOException;

}
