package com.itwarcraft.lite.mvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class URLMatcher {

	public final String requestMethod;
	public final String urlMapping;
	int[] argOrders;
	Pattern pattern;

	static final String[] Null = new String[0];

	static final String SafeChars = "/$-_.+!*'(),";

	/**
	 * Build UrlMatcher by given url like "/index/$1/$2.html".
	 * 
	 * @param urlMapping
	 *            UrlMapping may contains $1, $2, ... $9.
	 */
	public URLMatcher(String urlMapping) {
		String[] temp = urlMapping.split(":");
		if(temp.length<2){
			this.requestMethod = "get";
			this.urlMapping =urlMapping;
		}else{
			this.requestMethod = temp[0];
			this.urlMapping = temp[1];	
		}
		init();
	}


	public URLMatcher(String requestMethod,String url){
		this.requestMethod = requestMethod;
		this.urlMapping = url;
		init();
	}

	private void init(){
		StringBuilder sb = new StringBuilder(urlMapping.length() + 20);
		List<Integer> parameterList = new ArrayList<Integer>();
		Set<Integer> parameterSet = new HashSet<Integer>();
		// 正则表达式匹配开始
		sb.append('^');
		int startIndex = 0;
		for (;;) {
			int $index = urlMapping.indexOf('$', startIndex);
			// 找到$num索引
			if ($index != (-1) && $index < urlMapping.length() - 1 && isNum(urlMapping.charAt($index + 1))) {
				int num = urlMapping.charAt($index + 1) - '0';
				parameterSet.add(num);
				parameterList.add(num);

				add(sb, urlMapping.substring(startIndex, $index));
				addParameterMatch(sb);
				startIndex = $index + 2;// $num中num可选值为1-9
			} else {
				// 未找到$num索引
				add(sb, urlMapping.substring(startIndex, urlMapping.length()));
				break;
			}
		}
		sb.append('$');
		// 正则表达式匹配结束
		this.argOrders = new int[parameterList.size()];
		if (parameterList.size() > 0) {
			for (int i = 1; i <= parameterList.size(); i++) {
				// 顺序必须从1开始-N结尾
				if (!parameterSet.contains(i)) {
					throw new RuntimeException("missing parameter '$" + i + "'.");
				}
			}

			for (int i = 0; i < parameterList.size(); i++) {
				this.argOrders[i] = parameterList.get(i) - 1;
			}
		}

		this.pattern = Pattern.compile(sb.toString());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof URLMatcher) {
			return ((URLMatcher) obj).urlMapping.equals(this.urlMapping);
		}
		return false;
	}

	public int getArgumentCount() {
		return this.argOrders.length;
	}

	/**
	 * Test if the url is match the regex. If matched, the parameters are
	 * returned as String[] array, otherwise, null is returned.
	 */
	public String[] getMatchedParameters(String url) {
		Matcher matcher = pattern.matcher(url);
		if (!matcher.matches()) {
			return null;
		}
		
		if (argOrders.length == 0) {
			return Null;
		}

		String[] parameters = new String[argOrders.length];
		int length = argOrders.length;
		for (int i = 0; i < length; i++) {
			parameters[argOrders[i]] = matcher.group(i + 1);
		}
		return parameters;
	}

	@Override
	public int hashCode() {
		return urlMapping.hashCode();
	}

	/**
	 * 添加相关的值
	 */
	void add(StringBuilder stringBuilder, String string) {
		if (string == null) {
			throw new RuntimeException("string is null");
		}
		if (string != null && string.length() == 0) {
			return;
		}
		int length = string.length();
		for (int i = 0; i < length; i++) {
			char mychar = string.charAt(i);
			if (mychar >= 'a' && mychar <= 'z') {
				stringBuilder.append(mychar);
			} else if (mychar >= 'A' && mychar <= 'Z') {
				stringBuilder.append(mychar);
			} else if (mychar >= '0' && mychar <= '9') {
				stringBuilder.append(mychar);
			} else {
				int n = SafeChars.indexOf(mychar);
				if (n != -1) {
					stringBuilder.append('\\').append(mychar);
				} else {
					// 中文编码
					stringBuilder.append("\\u").append(Integer.toHexString(mychar).toUpperCase());
				}

			}
		}
	}

	void addParameterMatch(StringBuilder sb) {
		// 非/
		sb.append("([^\\/]*)");
	}

	boolean isNum(char mychar) {
		return mychar >= '1' && mychar <= '9';
	}
	
	


}
