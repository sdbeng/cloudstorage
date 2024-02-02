package com.udacity.jwdnd.course1.cloudstorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class CloudStorageApplication {
//	private static final Logger logger = LoggerFactory.getLogger(CloudStorageApplication.class);

	public static void main(String[] args) {
//		logger.info("Starting up from main");
//		var config = ApplicationContextUtil.getApplicationContext();
//		var noteService = config.getBean(NoteService.class);
//		logger.info("Finished initializing noteService");

		SpringApplication.run(CloudStorageApplication.class, args);
	}

}
