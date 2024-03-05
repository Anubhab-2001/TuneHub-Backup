package com.kodnest.tunehub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kodnest.tunehub.entity.Song;
import com.kodnest.tunehub.entity.User;
import com.kodnest.tunehub.service.SongService;
import com.kodnest.tunehub.serviceimpl.UserServiceimpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserServiceimpl serviceimpl;
	
	@Autowired
	SongService songService;

	@PostMapping("/register")
	public String addUser(@ModelAttribute User user) {
		
		// email taken from the registration form
		String email = user.getEmail();
		// checking if email as entered in  registration from is present in DB or not.
	boolean status = serviceimpl.emailExists(email);
	
		if(status==false) {
		serviceimpl.addUser(user);
		System.out.println("User added");
		}
		else {
			System.out.println("User already exists");
		}
		return "login";

	}
	@PostMapping("/validate")
	public String validate(@RequestParam("email") String email,@RequestParam("password")String password, HttpSession session,Model model) {
		
		if(serviceimpl.validateUser(email,password)==true){
			
			String role = serviceimpl.getRole(email);
			session.setAttribute("email", email);
			
			if(role.equals("admin")) {
				return "adminhome";
			}
		else {
			User user = serviceimpl.getUser(email);
			boolean userstatus = user.isIspremium();
			//logic
		 List<Song> fetchAllSongs = songService.fetchAllSong();
			model.addAttribute("songs", fetchAllSongs);
			model.addAttribute("ispremium", userstatus);
			return "customerhome";
		}
		
	}
		else {
			return "login";
		}

	
}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
	
}
