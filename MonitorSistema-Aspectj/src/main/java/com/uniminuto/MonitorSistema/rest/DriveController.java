package com.uniminuto.MonitorSistema.rest;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uniminuto.MonitorSistema.DTO.DriveDTO;
import com.uniminuto.MonitorSistema.services.DriveService;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

/**
 * Implementaci&oacute;n por defecto de {@link PingController}
 * 
 * @author qleoz12
 *
 */
@Slf4j
@RestController
@RequestMapping("/drive")
public class DriveController {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private DriveService driveService;

	@GetMapping
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok("Ping service OK: ");
	}
	
	
	@GetMapping("/drives")
	public ResponseEntity<?> drives() {
		return ResponseEntity.ok().body(driveService.drives());
	}
	
	/*
	 * for windows OS C:
	 */
	@GetMapping("/exist/{drive}")
	public ResponseEntity<String> exist(@PathVariable String drive) 
	{
		File disk= new File(drive);
		driveService.setFile(disk);
		if(!driveService.driveExist()) 
		{
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok("drive existe "+drive);
	}
	
	@GetMapping("/{drive}/info")
	public ResponseEntity<?> info(@PathVariable String drive) 
	{
		driveService.setFile(new File(drive));
		return ResponseEntity.ok().body(DriveDTO.builder().label(drive)
										.SpaceUsed(driveService.getSpaceUsed())
										.SpaceFree(driveService.getSpaceFree())
										.SpaceTotal(driveService.getSpaceTotal())
										.getSpaceUsable(driveService.getSpaceUsable())
										.build());
	}
	
	@GetMapping("/{drive}/SpaceUsed")
	public ResponseEntity<?> SpaceUsed(@PathVariable String drive) 
	{
		driveService.setFile(new File(drive));
		return ResponseEntity.ok().body(DriveDTO.builder().label(drive).SpaceUsed(driveService.getSpaceUsed()).build());
	}
	
	
	@GetMapping("/{drive}/files")
	public ResponseEntity<?> files(@PathVariable String drive) 
	{
		driveService.setFile(new File(drive));
		if(driveService.files().isEmpty()) 
		{
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok().body(driveService.files().get());
	}
	
	
	@GetMapping("/memory/info")
	public ResponseEntity<?> memory() 
	{
		return ResponseEntity.ok().body(driveService.getMemory());
	}
	
	@GetMapping("/cpu/info")
	public ResponseEntity<?> cpu() 
	{
		return ResponseEntity.ok().body(driveService.cpu());
	}
	
	

}