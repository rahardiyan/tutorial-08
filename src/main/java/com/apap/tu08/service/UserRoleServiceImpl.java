package com.apap.tu08.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apap.tu08.model.UserRoleModel;
import com.apap.tu08.repository.UserRoleDb;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	@Autowired
	private UserRoleDb userDB;
	
	@Override
	public UserRoleModel addUser(UserRoleModel user) {
		String pass = encrypt(user.getPassword());
		user.setPassword(pass);
		return userDB.save(user);
	}

	@Override
	public String encrypt(String password) {
		BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		String hashPass = pe.encode(password);
		return hashPass;
	}
}