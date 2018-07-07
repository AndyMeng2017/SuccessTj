package com.hcicloud.sap.common.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class SystemLogAspect_Annotion {
	
	/*@Autowired
	SystemLogServiceI systemLogService;*/

	//定义一个切入点
	@Pointcut("execution(* com.hcicloud.sap.controller ..*.add(..))")
	private void addMethod(){}
	@Pointcut("execution(* com.hcicloud.sap.controller ..*.delete(..))")
	private void deleteMethod(){}
	@Pointcut("execution(* com.hcicloud.sap.controller ..*.edit(..))")
	private void editMethod(){}
	@Pointcut("execution(* com.hcicloud.sap.controller ..*.login(..))")
	private void loginMethod(){}
	@Pointcut("execution(* com.hcicloud.sap.controller ..*.logout(..))")
	private void logoutMethod(){}
	
	@Before("addMethod() || editMethod() || deleteMethod() || loginMethod() || logoutMethod()")
	public void doAccessCheck(){
		System.out.println("前置通知");
	}
	
	@AfterReturning("addMethod() || editMethod() || deleteMethod() || loginMethod() || logoutMethod()")
	public void doAfter(){
		System.out.println("后置通知");
	}
	
	@After("addMethod() || editMethod() || deleteMethod() || loginMethod() || logoutMethod()")
	public void after(/*JoinPoint jp*/){
		System.out.println("最终通知");
		/*
		System.out.println(jp.getTarget().getClass().getSimpleName());
		System.out.println(jp.getTarget().getClass().getAnnotation(RequestMapping.class).value());
		System.out.println(jp.getTarget().getClass().getAnnotation(RequestMapping.class).toString());
		
		String [] annotions = jp.getTarget().getClass().getAnnotation(RequestMapping.class).value();
		for (int i = 0; i < annotions.length; i++) {
			System.out.println("value:" + annotions[i]);
		}
		
		systemLogService.add(new SystemLog(annotions[0], jp.getSignature().getName()));
		*/
	}
	
	@AfterThrowing("addMethod() || editMethod() || deleteMethod() || loginMethod() || logoutMethod()")
	public void doAfterThrow(){
		System.out.println("例外通知");
	}
	
	@Around("addMethod() || editMethod() || deleteMethod() || loginMethod() || logoutMethod()")
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable{
		System.out.println("进入环绕通知");
		//执行该方法
		Object object = pjp.proceed();
		System.out.println("退出方法");
		return object;
	}
}
