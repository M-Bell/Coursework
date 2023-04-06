package com.shyiko.coursework.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class HibernateSessionConfig {

//    @Bean
//    public SessionFactory sessionFactory() {
//        try {
//            return new Configuration().configure().buildSessionFactory();
//        } catch (Exception e) {
//            throw new RuntimeException("Error building SessionFactory", e);
//        }
//    }

    @Bean
    public EntityManager entityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.shyiko.coursework");
        return factory.createEntityManager();
    }

    @Bean
    public EntityTransaction transaction() {
        return entityManager().getTransaction();
    }

}
