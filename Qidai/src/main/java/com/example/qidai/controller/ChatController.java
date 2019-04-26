package com.example.qidai.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.qidai.unit.GetWeatherUnit;
import com.example.qidai.unit.Unit;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/chat/")
public class ChatController {
	@RequestMapping(value = "chatAI", produces = "text/html;charset=UTF-8;")
	@ResponseBody
	public String getchat(@RequestParam("content") String content, HttpServletRequest request) throws Exception {
		String answer = "";
//-----服务器
//		String s1 = "/usr/bin/python";
//		String s2 = "/var/qidai/chatAI.py";
//		String encode="utf-8";
		
//本地
		String s1 = "D:\\liuyang\\NLP\\Kuakua\\venv2\\Scripts\\python";
		String s2 = "D:\\chatAI.py";
		String encode = "gb2312";

		// 传入python脚本的参数
		String[] arguments = new String[] { s1, s2, content };
		Process process = Runtime.getRuntime().exec(arguments);
		BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), encode));
		String line;
		// python里的运行结果，想传给java，就需要用这种readline的形式了。
		while ((line = in.readLine()) != null) {
			answer += line;
		}
		in.close();
		return answer;
	}
}
