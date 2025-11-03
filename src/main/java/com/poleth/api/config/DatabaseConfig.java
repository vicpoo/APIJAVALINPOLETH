// DatabaseConfig.java
package com.poleth.api.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseConfig {

    private static EntityManagerFactory emf;

    public static void initialize() {
        emf = Persistence.createEntityManagerFactory("demo-pu");
    }

    public static EntityManager createEntityManager() {
        if (emf == null) {
            initialize();
        }
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}