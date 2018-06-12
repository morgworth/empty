package com.assignment.service;

import java.util.List;

import com.assignment.domain.Employee;

public interface Service {
	public List<Employee> getAllEmployees();
	public Employee getEmployeeById(String id);
	public void addEmployee(Employee employee);
	public void updateEmployee(Employee employee);
	public void deleteEmployeeById(String id);
	public List<Employee> getEmployeesByCity(String city);
	public List<Employee> getEmployeesByZipCode(String zip);
	public void bulkInsert(List<Employee> toInsert);
}
