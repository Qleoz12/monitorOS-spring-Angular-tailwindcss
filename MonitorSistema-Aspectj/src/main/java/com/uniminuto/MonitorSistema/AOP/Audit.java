package com.uniminuto.MonitorSistema.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Component
public class Audit {

	
	@Around("execution(* com.uniminuto.MonitorSistema.*.*.*(..))")
	public Object aroundWebMethodE(ProceedingJoinPoint pjp) throws Throwable {      
	    String packageName = pjp.getSignature().getDeclaringTypeName();
	    String methodName = pjp.getSignature().getName();
	    long start = System.currentTimeMillis();
	    if(!pjp.getSignature().getName().equals("initBinder")) {
	       log.info("ingresando al  metodo [" + packageName + "." + methodName +  "]");
	    }
	    Object output = pjp.proceed();
	    long elapsedTime = System.currentTimeMillis() - start;
	    if(!methodName.equals("initBinder")) {
	       log.info("m [" + packageName + "." + methodName + "]; exec time (ms): " + elapsedTime);
	    }
	    return output;
	}
}
