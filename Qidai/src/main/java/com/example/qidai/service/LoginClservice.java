package com.example.qidai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.qidai.dao.Userdao;
import com.example.qidai.model.UserBean;

@Service
public class LoginClservice {

	@Autowired
	private Userdao userdao;// 与dao层进行交互
	public String chick(String username, String userpassword) {
		UserBean b = userdao.findByNameAndPassword(username, userpassword);
		if (b == null) {
			return "error";

		}
		String aString = b.getName();
		String bString = b.getPassword();
		return "success";


	}
	public String register(String username, String userpassword) {
		UserBean user = new UserBean();
		user.setName(username);
		user.setPassword(userpassword);
		System.out.println(user.getName());
		userdao.save(user);
		return "success";
	}

}
