package com.assignment.dao;

import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.Oid;
import org.springframework.stereotype.Component;

import com.assignment.domain.Address;
import com.assignment.domain.Employee;
import com.mongodb.DB;
import com.mongodb.MongoClient;




@Component
public class DAO {
	DB db= new MongoClient().getDB("SpringAssignmentDB");
	Jongo jongo = new Jongo(db);
	MongoCollection employees = jongo.getCollection("employees");

	
	public List<Employee> getEmployeesByFirstName(String firstName) {
		MongoCursor<Employee> emps=employees.find("{name:',"+firstName+"'}").as(Employee.class);
		List<Employee> list= new LinkedList<>();
		while(emps.hasNext()) {
			list.add(emps.next());
		}
		return list;
	}

	public List<Employee> getAll(){
		MongoCursor<Employee> emps=employees.find("{}").as(Employee.class);
		List<Employee> list = new LinkedList<>();
		while(emps.hasNext()) {
			list.add(emps.next());
		}
		return list;
	}
	
	public void deleteEmployeeById(String id) {
		employees.remove(Oid.withOid(id));
	}
	
	public Employee getEmployeeById(String id) {
		return employees.findOne(Oid.withOid(id)).as(Employee.class);
	}
	
	public void addEmployee(Employee employee) {
//		Employee e = new Employee();
//		Address a= new Address();
//		a.setCity("San Jose");
//		a.setState("California");
//		a.setStreet("Somewhere");
//		a.setZipcode("95110");
//		e.getAddresses().add(a);
//		a=new Address();
//		a.setCity("Columbus");
//		a.setState("Ohio");
//		a.setStreet("200 W Norwich Ave");
//		a.setZipcode("43201");
//		e.getAddresses().add(a);
//		e.setFirstName("Morgan");
//		e.setLastName("Worthington");
//		e.setAge(28);
//		e.setDept("Engineering");
//		employees.insert(e);
		employees.insert(employee);
	}

	public Employee updateEmployee(Employee updated) {
		employees.update(Oid.withOid(updated.getId())).with("{$set: {"
				+ "firstName:'"+updated.getFirstName()+"',"
				+ "lastName:'"+updated.getLastName()+"',"
				+ "age:"+updated.getAge()+","
				+ "dept:'"+updated.getDept()+"',"
				+ "addresses:'"+updated.getAddresses()+"'}}"
				);
		return updated;
	}
}
