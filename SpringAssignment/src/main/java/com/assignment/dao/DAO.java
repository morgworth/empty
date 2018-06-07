package com.assignment.dao;

import java.util.LinkedList;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.Oid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.assignment.domain.Employee;
import com.mongodb.DB;
import com.mongodb.MongoClient;




@Repository
public class DAO {
	DB db= new MongoClient().getDB("SpringAssignmentDB");
	Jongo jongo = new Jongo(db);
	MongoCollection employees = jongo.getCollection("employees");

	@Cacheable(value="employee-cache", key="#root.method.name.concat(:).concat(#param)")
	public List<Employee> getEmployeesByFirstName(String firstName) {
		MongoCursor<Employee> emps=employees.find("{name:',"+firstName+"'}").as(Employee.class);
		List<Employee> list= new LinkedList<>();
		while(emps.hasNext()) {
			list.add(emps.next());
		}
		return list;
	}

	@Cacheable(value="employee-cache", key="all-employees")
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
	
	@Cacheable(value="employee-cache", key="#root.method.name.concat(:).concat(#result.id)")
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

	@Cacheable(value="employee-cache", key="#root.method.name.concat(:).concat(#result.id)")
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
