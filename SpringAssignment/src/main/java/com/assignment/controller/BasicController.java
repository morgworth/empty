package com.assignment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

	@GetMapping("/something")
	public String something() {
		return "something";
	}
}
