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

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;




@Repository
public class DAO {
	DB db= new MongoClient().getDB("SpringAssignmentDB");
	Jongo jongo = new Jongo(db);
	MongoCollection employees = jongo.getCollection("employees");
	
	CacheManager cm = CacheManager.newInstance("src/main/resources/ehcache.xml");
	Cache cache=cm.getCache("employee-cache");
	

	public List<Employee> getEmployeesByFirstName(String firstName) {
		MongoCursor<Employee> emps=employees.find("{name:',"+firstName+"'}").as(Employee.class);
		List<Employee> list= new LinkedList<>();
		while(emps.hasNext()) {
			list.add(emps.next());
		}
		Element cacheElement=new Element("getEmployeesbyFirstName:"+firstName,list);
		cache.put(cacheElement);
		return list;
	}

	public List<Employee> getAll(){
		MongoCursor<Employee> emps=employees.find("{}").as(Employee.class);
		List<Employee> list = new LinkedList<>();
		while(emps.hasNext()) {
			list.add(emps.next());
		}
		Element cacheElement=new Element("all-employees",list);
		cache.put(cacheElement);
		return list;
	}
	
	public void deleteEmployeeById(String id) {
		employees.remove(Oid.withOid(id));
	}
	
	@Cacheable(value="employee-cache", key="#root.method.name.concat(:).concat(#result.id)")
	public Employee getEmployeeById(String id) {
		Employee employee=employees.findOne(Oid.withOid(id)).as(Employee.class);
		Element cacheElement=new Element("getEmployeeById:"+employee.getId(),employee);
		cache.put(cacheElement);
		return employee;
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
		
		Element cacheElement=new Element("updateEmployee:"+updated.getId(),updated);
		cache.put(cacheElement);
		
		return updated;
	}
}
