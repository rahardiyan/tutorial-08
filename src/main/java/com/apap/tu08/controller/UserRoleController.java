package com.apap.tu08.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tu08.model.UserRoleModel;
import com.apap.tu08.repository.UserRoleDb;
import com.apap.tu08.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;

	
	@Autowired
	private UserRoleDb userDB;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user) {
		userService.addUser(user);
		return "home";
	}
	
	@RequestMapping(value = "/updatePass", method = RequestMethod.GET)
	private String updatePassUser(@ModelAttribute UserRoleModel user, Model model) {
		return "updatePassword";
	}
	
	public boolean passValid(String pass) {
	      String pattern = "(?=.*[0-9]).{8,}";
	      if(pass.matches(pattern)) {
	    	  return true;
	      }else {
	    	  return false;
	      }
	}
	
	private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
	
	@RequestMapping(value = "/updatePassSubmit", method = RequestMethod.POST)
	private String updatePassSubmit(@RequestParam(value = "username") String username,
			@RequestParam(value = "oldPass") String oldPass,
			@RequestParam(value = "newPass") String newPass,
			@RequestParam(value = "newPassC") String newPassC,
			@ModelAttribute UserRoleModel user,
			Model model) {
		user = userDB.findByUsername(this.getPrincipal());		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(this.passValid(oldPass) == true && this.passValid(newPass) == true && this.passValid(newPassC) == true) {
			if(encoder.matches(oldPass, user.getPassword())) {
				if(newPass.equals(newPassC)) {
					user.setPassword(encoder.encode(newPass));
					userDB.save(user);
					return "update";
				}
			}
		}
		model.addAttribute("error", true);
		return "updatepassword2";
	}
	
	
}