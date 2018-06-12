package com.assignment.config;


import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.assignment")
@EnableCaching
public class AppConfig extends WebMvcConfigurerAdapter {

	@Bean
	public MongoCollection employees(){
		DB db= new MongoClient().getDB("SpringAssignmentDB");
		Jongo jongo = new Jongo(db);
		return jongo.getCollection("employees");
	}

	

	CacheManager cm= CacheManager.create();
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
	@Bean
	public Cache cache1() {
		return cm.getCache("employee-cache-1");
	}
	@Bean
	public Cache cache2() {
		return cm.getCache("employee-cache-2");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}