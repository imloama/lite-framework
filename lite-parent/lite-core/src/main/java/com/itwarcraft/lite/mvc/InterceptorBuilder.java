package com.itwarcraft.lite.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itwarcraft.lite.annotation.Interceptor;
import com.itwarcraft.lite.base.Intercepter;

/**
 * 拦截器构建类：查找注解构建拦截器Map
 */
public class InterceptorBuilder {
	private static Logger logging = Logger.getLogger(InterceptorBuilder.class);

	private static final Intercepter[] Null = new Intercepter[0];
	private Map<Class<Intercepter>, Intercepter> interceptorMap = new HashMap<Class<Intercepter>, Intercepter>();

	@SuppressWarnings("unchecked")
	/**
	 * 将系统最基础的拦截器数组转换为HashMap<Interceptor>
	 */
	public void addBaseInterceptorsToInterceptorMap(Intercepter[] baseInterceptors) {
		for (Intercepter interceptor : baseInterceptors) {
			interceptorMap.put((Class<Intercepter>) interceptor.getClass(), interceptor);
		}
	}

	/**
	 * 构建Action的拦截器
	 */
	public Intercepter[] buildActionInterceptors(Intercepter[] baseInterceptors, Class<?> controllerClass, Intercepter[] controllerInterceptors, Method method, Intercepter[] methodInterceptors) {

		/*
		ClearLayer controllerClearType = getClearTypeOnTheController(controllerClass);
		if (controllerClearType != null) {
			baseInterceptors = Null;
		}
		ClearLayer methodClearType = getClearTypeOnTheMethod(method);
		if (methodClearType != null) {
			// 该处的设计方式很好,至少去除Controller上面的拦截器
			controllerInterceptors = Null;
			if (methodClearType == ClearLayer.All) {
				baseInterceptors = Null;
			}
		}*/
		int size = (baseInterceptors == null ? 0 : baseInterceptors.length) + controllerInterceptors.length + methodInterceptors.length;
		if (size == 0) {
			return Null;
		} else {
			Intercepter[] allInterceptorsOnTheAction = new Intercepter[size];
			int index = 0;
			if (baseInterceptors != null) {
				for (int i = 0; i < baseInterceptors.length; i++) {
					allInterceptorsOnTheAction[index++] = baseInterceptors[i];
				}
			}

			for (int i = 0; i < controllerInterceptors.length; i++) {
				allInterceptorsOnTheAction[index++] = controllerInterceptors[i];
			}
			for (int i = 0; i < methodInterceptors.length; i++) {
				allInterceptorsOnTheAction[index++] = methodInterceptors[i];
			}
			return allInterceptorsOnTheAction;
		}

	}

	/**
	 * 构建控制器上面的拦截器
	 */
	public Intercepter[] buildControllerInterceptors(Class<?> controllerClass) {
		Interceptor intercepters = controllerClass.getAnnotation(Interceptor.class);
		return intercepters != null ? createInterceptors(intercepters) : Null;
	}

	/**
	 * 构建方法上面的拦截器
	 */
	public Intercepter[] buildMethodInterceptors(Method method) {
		Interceptor intercepters = method.getAnnotation(Interceptor.class);
		return intercepters != null ? createInterceptors(intercepters) : Null;
	}

	/**
	 * 创建拦截器:每个拦截器只实例化一次
	 */
	// 该类实现的模式是数据库存储模式,即有一个返回值和一个或者多个引用传值:interceptorMap是引用传值,interceptors是返回值
	private Intercepter[] createInterceptors(Interceptor before) {
		Intercepter[] interceptors = null;
		@SuppressWarnings("unchecked")
		// :Before(values={xx.class,yy.class})
		Class<Intercepter>[] interceptorClasses = (Class<Intercepter>[]) before.value();
		if (interceptorClasses != null && interceptorClasses.length > 0) {
			interceptors = new Intercepter[interceptorClasses.length];
			int length = interceptors.length;
			for (int i = 0; i < length; i++) {
				interceptors[i] = interceptorMap.get(interceptorClasses[i]);
				if (interceptors[i] != null) {
					// 只实例化一次
					continue;
				}
				try {
					interceptors[i] = interceptorClasses[i].newInstance();
					interceptorMap.put(interceptorClasses[i], interceptors[i]);
				} catch (Exception e) {
					logging.error(e);
					throw new RuntimeException(e);
				}
			}
		}
		return interceptors;
	}

	/**
	 * 得到标记在控制器上的ClearInterceptor的值
	 
	private ClearLayer getClearTypeOnTheController(Class<?> controllerClass) {

		ClearInterceptor clearInterceptor = controllerClass.getAnnotation(ClearInterceptor.class);
		return clearInterceptor != null ? clearInterceptor.value() : null;
	}
*/
	/**
	 * 得到标记在方法上的ClearInterceptor的值
	 
	private ClearLayer getClearTypeOnTheMethod(Method method) {
		ClearInterceptor clearInterceptor = method.getAnnotation(ClearInterceptor.class);
		return clearInterceptor != null ? clearInterceptor.value() : null;
	}*/

}
