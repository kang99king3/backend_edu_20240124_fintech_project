package com.hk.fintech.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.hk.fintech.command.LoginCommand;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

	@GetMapping("/main")
	public String main() {
		return "main";
	}
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("loginCommand", new LoginCommand());
		return "index";
	}
	
}
