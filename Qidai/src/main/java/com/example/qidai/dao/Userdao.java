package com.example.qidai.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.qidai.model.UserBean;

@Repository
public interface Userdao extends JpaRepository<UserBean, Integer> {
 
	
	UserBean findByNameAndPassword(String name,String password);
    
    //这是使用的jpa，程序运行时，会自动识别findBy后面的字段部分。
 
}
