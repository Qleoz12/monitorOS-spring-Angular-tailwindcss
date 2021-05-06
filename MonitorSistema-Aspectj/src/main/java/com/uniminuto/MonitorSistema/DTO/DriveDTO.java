package com.uniminuto.MonitorSistema.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DriveDTO 
{
	private String label;
	private Long SpaceUsed;
	private Long SpaceFree;
	private Long getSpaceUsable;
	private Long SpaceTotal;
	
	
}
