package com.crud_library.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class CrudConfig {
	
	@Bean
	public MessageSource crudMessageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		
		ms.setBasename("crudMessages");
		ms.setDefaultEncoding("UTF-8");
		
		return ms;
	}
	

}
