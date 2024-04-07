package com.guruprasad.trainticket.snippets;

import liquibase.util.NetUtil;

import java.lang.reflect.Field;

public final class StaticCodeSnippets {

    private static boolean liquibaseStuffAlreadyRan;

    public static void setLiquibaseNetUtilsLocalHost() {
        if (liquibaseStuffAlreadyRan) {
            return;
        }

        try {
            Field field = NetUtil.class.getDeclaredField("hostName");
            field.setAccessible(true);
            field.set(null, "localhost");
            field.setAccessible(false);

            liquibaseStuffAlreadyRan = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
