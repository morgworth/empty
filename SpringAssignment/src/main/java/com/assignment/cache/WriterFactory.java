package com.assignment.cache;

import java.util.Properties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.CacheWriterFactory;

public class WriterFactory extends CacheWriterFactory {

	@Override
	public CacheWriter createCacheWriter(Ehcache cache, Properties properties) {
		return new Writer();
	}

}
