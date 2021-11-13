package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class Blog {
    public static void main(final String[] args) {
        new Blog().run(args);
    }

    private void run(final String[] args) {
        SpringApplication.run(Blog.class, args);
    }
}
