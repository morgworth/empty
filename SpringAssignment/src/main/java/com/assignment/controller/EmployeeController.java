package com.assignment.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.domain.Employee;
import com.assignment.service.Service;

@RestController
public class EmployeeController {
	@Autowired
	Service service;
	
	@GetMapping("/all-employees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		return new ResponseEntity<List<Employee>>(service.getAllEmployees(),HttpStatus.OK);
	}
	
	@RequestMapping(value="/get-employee/zip/{zip}")
	public ResponseEntity<List<Employee>> getEmployeesByZipCode(@PathVariable(value="zip") final String zip) {
		List<Employee> emps=service.getEmployeesByZipCode(zip);
		return new ResponseEntity<List<Employee>>(emps,HttpStatus.OK);
	}
	
	@RequestMapping(value="/get-employee/city/{city}")
	public ResponseEntity<List<Employee>> getEmployeesByCity(@PathVariable(value="city") final String city) {
		List<Employee> emps=service.getEmployeesByCity(city);
		return new ResponseEntity<List<Employee>>(emps,HttpStatus.OK);
	}
	
	@RequestMapping(value="/get-employee/id/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value="id") final String id) {
		Employee employee=service.getEmployeeById(id);
		return new ResponseEntity<Employee>(employee,HttpStatus.OK);
	}
	
	@RequestMapping(value="/add-employee", method=RequestMethod.POST, headers= {"content-type=application/json"})
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
		service.addEmployee(employee);
		return new ResponseEntity<Employee>(employee,HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/delete-employee/id/{id}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable(value="id") final String id) {
		service.deleteEmployeeById(id);
		return new ResponseEntity<String>("Employee deleted",HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value="/update-employee/",method=RequestMethod.POST,headers= {"content-type=application/json"})
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee){
		service.updateEmployee(employee);
		return new ResponseEntity<Employee>(employee,HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value="/bulk-insert", method=RequestMethod.POST,headers= {"content-type=application/json"})
	public ResponseEntity<List<Employee>> bulkInsert(@RequestBody List<Employee> toInsert){
		
		service.bulkInsert(toInsert);
		
		return new ResponseEntity<List<Employee>>(toInsert,HttpStatus.ACCEPTED);
	}
}

