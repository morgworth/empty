package com.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dao.DAO;
import com.assignment.domain.Employee;

@RestController
public class EmployeeController {
	@Autowired
	DAO dao;
	
	@GetMapping("/allEmployees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		return new ResponseEntity<List<Employee>>(dao.getAll(),HttpStatus.OK);
	}
	
	@RequestMapping(value="/add-employee", method=RequestMethod.POST, headers= {"content-type=application/json"})
	public ResponseEntity<Employee> postEmployee(@RequestBody Employee employee) {
		dao.addEmployee();
		return new ResponseEntity<Employee>(employee,HttpStatus.CREATED);
	}
}
