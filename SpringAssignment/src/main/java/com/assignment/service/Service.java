package com.assignment.service;

import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assignment.dao.DAO;
import com.assignment.domain.Address;
import com.assignment.domain.Employee;

@Component
public class Service {
	@Autowired
	DAO dao;
	
	public List<Employee> getAllEmployees(){
		return dao.getAll();
	}

	public Employee getEmployeeById(String id) {
		return dao.getEmployeeById(id);
	}

	public void addEmployee(Employee employee) {
		dao.addEmployee(employee);
	}
	
	public void updateEmployee(Employee employee) {
		dao.updateEmployee(employee);
	}

	public void deleteEmployeeById(String id) {
		dao.deleteEmployeeById(id);
		
	}

	public List<Employee> getEmployeesByCity(String city) {
		List<Employee> emps=getAllEmployees();
		ListIterator<Employee> itr=emps.listIterator();
		while(itr.hasNext()) {
			Boolean inCity=false;
			Employee toCheck=itr.next();
			for(Address a:toCheck.getAddresses()) {
				if(a.getCity().equals(city)) {
					inCity=true;
				}
			}
			if(!inCity) {
				itr.remove();
			}
		}
		return emps;
	}
	
	public List<Employee> getEmployeesByZipCode(String zip) {
		List<Employee> emps=getAllEmployees();
		ListIterator<Employee> itr=emps.listIterator();
		while(itr.hasNext()) {
			Boolean inCity=false;
			Employee toCheck=itr.next();
			for(Address a:toCheck.getAddresses()) {
				if(a.getZipcode().equals(zip)) {
					inCity=true;
				}
			}
			if(!inCity) {
				itr.remove();
			}
		}
		return emps;
	}
}
