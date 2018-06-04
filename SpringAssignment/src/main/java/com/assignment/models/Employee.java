package com.assignment.models;

import org.springframework.stereotype.Component;

@Component
public class Employee {
	private String firstName="John";
	private String secondName="Doe";
	private int age=35;
	private String dept="Sales";
	
	public Employee() {
		
	}
	
	@Override
	public String toString() {
		return "Employee [firstName=" + firstName + ", secondName=" + secondName + ", age=" + age + ", dept=" + dept
				+ "]";
	}

	public Employee(String firstName, String secondName, int age, String dept) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.age = age;
		this.dept = dept;
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
