package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The type Init response.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Component
public class InitResponse {
    /**
     * The Title.
     */
    @Value("${blog.title}")
    private String title;
    /**
     * The Subtitle.
     */
    @Value("${blog.subtitle}")
    private String subtitle;
    /**
     * The Phone.
     */
    @Value("${blog.phone}")
    private String phone;
    /**
     * The Email.
     */
    @Value("${blog.email}")
    private String email;
    /**
     * The Copyright.
     */
    @Value("${blog.copyright}")
    private String copyright;
    /**
     * The Copyright from.
     */
    @Value("${blog.copyrightFrom}")
    private String copyrightFrom;
}
