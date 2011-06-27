package org.aa.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import it.sauronsoftware.base64.Base64;

public class AdminOperations{

	private String Key="key";
	
	public String getEncodedData(String S) {
		String result1 = null;

		result1 = Base64.encode(S).toString();

		return result1.toString();
	}

	public String getDecodedData(String S) {
		String result1 = null;
		if (org.apache.catalina.util.Base64.isBase64(S)) {
			result1 = Base64.decode(S).toString();
		}
		return result1.toString();
	}
	
	public String getRandomNumber() {

		final int PASSWORD_LENGTH = 8;
		StringBuffer sb = new StringBuffer();
		for (int x = 0; x < PASSWORD_LENGTH; x++) {

			if ((x % 3) == 0 && (x != 0)) {
				int i = (int) (Math.random() * 10);
				char d = Character.forDigit(i, 10);
				System.out.println("d=" + d);
				sb.append((char) d);
			}
			sb.append((char) ((int) (Math.random() * 26) + 97));
		}
		return (sb.toString());
}
	
	public boolean isValidUserSession(HttpServletRequest req, String key) {

		HttpSession session = req.getSession(true);

		String s = session.getAttribute(Key).toString();
		boolean result = false;

		if (s.equals(key)) {
			result = true;
		}
		System.out.println(s);
		return result;
	}
}