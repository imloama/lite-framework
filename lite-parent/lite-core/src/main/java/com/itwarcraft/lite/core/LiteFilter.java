package com.itwarcraft.lite.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.itwarcraft.lite.annotation.Act;
import com.itwarcraft.lite.annotation.Intercept;
import com.itwarcraft.lite.annotation.Intercepters;
import com.itwarcraft.lite.annotation.Path;
import com.itwarcraft.lite.base.Intercepter;
import com.itwarcraft.lite.converter.ConverterFactory;
import com.itwarcraft.lite.mvc.Action;
import com.itwarcraft.lite.mvc.ActionContext;
import com.itwarcraft.lite.mvc.DefaultExceptionHandler;
import com.itwarcraft.lite.mvc.ActionInvocation;
import com.itwarcraft.lite.mvc.ActionResult;
import com.itwarcraft.lite.mvc.URLMatcher;
import com.itwarcraft.lite.mvc.ViewType;
import com.itwarcraft.lite.util.ClassUtil;

/**
 * 入口函数,拦截所有的请求，并调用ActionInvocation执行action
 * @author itwarcraft@gmail.com
 */

public class LiteFilter implements Filter {

	private static final Logger logger = Logger.getLogger(LiteFilter.class);
	
	private URLMatcher[] matchers = null;
	private Map<URLMatcher, Action> matcherActionMap = new HashMap<URLMatcher, Action>();
	
	/**
	 * 全局Interceptor列表，表示在Action前执行
	 */
	private final List<Intercepter> interceptersBefore = new ArrayList<Intercepter>();
	/**
	 * 全局Interceptor列表，表示在Action后执行
	 */
	private final List<Intercepter> interceptersAfter = new ArrayList<Intercepter>();
	
	private ConverterFactory converterFactory = new ConverterFactory();
	
	/**
	 * 初始化
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("初始化容器");
		boolean dev = false;
		ViewType type = ViewType.JSP;
		String devMode = filterConfig.getInitParameter("devMode");
		String template = filterConfig.getInitParameter("template");
		if (devMode != null && ("true".equalsIgnoreCase(devMode) )) {
			dev = true;
		}
		logger.info("当前的系统运行模式为（true表示为开发模式，false表示为运行模式）："+dev);
		//设定视图模板
		if(template!=null){
			if("freemarker".equalsIgnoreCase(template)){
				type = ViewType.FREE_MARKER;
			}else if("velocity".equalsIgnoreCase(template)){
				type = ViewType.VELOCITY;
			}else if("jsp".equalsIgnoreCase(template)){
				type = ViewType.JSP;
			}else if("html".equalsIgnoreCase(template)){
				type = ViewType.HTML;
			}else{
				//实现自定义视图，可以在web.xml中配置自定义视图的class名称
				type = ViewType.CUSTOM.setTempate(template);
				try {
					PluginFactory.addPlugin("template", template);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("加载自定义模板出错了！");
				} 
			}
		}
		logger.info(type.toString());
		Lite.init(dev, type);
		
		//初始化Action
		//初始化拦截器
		AnnotationBuilder builder = new AnnotationBuilder();
		try {
			builder.buildIntercepters();
			builder.buildActions();
		} catch (InstantiationException e) {
			e.printStackTrace();
			logger.error("初始化拦截器或ACTION失败 !");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.error("初始化拦截器或ACTION失败 !");
		}

		
		if(this.matcherActionMap.size()==0){
			throw new RuntimeException("the size of urlMatcherActionMap is 0");
		}
		this.matchers = this.matcherActionMap.keySet().toArray(new URLMatcher[this.matcherActionMap.size()]);
		//url地址排序
		Arrays.sort(this.matchers,new Sort());//排序结束
		
		//扫描所有的interceptor，创建全局的拦截器    添加1个用户登陆限制的拦截器，mycandy中的
		
		
	}
	
	


	public void doFilter(ServletRequest req, ServletResponse rep,
			FilterChain filterChain) throws IOException, ServletException {
		/**
		 * <pre>
		 * 		 1.关于get出现乱码的问题处理的办法是约定:get提交的方式只是数字和字母
		 * 		 2.1.jsp页面必须这样设置：<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
		 * 		 2.2request.setCharacterEncoding("utf-8");
		 * 		 2.3response.setCharacterEncoding("utf-8");
		 * </pre>
		 */
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) rep;

		// 编码设置
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("SET-COOKIE", "JSESSIONID=" + request.getSession().getId() + ";HttpOnly");

		// 相对于系统根目录的访问的路径
		String relativeUrlForWebRoot = request.getServletPath();
		if ("get".equalsIgnoreCase(request.getMethod())) {
			relativeUrlForWebRoot = new String(relativeUrlForWebRoot.getBytes("iso-8859-1"), "utf-8");
		}
		// 访问参数
		String queryString = request.getQueryString();
		if (!"/favicon.ico".equals(relativeUrlForWebRoot)) {
			logger.debug("ip:" + (request.getRemoteAddr() == null ? "???.???.???.???" : request.getRemoteAddr()) + "访问路径参数是:" + relativeUrlForWebRoot + (queryString == null ? "" : "?" + queryString));
		}
		/**
		 * <pre>
		 * isHandled变量为true标示在Action中找到相应的Action并执行,为false则进行其他的Filter执行
		 * </pre>
		 */
		boolean isHandled = false;
		try {
			// 里面不能出现404跳转,可能存在其他的非JFly访问方式
			// ·/favicon.ico·bug过滤处理
			if (!"/favicon.ico".equals(relativeUrlForWebRoot)) {

				ActionContext.setActionContext(request.getSession().getServletContext(), request, response);
				try {
					Object result = service(request, response);
					if (result == null) {
						isHandled = false;
					} else {
						handleResult(request, response, result);
						isHandled = true;
					}

				} catch (Exception e) {
					isHandled = false;
					DefaultExceptionHandler exceptionHandler = new DefaultExceptionHandler();
					exceptionHandler.handle(request, response, e);

				} finally {
					ActionContext.removeActionContext();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		if (isHandled == false) {
			filterChain.doFilter(req, rep);
		}

	}

	
	private void handleResult(HttpServletRequest request,
			HttpServletResponse response, Object result) throws Exception {
		if (result == null) {
			return;
		}
		if (result instanceof ActionResult) {
			ActionResult render = (ActionResult) result;
			//如果是json请求
			//如果是xml请求
			//如果是普通请求  根据ActionResult中设定的参数类型，默认  
			//可以在web.xml中配置视图的模板类型，然后调用不同的视图
			render.render(request, response);
			return;
		}
		// 如果方法返回的是string，则直接打印String的内容
		if (result instanceof String) {
			String string = (String) result;
			ActionResult render = new ActionResult(ViewType.TEXT);
			render.addModel("lite_view_text", string);
			render.render(request, response);
			//render.renderString(request, response, string);
			/*
			
			if (string.startsWith("redirect:")) {
				response.sendRedirect(string.substring("redirect:".length()));
				return;
			}*/
		}
		throw new ServletException("Cannot handle result with type '" + result.getClass().getName() + "'.");
	}





	public Object service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 相对于系统根目录的访问的路径
		String relativeUrlForWebRoot = request.getServletPath();
		if ("get".equalsIgnoreCase(request.getMethod())) {
			relativeUrlForWebRoot = new String(relativeUrlForWebRoot.getBytes("iso-8859-1"), "utf-8");
		}
		// 对非文件上传的字符编码处理
		
		for(URLMatcher matcher:this.matcherActionMap.keySet()){
			//保证是否为get post等方法
			if(!matcher.requestMethod.equalsIgnoreCase(request.getMethod())){
				continue;
			}
			String[] parameters = matcher.getMatchedParameters(relativeUrlForWebRoot);
			if (parameters != null) {
				Action action = matcherActionMap.get(matcher);

				Object[] arguments = new Object[parameters.length];
				for (int i = 0; i < parameters.length; i++) {
					Class<?> parameterType = action.parameterTypes[i];

					if (parameterType.equals(String.class)) {
						arguments[i] = parameters[i];
					} else {
						try {
							arguments[i] = converterFactory.convert(parameterType, parameters[i]);
						} catch (Exception e) {
							throw new RuntimeException("转换异常");
						}
					}
				}
				return new ActionInvocation(action,arguments).invoke();
			}
		}
		
		return null;
	}
	
	
	private Set<String> findPublicMethodNamesInObjectClass() {
		Set<String> publicMethodNameSet = new HashSet<String>();
		Method[] methods = Object.class.getMethods();
		for (Method method : methods) {
			publicMethodNameSet.add(method.getName());
		}
		// jrebel产生的代理方法__rebel_clinit,需要将此方法排除
		publicMethodNameSet.add("__rebel_clinit");
		return publicMethodNameSet;
	}

	public void destroy() {

	}
	
	/**
	 * URLMatcher排序类
	 * @author itwarcraft@gmail.com
	 *
	 */
	public final class Sort implements Comparator<URLMatcher>{

		public int compare(URLMatcher m1, URLMatcher m2) {
			String u1 = m1.urlMapping;
			String u2 = m2.urlMapping;
			int c = u1.compareTo(u2);
			if(c==0){
				throw new RuntimeException("Cannot mapping one url '" + u1 + "' to more than one action method.");
			}else{
				return 0;	
			}
		}
		
	}
	
	/**
	 * Initionalize Annotation 初始化类
         * ，由于注解的拦截器需要有一定的顺序，在注解上提供order字段，按从小到大的顺序排列。
	 * @author itwarcaft@gmail.com
	 * @time 2011-11-11 18:15
	 *
	 */
	public final class AnnotationBuilder{
		
		/**
		 * 找到所有的全局拦截器，保存到DispatcherFilter.java类的变量中
		 * @throws IllegalAccessException 
		 * @throws InstantiationException 
		 */
		public void  buildIntercepters() throws InstantiationException, IllegalAccessException{
			
			List<Class<?>> interceptors = ClassUtil.getClassListByAnnotation(Intercept.class);
			for(Class<?> clasz : interceptors){
				Intercepter inter = (Intercepter)clasz.newInstance();
				Intercept intercep = (Intercept) clasz.getAnnotation(Intercept.class); 
				String value = intercep.value();
				if("before".equals(value)){
					interceptersBefore.add(inter);
				}else{
					interceptersAfter.add(inter);
				}
			}
		}
		
		/**
		 * 通过注解，找到所有的Action,和注解的拦截器，由于注解的拦截器需要有一定的顺序，在注解上提供order字段，按从小到大的顺序排列。
		 * @throws IllegalAccessException 
		 * @throws InstantiationException 
		 */
		public void buildActions() throws InstantiationException, IllegalAccessException{
			//1 扫描所有的注解action
			List<Class<?>> list = ClassUtil.getClassListByAnnotation(Act.class);
			Set<String> publicMethodNameSetInObjectClass = findPublicMethodNamesInObjectClass();
			
			for(Class<?> clasz : list){
				try {
					Class.forName(clasz.getName());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getCause());
				}
				
				String urlo = clasz.getAnnotation(Act.class).value();
				
				Intercepter[] interBefore = null;
				Intercepters before = clasz.getAnnotation(Intercepters.class);
				if(before!=null){
					Class<? extends Intercepter>[] a1 = before.value();
					interBefore = new Intercepter[a1.length];
					int i = 0;
					for(Class<? extends Intercepter> cls : a1){
						interBefore[i] = cls.newInstance();
						i++;
					}
				}
				
			
				Method[] methods = clasz.getMethods();
				if(methods!=null&&methods.length>0){
					for (Method m : methods) {
						//判断方法是否有url地址的注解
						if(!publicMethodNameSetInObjectClass.contains(m)
								&&m.isAnnotationPresent(Path.class)){
							Path path = m.getAnnotation(Path.class);
							String url = path.value();
							int index = url.indexOf(":");
							
							URLMatcher matcher = null;
							if(index>0){
								String[] ut = url.split(":");
								matcher = new URLMatcher(ut[0],urlo+ut[1]);
							}else{
								matcher = new URLMatcher(url);
							}
							
							//url地址中包括的参数个数必须与方法的参数相同
							if(matcher.getArgumentCount()!=m.getParameterTypes().length){
								logger.error("url地址中的参数与方法参数的数据不相同...");
								continue;
							}
							
							
							//获取拦截器
							Intercepters interceptors = m.getAnnotation(Intercepters.class);
							Intercepter[] insBefore = null;
							if(interceptors!=null){
								Class<? extends Intercepter>[] inters = interceptors.value();
								//拦截器数组
								insBefore = new Intercepter[inters.length+(interBefore==null?0:interBefore.length)];
								int i=0;
								for(Intercepter ints : interBefore){
									insBefore[i] = ints;
									i++;
								}
								
								for(Class<? extends Intercepter> intercs : inters)
								{
									try {
										insBefore[i]=intercs.newInstance();
									} catch (InstantiationException e) {
										logger.error("初始化拦截器错误！");
										e.printStackTrace();
									} catch (IllegalAccessException e) {
										logger.error("初始化拦截器错误！");
										e.printStackTrace();
									}
									i++;
								}//interceptors 循环结束 
							}
							
							
							Action action = new Action(clasz,m,insBefore);
							matcherActionMap.put(matcher, action);
						}//
					}//循环method结束
				}
				
				
			}//class循环结束
			
			
			
			List<Class<?>> actions = ClassUtil.getClassListByAnnotation(Act.class);
			for(Class<?> clasz : actions){
				Object target = clasz.newInstance();
				
				
			}
		}
		
	}

}
