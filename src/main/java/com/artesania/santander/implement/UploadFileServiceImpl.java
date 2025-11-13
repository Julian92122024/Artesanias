package com.artesania.santander.implement;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.artesania.santander.service.IUploadFileService;

@Service
public class UploadFileServiceImpl implements IUploadFileService {
	
	//Clase donde se implementa la funcionalidad de los metodos de la interface IUploadService

	//Variable para visuzalizar mensajes en consola
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final static String UPLOADS_FOLDER = "uploads";

	//Metodo para cargar la imagen
	@Override
	public Resource load(String filename) throws MalformedURLException {

		Path pathFoto = getPath(filename); //Asignamos la ruta absoluta a la variable pathFoto
		log.info("pathFoto: " + pathFoto);
		Resource recurso = null;

		recurso = new UrlResource(pathFoto.toUri()); //Creamos el recurso con la ruta de la foto
		
		//Capturamos excepciones
		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("ERROR: NO Se Puede Cargar la Imagen: " + pathFoto.toString());
		}

		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {

		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename(); //Creamos un nombre unico para la foto

		Path rootPath = getPath(uniqueFilename); //Asignamos la ruta absoluta a la variable pathFoto

		log.info("rootPath: " + rootPath); //Mensaje en consola

		Files.copy(file.getInputStream(), rootPath);

		return uniqueFilename;
	}

	//Metodo para eliminar la foto
	@Override
	public boolean delete(String filename) {

		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();

		if (archivo.exists() && archivo.canRead()) {
			if (archivo.delete()) {
				return true;
			}
		}

		return false;
	}

	//Metodo para retornar el path completo
	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	//Metodo para eliminar todas las imagenes
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
		
	}

	//Metodo para crear la carpeta uploads donde se guardarán las imagenes
	@Override
	public void init() throws IOException {
		// TODO Auto-generated method stub
		Files.createDirectory(Paths.get(UPLOADS_FOLDER));
	}

}
