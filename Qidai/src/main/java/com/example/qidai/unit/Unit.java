package com.example.qidai.unit;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import net.sf.json.JSONObject;

public class Unit {
	/*
	 * 获取地址 参数类型 : String (例如,0.0.0.0) 返回类型 : String (例如 北京市)
	 */
	public static String getAddress(String ip) {
		String address = "";
		String url = "http://www.882667.com/ip_" + ip + ".html";
		try {
			Document doc = Jsoup.connect(url).get();
			// 得到html的所有东西
			Elements content = doc.getElementsByClass("shenlansezi");
			String s = String.valueOf(content.get(2));

			String regex = "([\u4e00-\u9fa5]+)";
			String str = "";
			Matcher matcher = Pattern.compile(regex).matcher(s);
			while (matcher.find()) {
				str += matcher.group(0);
			}
			address = str.substring(0, str.indexOf("市") + 1);
			return address;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "北京市";
		}
	}

	/*
	 * 获取星期 参数类型 : Date 返回类型 : String (例如 周日)
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	

	/*
	 * 获取ip 参数类型 : HttpServletRequest 返回类型 : String
	 */
	public static String getIp(HttpServletRequest request) throws Exception {
		// 获取客户端ip开始
		if (request == null) {
			throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
		}
		String ipString = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
			ipString = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
			ipString = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
			ipString = request.getRemoteAddr();
		}

		// 多个路由时，取第一个非unknown的ip
		final String[] arr = ipString.split(",");
		for (final String str : arr) {
			if (!"unknown".equalsIgnoreCase(str)) {
				ipString = str;
				break;
			}
		}
		if(ipString.indexOf(":")!=-1) {
			ipString = ipString.substring(ipString.indexOf(":")+1,ipString.length());
		}
		return ipString;
	}

}
