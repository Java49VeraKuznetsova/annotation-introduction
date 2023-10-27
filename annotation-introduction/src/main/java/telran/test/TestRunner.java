package telran.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import telran.test.annotation.BeforeEach;
import telran.test.annotation.Test;

public class TestRunner implements Runnable {
	private Object testObj;
	
	
	public TestRunner(Object testObj) {
		super();
		this.testObj = testObj;
			}

	//@SuppressWarnings("null")
	@Override
	public void run() {
			Class<?> clazz = testObj.getClass();
			Method[] methods = clazz.getDeclaredMethods();
				Method beforeEachMethod = null;
            
           //for (Method method: methods){
				int i = 0;
				while (beforeEachMethod == null && i< methods.length) {
					 
					
			if (methods[i].isAnnotationPresent(BeforeEach.class)) {
				methods[i].setAccessible(true);
				beforeEachMethod = methods[i];
			
				
			}
			i++;
			}
			for(Method method: methods) {
				
				if(method.isAnnotationPresent(Test.class)) {
					method.setAccessible(true);
					try {
						if (beforeEachMethod != null) {
					
							beforeEachMethod.invoke(testObj);
					
						}
						method.invoke(testObj);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						System.out.println("error: " + e.getMessage());
					}
				}
			}

	}

}
