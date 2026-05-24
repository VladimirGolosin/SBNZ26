package com.ftn.sbnz;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServiceApplication {

    @Bean
    public KieContainer kieContainer() {
        return org.kie.api.KieServices.Factory.get()
            .newKieContainer(
                org.kie.api.KieServices.Factory.get()
                    .newReleaseId("com.ftn.sbnz", "kjar", "0.0.1-SNAPSHOT")
            );
    }

    public static void main(String[] args) {


        
    }
}
