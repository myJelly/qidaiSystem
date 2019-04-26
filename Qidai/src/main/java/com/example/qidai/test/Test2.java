package com.example.qidai.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
public class Test2 {
	   public void java2python() throws Exception{
//	      python解析器的路径
	        String s1 = "D:\\liuyang\\NLP\\Kuakua\\venv2\\Scripts\\python";
//	      要执行的python脚本
	        String s2 = "D:\\chatAI.py";
//	      传入python脚本的参数
	        String s3 = "学习";
	        String[] arguments = new String[] {s1, s2, s3};
	        Process process = Runtime.getRuntime().exec(arguments);
	        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"gb2312"));
	        String line;
	        String result = "";
//	        python里的运行结果，想传给java，就需要用这种readline的形式了。
	        while ((line = in.readLine()) != null) {
	            result += line;
	            //System.out.println(line);
	        }
	        in.close();
	        System.out.println(result);
	    }
	    public static void main(String[] args) throws Exception {
	    	Test2 t = new Test2();
	        t.java2python();
	    }
}