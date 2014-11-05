package com.thermometer.utility;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class JavaLog {
	static public Logger log4j = Logger.getLogger(JavaLog.class);
	static {
		BasicConfigurator.configure();
		PropertyConfigurator.configure("classpath:/log4j.properties");
	}
	
}
