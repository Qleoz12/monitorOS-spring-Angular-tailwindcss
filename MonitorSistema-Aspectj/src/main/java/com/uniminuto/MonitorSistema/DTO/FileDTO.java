package com.uniminuto.MonitorSistema.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileDTO 
{
	private String label;
	private String type;
	private String size;
	
	private Boolean isfolder;
	private Boolean isFile;
	
}
