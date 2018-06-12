package com.assignment.config;


import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.assignment.scheduler.RefreshJob;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
	
	@Bean
	LoadingCache<String,Object> guavaCache(){
		//from this site:
		//https://github.com/google/guava/wiki/CachesExplained
		return CacheBuilder.newBuilder()
			       .expireAfterAccess(3, TimeUnit.MINUTES)
			       .build(
			           new CacheLoader<String, Object>() {
			             public Object load(String key) { // no checked exception
			               return "input item into cache with key: "+key;
			             }
			           });
	}
	
	@Bean
	Logger logger() {
		return LogManager.getLogger();
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	//Schedule level 2 cache to refresh over a given time interval (here every 20 minutes), used the following tutorial:
	//https://www.mkyong.com/java/quartz-2-scheduler-tutorial/
	{
		JobDetail job = JobBuilder.newJob(RefreshJob.class).withIdentity("refresh").build();
		Trigger trigger=TriggerBuilder.newTrigger().withIdentity("refreshTrigger").withSchedule(CronScheduleBuilder.cronSchedule("*/20 * * * *")).build();
		try {
			Scheduler sched = new StdSchedulerFactory().getScheduler();
			sched.start();
			sched.scheduleJob(job,trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

}