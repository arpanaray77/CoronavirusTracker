package com.covidapp.coronavirustracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.covidapp.coronavirustracker.models.LocationData;
import org.springframework.web.bind.annotation.GetMapping;
import com.covidapp.coronavirustracker.services.CoronavirusDataService;

@Controller
public class HomeController {
	
	@Autowired
	CoronavirusDataService coronavirusDataService;
	
	@GetMapping("/")
	public String home(Model model){
		
		List<LocationData> allStats = coronavirusDataService.getAllStats();
		int totalReportedCases= allStats.stream().mapToInt(stat-> stat.getLatestTotalCases()).sum();
		int totalNewCases= allStats.stream().mapToInt(stat-> stat.getDiffrenceFromPrevDay()).sum();
		
		model.addAttribute("data",coronavirusDataService.getAllStats());
		model.addAttribute("totalReportedCases", totalReportedCases);
		model.addAttribute("totalNewCases", totalNewCases);
		return "homeurl";
	}

}
