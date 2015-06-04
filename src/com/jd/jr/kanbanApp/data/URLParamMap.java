package com.jd.jr.kanbanApp.data;

import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.net.UrlQuerySanitizer.ParameterValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class URLParamMap implements Map<String, String> {

	private static final String TAG = "URLParamMap";

	private Map<String, String> map = new HashMap<String, String>();

	private final String charset;

	public URLParamMap() {
		charset = "UTF-8";
	}

	public URLParamMap(String charset) {
		this.charset = charset;
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		throw new RuntimeException("Can't use putAll method");
	}

	public Set<Entry<String, String>> entrySet() {
		return map.entrySet();
	}

	public boolean equals(Object object) {
		return map.equals(object);
	}

	public String get(Object key) {
		return map.get(key);
	}

	public int hashCode() {
		return map.hashCode();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public String put(String key, String value) {
		try {
			value = URLEncoder.encode(value, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return map.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends String> arg0) {
		throw new RuntimeException("Can't use putAll method");
	}

	public String remove(Object key) {
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection<String> values() {
		return map.values();
	}

	public void put(Uri uri) {
		String url = uri.toString();
		UrlQuerySanitizer urlQuerySanitizer = new UrlQuerySanitizer(url);
		List<ParameterValuePair> parameterList = urlQuerySanitizer.getParameterList();
		for (Iterator<ParameterValuePair> iterator = parameterList.iterator(); iterator.hasNext();) {
			ParameterValuePair parameterValuePair = (ParameterValuePair) iterator.next();
			String key = parameterValuePair.mParameter;
			String value = uri.getQueryParameter(key);
			if(value != null){
				put(key, value);
			}
		}
	}

}
