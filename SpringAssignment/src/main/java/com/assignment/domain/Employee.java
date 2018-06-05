package com.assignment.domain;

import java.util.LinkedList;
import java.util.List;

import org.jongo.marshall.jackson.oid.MongoId;
import org.springframework.stereotype.Component;

@Component
public class Employee {
	private String firstName="Jane";
	private String secondName="Doe";
	private int age=35;
	private String dept="Sales";
	@MongoId
	private String id;
	private List<Address> addresses=new LinkedList();
	
	public Employee(String firstName, String secondName, int age, String dept, String id) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.age = age;
		this.dept = dept;
		this.id=id;
	}
	
	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Employee() {
		
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
}
