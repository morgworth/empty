package com.assignment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dao.DAO;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class EmployeeController {
	@Autowired
	DAO dao;
	
	@GetMapping("employee")
	public String getEmployee() {
		String serial;
		ObjectMapper mapper=new ObjectMapper();
		try{
			serial = mapper.writeValueAsString(dao.getEmployee());
		} catch (Exception e) {
			serial=dao.getEmployee().toString();
		}
		
		return serial;
	}
}
