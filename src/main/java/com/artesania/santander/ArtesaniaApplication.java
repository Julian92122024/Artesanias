package com.artesania.santander;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.artesania.santander.service.IUploadFileService;

@SpringBootApplication
public class ArtesaniaApplication  {
	
	@Autowired
	IUploadFileService uploadFileService;

	public static void main(String[] args) {
		SpringApplication.run(ArtesaniaApplication.class, args);
	}



}
