package com.assignment.dao;

import java.util.LinkedList;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.Oid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.assignment.domain.Employee;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/*
 * For reading in cache before DB, used code snippets from this video:
 * https://www.youtube.com/watch?v=diJr2t4KmVw
 */


@Repository
public class DAO {
	DB db= new MongoClient().getDB("SpringAssignmentDB");
	Jongo jongo = new Jongo(db);
	
	@Autowired
	MongoCollection employees;
	@Autowired
	Cache cache1;
	@Autowired
	Cache cache2;
	
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
		List<Employee> list= new LinkedList<>();
		Element cacheElement=cache1.get("getEmployeesbyFirstName:"+firstName);
		if(cacheElement==null) {
			System.out.println("Cache Level 1 miss");
			cacheElement=cache2.get("getEmployeesbyFirstName:"+firstName);
			if(cacheElement==null) {
				System.out.println("Cache Level 2 miss");
				MongoCursor<Employee> emps=employees.find("{name:',"+firstName+"'}").as(Employee.class);
				while(emps.hasNext()) {
					list.add(emps.next());
				}
				cache1.put(new Element("getEmployeesbyFirstName:"+firstName,list));
				cache2.put(new Element("getEmployeesbyFirstName:"+firstName,list));
			} else {
				System.out.println("Cache Level 2 hit");
				cache1.put(cacheElement);
				list=(List<Employee>) cacheElement.getObjectValue();
			}
		} else {
			System.out.println("Cache Level 1 hit");
			list=(List<Employee>) cacheElement.getObjectValue();
		}
		return list;
	}

	public List<Employee> getAll(){
		
		List<Employee> list = new LinkedList<>();
		Element cacheElement=cache1.get("all-employees");
		if(cacheElement==null) {
			System.out.println("Cache Level 1 miss");
			cacheElement=cache2.get("all-employees");
			if(cacheElement==null) {
				System.out.println("Cache Level 2 miss");
				MongoCursor<Employee> emps=employees.find("{}").as(Employee.class);
				while(emps.hasNext()) {
					Employee emp=emps.next();
					list.add(emp);
					cache1.put(new Element(emp.getId(),emp));
					cache2.put(new Element(emp.getId(),emp));
				}
				cache1.put(new Element("all-employees",list));
				cache2.put(new Element("all-employees",list));
			} else {
				System.out.println("Cache Level 2 hit");
				cache1.put(cacheElement);
				list = (List<Employee>) cacheElement.getObjectValue();
			}
		} else {
			System.out.println("Cache Level 1 hit");
			list=(List<Employee>) cacheElement.getObjectValue();
		}
		
		return list;
	}

	public void deleteEmployeeById(String id) {
		cache1.remove(id);
		cache2.remove(id);
	}

	public Employee getEmployeeById(String id) {

		Employee emp;
		
		Element cacheElement=cache1.get(id);
		if(cacheElement==null) {
			System.out.println("Cache Level 1 miss");
			cacheElement=cache2.get(id);
			if(cacheElement==null) {
				System.out.println("Cache Level 2 miss");

				emp=employees.findOne(Oid.withOid(id)).as(Employee.class);
				cache1.put(new Element(emp.getId(),emp));
				cache2.put(new Element(emp.getId(),emp));
			} else {
				System.out.println("Cache Level 2 hit");
				emp=(Employee) cacheElement.getObjectValue();
				cache1.put(new Element(emp.getId(),emp));
			}	
		} else {
			System.out.println("Cache Level 1 hit");
			emp=(Employee) cacheElement.getObjectValue();
		}
		return emp;
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
		cache1.put(cacheElement);
		cache2.put(cacheElement);
	}

	public Employee updateEmployee(Employee updated) {
		//		employees.update(Oid.withOid(updated.getId())).with("{$set: {"
		//				+ "firstName:'"+updated.getFirstName()+"',"
		//				+ "lastName:'"+updated.getLastName()+"',"
		//				+ "age:"+updated.getAge()+","
		//				+ "dept:'"+updated.getDept()+"',"
		//				+ "addresses:'"+updated.getAddresses()+"'}}"
		//				);

		Element cacheElement=new Element("updateEmployee:"+updated.getId(),updated);
		cache1.put(cacheElement);
		cache2.put(cacheElement);

		return updated;
	}
}
