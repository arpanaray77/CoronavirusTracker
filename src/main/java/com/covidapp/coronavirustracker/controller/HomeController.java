package com.covidapp.coronavirustracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.covidapp.coronavirustracker.services.CoronavirusDataService;

@Controller
public class HomeController {
	
	@Autowired
	CoronavirusDataService coronavirusDataService;
	
	@GetMapping("/")
	public String home(Model model){
		
		model.addAttribute("data",coronavirusDataService.getAllStats());
		return "homeurl";
	}

}
