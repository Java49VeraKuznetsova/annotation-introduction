package telran.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import telran.test.annotation.BeforeEach;
import telran.test.annotation.Test;

public class TestRunner implements Runnable {
	private Object testObj;
	
	
	public TestRunner(Object testObj) {
		super();
		this.testObj = testObj;
			}

	
	@Override
	public void run() {
		
		Class<?> clazz = testObj.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		
		Method[] beforeEachMetods = getBeforeEachMethdos(methods);
		runTestMethods(methods, beforeEachMetods);
	}

	private  Method[] getBeforeEachMethdos(Method[] methods) {
	
		return Arrays.stream(methods)
				.filter(m -> m.isAnnotationPresent(BeforeEach.class))
				.toArray(Method[]::new);
	}

	private void runTestMethods(Method[] methods, Method[] beforeEachMethods) {
		for (Method method: methods) {
		if(method.isAnnotationPresent(Test.class)) {
			method.setAccessible(true);
			try {
				runMethods(beforeEachMethods);
				method.invoke(testObj);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException();
			}
		}
		}
	}

	private void runMethods(Method[] methods) {
		for (Method method: methods) {
			method.setAccessible(true);
				try {
									
					method.invoke(testObj);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException();
				}
	
			}
	}
}
