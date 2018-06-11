package com.assignment.dao;

import java.util.LinkedList;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.Oid;
import org.springframework.stereotype.Repository;

import com.assignment.domain.Employee;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.CacheWriterConfiguration;




@Repository
public class DAO {
	DB db= new MongoClient().getDB("SpringAssignmentDB");
	Jongo jongo = new Jongo(db);
	MongoCollection employees = jongo.getCollection("employees");

	CacheManager cm = CacheManager.create();
	Cache cache=cm.getCache("employee-cache");
	{
		//ehcache.xml couldn't be found earlier, set up within java instead here. 
		//from this site:
		//https://documentation.softwareag.com/onlinehelp/Rohan/terracotta_435/bigmemory-go/webhelp/index.html#page/bigmemory-go-webhelp/co-write_configuring_cache_writer.html
		//		cache = new Cache( 
		//				new CacheConfiguration("employee-cache", 1000) 
		//				.cacheWriter(new CacheWriterConfiguration() 
		//				.writeMode(CacheWriterConfiguration.WriteMode.WRITE_BEHIND) 
		//				.maxWriteDelay(8) 
		//				.rateLimitPerSecond(5) 
		//				.writeCoalescing(true) 
		//				.writeBatching(true) 
		//				.writeBatchSize(20) 
		//				.retryAttempts(2) 
		//				.retryAttemptDelaySeconds(2) 
		//				.cacheWriterFactory(new CacheWriterConfiguration.CacheWriterFactoryConfiguration() 
		//				   .className("com.assignment.cache.WriterFactory") 
		//				   .properties("just.some.property=test; another.property=test2") 
		//				   .propertySeparator(";"))));
		//		cm.addCache(cache);
	}


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
			Employee emp=emps.next();
			list.add(emp);
			Element cacheElement= new Element(emp.getId(),emp);
			cache.put(cacheElement);
		}

		Element cacheElementList=new Element("all-employees",list);
		cache.put(cacheElementList);

		return list;
	}

	public void deleteEmployeeById(String id) {
		cache.remove(id);
	}

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

		//		employees.insert(employee);

		Element cacheElement=new Element("addEmployee:"+employee.toString(),employee);
		cache.put(cacheElement);
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
