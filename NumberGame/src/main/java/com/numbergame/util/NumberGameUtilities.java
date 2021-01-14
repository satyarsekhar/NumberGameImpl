package com.numbergame.util;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

public class NumberGameUtilities {
	static int number;

	static HashMap<String, String> questionMap = new HashMap<>();

	public static HttpHeaders setHeaders(HttpServletRequest request, String answer) {
		HttpHeaders headers = null;
		Enumeration<String> headerVals = request.getHeaders("number-game-id");
		boolean headerFound = false;
		if (headerVals != null) {
			while (headerVals.hasMoreElements()) {
				String h = (String) headerVals.nextElement();
				questionMap.put(h, answer);
				headers = new HttpHeaders();
				headers.add("number-game-id", String.valueOf(h));
				headerFound = true;
				break;
			}
		}

		if (!headerFound) {
			headers = new HttpHeaders();
			headers.add("number-game-id", String.valueOf(number));
			questionMap.put(String.valueOf(number), answer);
			number++;

		}
		return headers;
	}
	
	public static String getQuestionFromHeader(HttpServletRequest request) {
		Enumeration<String> headerVals = request.getHeaders("number-game-id");
		String responseString = null;
		if (headerVals != null) {
			while (headerVals.hasMoreElements()) {
				String h = (String) headerVals.nextElement();
				responseString = questionMap.get(h);
				break;
			}
		}
		return responseString;
	}
}
