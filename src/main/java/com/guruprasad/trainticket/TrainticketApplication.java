package com.guruprasad.trainticket;

import liquibase.changelog.ChangeLogHistoryServiceFactory;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.generator.internal.CurrentTimestampGeneration;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.Collections;

@SpringBootApplication
@ImportRuntimeHints(TrainticketApplication.ForBuildingNativeImage.class)
public class TrainticketApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainticketApplication.class, args);
	}

    /*
    There were errors seen during startup, after checking online, resolved the errors by adding the below hint registrations.
    All hints are necessary.
    * */
	static class ForBuildingNativeImage implements RuntimeHintsRegistrar {

		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

            /* For liquibase
            references:
            1. https://github.com/oracle/graalvm-reachability-metadata/issues/239#issuecomment-1446033162
            2. https://github.com/spring-projects/spring-boot/issues/38941#issuecomment-1871816760
            * */
			hints.resources().registerPattern("db/*.xml");
			hints.resources().registerPattern("db/changelog/*.xml");
			hints.reflection().registerType(ChangeLogHistoryServiceFactory.class, (type) ->
				type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));

            /* For Hibernate
            references:
            1. https://stackoverflow.com/a/77791515/6241712
            * */
            try {
                Constructor<CurrentTimestampGeneration> constructor1 = ReflectionUtils.accessibleConstructor(CurrentTimestampGeneration.class,
                    CurrentTimestamp.class,
                    Member.class,
                    GeneratorCreationContext.class
                );
                Constructor<CurrentTimestampGeneration> constructor2 = ReflectionUtils.accessibleConstructor(CurrentTimestampGeneration.class,
                    CreationTimestamp.class,
                    Member.class,
                    GeneratorCreationContext.class
                );
                Constructor<CurrentTimestampGeneration> constructor3 = ReflectionUtils.accessibleConstructor(CurrentTimestampGeneration.class,
                    UpdateTimestamp.class,
                    Member.class,
                    GeneratorCreationContext.class
                );

                hints.reflection().registerConstructor(constructor1, ExecutableMode.INVOKE);
                hints.reflection().registerConstructor(constructor2, ExecutableMode.INVOKE);
                hints.reflection().registerConstructor(constructor3, ExecutableMode.INVOKE);
            } catch (NoSuchMethodException nme) {
                throw new RuntimeException(nme);
            }
		}
	}
}
