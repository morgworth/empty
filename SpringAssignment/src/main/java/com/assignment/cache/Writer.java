package com.assignment.cache;

import java.util.Collection;

import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import net.sf.ehcache.CacheEntry;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.writebehind.operations.SingleOperationType;

/*
 * http://www.ehcache.org/documentation/2.7/get-started/getting-started.html
 * http://www.ehcache.org/documentation/2.7/apis/write-through-caching#using-a-combined-read-through-and-write-behind-cache
 */

public class Writer implements CacheWriter {

	DB db= new MongoClient().getDB("SpringAssignmentDB");
	Jongo jongo = new Jongo(db);
	MongoCollection employees = jongo.getCollection("employees");

	public CacheWriter clone(Ehcache cache) throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	public void init() { }
	
	public void dispose() throws CacheException { }

	public void write(Element element) throws CacheException
	{
		employees.save(element.getObjectValue());  

	}

	public void writeAll(Collection<Element> elements) throws CacheException
	{
		for (Element element : elements) {
			write(element);
		}
	}

	public void delete(CacheEntry entry) throws CacheException {
		employees.remove((ObjectId) entry.getKey());
	}

	public void deleteAll(Collection<CacheEntry> entries) throws CacheException
	{
		for (CacheEntry entry : entries) {
			delete(entry);
		}
	}
	
	@Override
	public void throwAway(Element element, SingleOperationType operationType, RuntimeException e) {
		// TODO Auto-generated method stub

	}


}
