package com.assignment.dao;

import org.springframework.stereotype.Component;

import com.assignment.models.Employee;

@Component
public class DAO {
	
	public Employee getEmployee() {
		return new Employee();
	}
	
}
