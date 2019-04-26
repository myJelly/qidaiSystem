package com.example.qidai.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.qidai.model.UserBean;
import com.example.qidai.service.LoginClservice;

import net.sf.json.JSONObject;



@Controller
@RequestMapping("/qidai/")
public class LoginController {
	@Autowired  //与service层进行交互
	private LoginClservice loginClService;
	
	
	@RequestMapping("user")
	public String getLoginCl(){
			return "wel";
		
	}
	@RequestMapping("register")
	public String getRegister(@RequestParam("name") String name,
			@RequestParam("password") String password){
		String register = loginClService.register(name, password);
		if(register.equals("success")) {
			return "login_ajax";
		}else {
			return "register";
		}
		
		
	}
	@RequestMapping("user_ajax")
	@ResponseBody
	public String getLgoinInfo(HttpServletRequest  request,@RequestParam("name") String name,
			@RequestParam("password") String password) {
		String b =loginClService.chick(name, password);//调用service层的方法
				
		if(b.equals("success")){		
			return "OK";
		}else {
			return "ERROR";
		}
		
	}
	

}
