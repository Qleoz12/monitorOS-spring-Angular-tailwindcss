package com.uniminuto.MonitorSistema.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniminuto.MonitorSistema.DTO.FileDTO;
import com.uniminuto.MonitorSistema.utils.OSUtils;
import com.uniminuto.MonitorSistema.utils.OSUtils.OS;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.VirtualMemory;
import oshi.software.os.OperatingSystem;

import java.nio.file.Files;
import java.nio.file.Path;

import oshi.util.FormatUtil;
import oshi.util.Util;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Service
@Aspect
public class DriveService {

	
	@Autowired
	OSUtils osUtils;
	
	private File file;
	
	public long getSpaceTotal() 
	{
		return file.getTotalSpace();
	}
	
	public long getSpaceFree() 
	{
		return file.getFreeSpace();
	}
	
	public long getSpaceUsable() 
	{
		return file.getUsableSpace();
	}
	
	public long getSpaceUsed() 
	{
		return file.getTotalSpace()-file.getUsableSpace();
	}
	
	public boolean driveExist() 
	{
		boolean exists= file.exists();
		if(exists)
		{
			for(File drive :file.listRoots())
			{
				log.debug(drive.toString());
				String drivePath=drive.toString();
				
				drivePath= osUtils.isWindows() ? drivePath.replace("\\", "") : drivePath;
				
				if(file.getPath().equalsIgnoreCase(drivePath)) 
				{
					return true;
				}
			}
			
		}
		return false;
	}
	
	
	public Optional<List<FileDTO>> files() 
	{
		boolean exists= file.exists();
		List list= new  ArrayList<FileDTO>();
		try 
		{
			if(exists)
			{
				//return Optional.of(file.list());
				String[] files =file.list();
				File fileLocal;
				for (int i = 0; i < files.length; i++) 
				{
					String prePathfile=file.getPath()!=null?file.getPath()+File.separator:"";
					String pathfile=prePathfile+files[i];
					fileLocal=new File(pathfile);
					String type;
					type = Files.probeContentType(Path.of(fileLocal.getPath()));
					list.add(FileDTO.builder()
							.label(fileLocal.getName())
							.size(""+fileLocal.length())
							.isfolder(fileLocal.isDirectory())
							.isFile(fileLocal.isFile())
							.type(type)
							.build());			
				}
			}
		
			return Optional.ofNullable(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Optional.ofNullable(null);
	}
	

	
	public Optional<File[]> drives() 
	{	
		file= new File(System.getenv("SystemDrive"));
		boolean exists= file.exists();
		if(exists)
		{
			return Optional.of(file.listRoots());
	
		}
		return Optional.ofNullable(null);
	}
	
	 public GlobalMemory getMemory()
	 {
		 
//		 List<String> oshi = new ArrayList<>();
//	        oshi.add("Physical Memory: \n " + memory.toString());
//	        VirtualMemory vm = memory.getVirtualMemory();
//	        oshi.add("Virtual Memory: \n " + vm.toString());
//	        List<PhysicalMemory> pmList = memory.getPhysicalMemory();
//	        if (!pmList.isEmpty()) {
//	            oshi.add("Physical Memory: ");
//	            for (PhysicalMemory pm : pmList) {
//	                oshi.add(" " + pm.toString());
//	            }
//	        }
//	        
//	        StringBuilder output = new StringBuilder();
//	        for (int i = 0; i < oshi.size(); i++) {
//	            output.append(oshi.get(i));
//	            if (oshi.get(i) != null && !oshi.get(i).endsWith("\n")) {
//	                output.append('\n');
//	            }
//	        }
//	        log.info("Printing Operating System and Hardware Info:{}{}", '\n', output);
		 SystemInfo si = new SystemInfo();
		 OperatingSystem os = si.getOperatingSystem();
		 HardwareAbstractionLayer hal = si.getHardware();
		 return hal.getMemory();
	 }
	 
	 
	 public CentralProcessor cpu()
	 {
		 List<String> oshi = new ArrayList<>();
		 SystemInfo si = new SystemInfo();
		 OperatingSystem os = si.getOperatingSystem();
		 HardwareAbstractionLayer hal = si.getHardware();
		 CentralProcessor processor=hal.getProcessor();
		 
		        oshi.add("Context Switches/Interrupts: " + processor.getContextSwitches() + " / " + processor.getInterrupts());

		        long[] prevTicks = processor.getSystemCpuLoadTicks();
		        long[][] prevProcTicks = processor.getProcessorCpuLoadTicks();
		        oshi.add("CPU, IOWait, and IRQ ticks @ 0 sec:" + Arrays.toString(prevTicks));
		        // Wait a second...
		        Util.sleep(1000);
		        long[] ticks = processor.getSystemCpuLoadTicks();
		        oshi.add("CPU, IOWait, and IRQ ticks @ 1 sec:" + Arrays.toString(ticks));
		        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
		        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
		        long sys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
		        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
		        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
		        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
		        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
		        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
		        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

		        oshi.add(String.format(
		                "User: %.1f%% Nice: %.1f%% System: %.1f%% Idle: %.1f%% IOwait: %.1f%% IRQ: %.1f%% SoftIRQ: %.1f%% Steal: %.1f%%",
		                100d * user / totalCpu, 100d * nice / totalCpu, 100d * sys / totalCpu, 100d * idle / totalCpu,
		                100d * iowait / totalCpu, 100d * irq / totalCpu, 100d * softirq / totalCpu, 100d * steal / totalCpu));
		        oshi.add(String.format("CPU load: %.1f%%", processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100));
		        double[] loadAverage = processor.getSystemLoadAverage(3);
		        oshi.add("CPU load averages:" + (loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0]))
		                + (loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1]))
		                + (loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2])));
		        // per core CPU
		        StringBuilder procCpu = new StringBuilder("CPU load per processor:");
		        double[] load = processor.getProcessorCpuLoadBetweenTicks(prevProcTicks);
		        for (double avg : load) {
		            procCpu.append(String.format(" %.1f%%", avg * 100));
		        }
		        oshi.add(procCpu.toString());
		        long freq = processor.getProcessorIdentifier().getVendorFreq();
		        if (freq > 0) {
		            oshi.add("Vendor Frequency: " + FormatUtil.formatHertz(freq));
		        }
		        freq = processor.getMaxFreq();
		        if (freq > 0) {
		            oshi.add("Max Frequency: " + FormatUtil.formatHertz(freq));
		        }
		        long[] freqs = processor.getCurrentFreq();
		        if (freqs[0] > 0) {
		            StringBuilder sb = new StringBuilder("Current Frequencies: ");
		            for (int i = 0; i < freqs.length; i++) {
		                if (i > 0) {
		                    sb.append(", ");
		                }
		                sb.append(FormatUtil.formatHertz(freqs[i]));
		            }
		            oshi.add(sb.toString());
		        }
				return processor;
		    
	 }
}
