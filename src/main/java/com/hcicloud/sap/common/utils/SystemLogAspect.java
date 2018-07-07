package com.hcicloud.sap.common.utils;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Service
public class SystemLogAspect {
	
	@Autowired
	HttpServletRequest request;

	// 在类里面写方法，方法名诗可以任意的。此处我用标准的before和after来表示
	// 此处的JoinPoint类可以获取，action所有的相关配置信息和request等内置对象。
	public void before(JoinPoint jp) {
		System.out.println("调用前拦截:" + jp.getTarget().getClass().getName() + ":" + jp.getSignature().getName());
		
		System.out.println(jp.getTarget().getClass().getSimpleName());
		System.out.println(jp.getTarget().getClass().getAnnotation(RequestMapping.class).toString());
		
		String [] annotions = jp.getTarget().getClass().getAnnotation(RequestMapping.class).value();
		String ip = request.getRemoteAddr();
		for (int i = 0; i < annotions.length; i++) {
			System.out.println("value:" + annotions[i]);
		}
		if (annotions.length <= 0) {
			System.out.println("no annotion value is found!");
			return;
		}
		Object args[] = jp.getArgs();
        if(args.length<=0){
            return;
        }
//		logServiceI.add(annotions[0], jp.getSignature().getName(),args[0],ip);
	}

	public void after(JoinPoint jp) {

		System.out.println("调用后拦截:" + jp.getTarget().getClass().getName() + ":" + jp.getSignature().getName());
		
		System.out.println(jp.getTarget().getClass().getSimpleName());
		System.out.println(jp.getTarget().getClass().getAnnotation(RequestMapping.class).toString());
		
		String [] annotions = jp.getTarget().getClass().getAnnotation(RequestMapping.class).value();
		String ip = request.getRemoteAddr();
		for (int i = 0; i < annotions.length; i++) {
			System.out.println("value:" + annotions[i]);
		}
		if (annotions.length <= 0) {
			System.out.println("no annotion value is found!");
			return;
		}
		Object args[] = jp.getArgs();
        if(args.length<=0){
            return;
        }
//		logServiceI.add(annotions[0], jp.getSignature().getName(),args[0],ip);
	}
}
