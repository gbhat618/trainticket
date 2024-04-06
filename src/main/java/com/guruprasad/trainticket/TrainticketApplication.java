package com.guruprasad.trainticket;

import liquibase.util.NetUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;

@SpringBootApplication
public class TrainticketApplication {

	private static void setLiquibaseNetUtilsLocalHost() throws NoSuchFieldException, IllegalAccessException {
		Field field = NetUtil.class.getDeclaredField("hostName");
		field.setAccessible(true);
		field.set(null, "localhost");
		field.setAccessible(false);
	}

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
		setLiquibaseNetUtilsLocalHost();

		SpringApplication.run(TrainticketApplication.class, args);
	}

}
