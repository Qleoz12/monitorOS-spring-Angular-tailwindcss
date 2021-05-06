/*
 * Copyright 2019. Fondo Nacional del Ahorro
 */
package com.uniminuto.MonitorSistema.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * Controla de manera globlal las solicitudes de or&iacute;genes cruzados (CORS)
 *
 * @author Zamir Garc&iacute;a Romero - zagarcia@ayesa.com
 * @version 1.0
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilterConfiguration  implements Filter {
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		final HttpServletResponse response = (HttpServletResponse) res;
		final HttpServletRequest request = (HttpServletRequest) req;
		
		Map<String, String> corsOpts = new HashMap<>();
		corsOpts.put("Access-Control-Allow-Origin","*");
		corsOpts.put("Access-Control-Allow-Methods","POST, GET, PUT, DELETE, OPTIONS");
		corsOpts.put("Access-Control-Max-Age","3600");
		corsOpts.put("Access-Control-Allow-Headers","Content-Type, x-requested-with, authorization, token, access-control-allow-headers,");
		
		for (String key: corsOpts.keySet()) {
			response.setHeader(key, corsOpts.get(key));
		}
		
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, res);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
