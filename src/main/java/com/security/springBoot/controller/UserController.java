package com.security.springBoot.controller;
import com.security.springBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.security.springBoot.models.Role;
import com.security.springBoot.models.User;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

	@Qualifier("userDetailsServiceImpl")
	@Autowired
	final private UserDetailsService service;
	@Autowired
	private final UserService userService;

	public UserController(@Qualifier("userDetailsServiceImpl") UserDetailsService service, UserService userService) {
		this.service = service;
		this.userService = userService;
	}


	@GetMapping("/user")
	public String userPage(Principal principal,  Model model){
		model.addAttribute("user", service.loadUserByUsername(principal.getName()));
		return "user";
	}

	@GetMapping("/admin")
	public String adminPage(Model model){
		model.addAttribute("user", new User());
		List<Role> roles = new ArrayList<>();
		roles.add(new Role("ROLE_USER"));
		roles.add(new Role("ROLE_ADMIN"));
		model.addAttribute("roleList", roles);
		model.addAttribute("userList", userService.userList());
		return "admin";
	}

	@PostMapping("/admin")
	public String create(@ModelAttribute("user") User user){
		userService.addUser(user);
		return "redirect:/admin";
	}

	@GetMapping(value = "/logout")
	public String logout(){
		return "logout";
	}

	@PostMapping("logout")
	public String logoutURL(){
		return "redirect:/logout";
	}

	@GetMapping("/admin/remove/{id}")
	public String removeUser(@PathVariable("id") long id, Model model){
		model.addAttribute("user", userService.getUserById(id));

		return "delete";
	}

	@DeleteMapping(value = "/admin/remove/{id}")
	public String remove(@PathVariable("id") long id){
		userService.deleteUser(id);
		return "redirect:/admin";
	}

	@GetMapping(value = "/admin/{id}/edit")
	public String edit(@PathVariable("id") long id, Model model){
		model.addAttribute("user", userService.getUserById(id));
		List<Role> roles = new ArrayList<>();
		roles.add(new Role("ROLE_USER"));
		roles.add(new Role("ROLE_ADMIN"));
		model.addAttribute("roleList", roles);
		model.addAttribute("userList", userService.userList());
		return "edit";
	}

	@PutMapping("/admin/{id}/edit")
	public String update(@ModelAttribute("user") User user, @PathVariable("id") long id){
		userService.updateUser(user);
		return "redirect:/admin";
	}

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
