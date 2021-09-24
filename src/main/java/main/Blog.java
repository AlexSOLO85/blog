package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

/**
 * The type Blog.
 */
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class Blog {
    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(final String[] args) {
        new Blog().run(args);
    }

    private void run(final String[] args) {
        SpringApplication.run(Blog.class, args);
    }
}
