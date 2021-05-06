package com.uniminuto.MonitorSistema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class utils {

	
	@Bean
	public com.uniminuto.MonitorSistema.utils.OSUtils OSUtils(){
		//Default rounds: 10
		return new com.uniminuto.MonitorSistema.utils.OSUtils();
	}
}

