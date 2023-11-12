package com.github.rybalkin_an.app;

import com.github.rybalkin_an.app.testcontainers.ContainersConfig;
import org.springframework.boot.SpringApplication;

public class TestApplication {

        public static void main(String[] args) {
            SpringApplication
                    .from(Application::main)
                    .with(ContainersConfig.class)
                    .run(args);
        }

}
