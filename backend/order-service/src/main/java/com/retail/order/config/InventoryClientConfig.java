package com.retail.order.config;
import feign.Logger;
import org.springframework.context.annotation.Bean;
public class InventoryClientConfig { @Bean Logger.Level feignLoggerLevel(){return Logger.Level.BASIC;} }
