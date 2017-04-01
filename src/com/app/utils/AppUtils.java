package com.app.utils;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class AppUtils {
	public HashMap<String, String> getReqMap(HttpServletRequest request) {
		HashMap<String, String> data = new HashMap<>();

		for (String iterable_element : request.getParameterMap().keySet()) {
			data.put(iterable_element, request.getParameter(iterable_element));
		}
		Enumeration attrs = request.getAttributeNames();
		while (attrs.hasMoreElements()) {
			String name = (String) attrs.nextElement();
			String value = "" + request.getAttribute(name);
			data.put(name, value);
		}
		return data;
	}
}
