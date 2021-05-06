package com.uniminuto.MonitorSistema.rest;

import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementaci&oacute;n por defecto de {@link PingController}
 * 
 * @author qleoz12
 *
 */
@RestController
@Slf4j
@RequestMapping("/ping")
public class PingController {

	@Autowired
	private MessageSource messageSource;

	@GetMapping
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok("Ping service OK: ");
	}

	@GetMapping("/imageio")
	public ResponseEntity<String> testImageio() {
		Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
		while (readers.hasNext()) {
			return ResponseEntity.ok("reader: " + readers.next());
		}

		return ResponseEntity.ok("FAILED");
	}

}