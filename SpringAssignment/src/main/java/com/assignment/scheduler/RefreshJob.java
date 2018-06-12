package com.assignment.scheduler;

import java.util.LinkedList;
import java.util.List;

import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.assignment.dao.DAO;
import com.assignment.domain.Employee;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class RefreshJob implements Job {

	@Autowired
	Cache cache2;
	@Autowired
	DAO dao;
	@Autowired
	MongoCollection employees;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		cache2.removeAll();
		dao.getAll();
		//have to do this since level 1 may still be populated:
		List<Employee> list = new LinkedList<>();
		MongoCursor<Employee> emps=employees.find("{}").as(Employee.class);
		while(emps.hasNext()) {
			Employee emp=emps.next();
			list.add(emp);
			cache2.put(new Element(emp.getId(),emp));
		}
		cache2.put(new Element("all-employees",list));
	}
}
