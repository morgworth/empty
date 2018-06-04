package com.assignment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dao.DAO;
import com.assignment.models.Employee;

@RestController
public class EmployeeController {
	@Autowired
	DAO dao;
	
	@GetMapping("employee")
	public String getEmployee() {
		return dao.getEmployee().toString();
	}
}
