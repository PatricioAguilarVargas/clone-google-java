package com.cursojava.back.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cursojava.back.entities.WebPage;
import com.cursojava.back.services.ScheduleService;
import com.cursojava.back.services.SearchService;
import com.cursojava.back.services.SpiderService;

@RestController
public class SearchController {

	@Autowired
	private SearchService service;
	
	@Autowired
	private SpiderService spider;
	
	@Autowired
	private ScheduleService schedule;
	
	@CrossOrigin("*")
	@RequestMapping(value = "api/search", method = RequestMethod.GET)
	public List<WebPage> search(@RequestParam Map<String, String> params) {
		String query = params.get("query");
		return service.search(query);
	}
	
	@RequestMapping(value = "api/test", method = RequestMethod.GET)
	public void test(@RequestParam Map<String, String> params) {
		schedule.scheduleIndexWebPages();
	}
}
