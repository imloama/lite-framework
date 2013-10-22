package com.itwarcraft.lite.core;

import java.io.IOException;
import java.lang.reflect.Method;
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
import com.itwarcraft.lite.annotation.Interceptors;
import com.itwarcraft.lite.annotation.Path;
import com.itwarcraft.lite.converter.ConverterFactory;
import com.itwarcraft.lite.mvc.Action;
import com.itwarcraft.lite.mvc.ActionContext;
import com.itwarcraft.lite.mvc.ActionExceptionHandler;
import com.itwarcraft.lite.mvc.ActionInvocation;
import com.itwarcraft.lite.mvc.ActionResult;
import com.itwarcraft.lite.mvc.Intercepter;
import com.itwarcraft.lite.mvc.Interceptor;
import com.itwarcraft.lite.mvc.URLMatcher;
import com.itwarcraft.lite.util.ClassUtil;

/**
 * 入口函数,拦截所有的请求，并调用ActionInvocation执行action
 * @author itwarcraft@gmail.com
 *
 */
public class DispatcherFilter implements Filter {

	private static final Logger logger = Logger.getLogger(DispatcherFilter.class);
	
	private static final String REQUEST_JSON = "application/json";
	private static final String REQUEST_XML = "application/xml";
	
	private URLMatcher[] matchers = null;
	private Map<URLMatcher, Action> matcherActionMap = new HashMap<URLMatcher, Action>();
	private ConverterFactory converterFactory = new ConverterFactory();
	
	/**
	 * 初始化
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("初始化容器");
		String devMode = filterConfig.getInitParameter("devMode");
		if (devMode != null && ("true".equalsIgnoreCase(devMode) || "false".equalsIgnoreCase(devMode))) {
			App.init(Boolean.parseBoolean(devMode));
		}
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
						Interceptors interceptors = m.getAnnotation(Interceptors.class);
						Class<? extends Intercepter>[] inters = interceptors.value();
						//拦截器数组
						Intercepter[] ins = new Intercepter[inters.length];
						int i=0;
						for(Class<? extends Intercepter> intercs : inters)
						{
							try {
								ins[i]=intercs.newInstance();
							} catch (InstantiationException e) {
								logger.error("初始化拦截器错误！");
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								logger.error("初始化拦截器错误！");
								e.printStackTrace();
							}
							i++;
						}//interceptors 循环结束 
						Action action = new Action(clasz,m,ins);
						this.matcherActionMap.put(matcher, action);
					}//
				}//循环method结束
			}
			
			
		}//class循环结束
		
		if(this.matcherActionMap.size()==0){
			throw new RuntimeException("the size of urlMatcherActionMap is 0");
		}else{
			
		}
		
		this.matchers = this.matcherActionMap.keySet().toArray(new URLMatcher[this.matcherActionMap.size()]);
		
		//url地址排序
		Arrays.sort(this.matchers,new Comparator<URLMatcher>(){

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
		});//排序结束
		
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
					ActionExceptionHandler exceptionHandler = new ActionExceptionHandler();
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
			HttpServletResponse response, Object result) throws ServletException, IOException {
		if (result == null) {
			return;
		}
		if (result instanceof ActionResult) {
			ActionResult render = (ActionResult) result;
			String r = request.getContentType();
			//如果是json请求
			//如果是xml请求
			//如果是普通请求
			if(REQUEST_JSON.equalsIgnoreCase(r)){
				render.renderToJSON(request, response);
			}else if(REQUEST_XML.equalsIgnoreCase(r)){
				render.renderToXML(request, response);
			}else{
				render.render(request, response);
			}
			return;
		}
		// 这里没有实现-请自己实现
		if (result instanceof String) {
			String string = (String) result;
			if (string.startsWith("redirect:")) {
				response.sendRedirect(string.substring("redirect:".length()));
				return;
			}
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

}
