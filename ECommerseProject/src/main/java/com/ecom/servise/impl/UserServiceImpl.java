package com.ecom.servise.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;

@Service
public class UserServiceImpl implements UserService {
@Autowired
	private UserRepository userRepository;
@Autowired
 private PasswordEncoder passwordEncoder;
 //= new BCryptPasswordEncoder();

	@Override
	public UserDtls saveUser(UserDtls user) {
		// TODO Auto-generated method stub
		user.setRole("ROLE_USER");
		String encPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encPassword);
		UserDtls saveuser= userRepository.save(user);	
		return saveuser;
	}

	


}
