package main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfig {
    @Value("${spring.mail.username}")
    private String emailAddressFrom;
    @Value("${spring.mail.password}")
    private String gmailSecretMailApplicationKey;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String mailStarttlsEnable;
    @Value("${spring.mail.properties.mail.debug}")
    private String mailDebug;
    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String transportProtocol;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(emailAddressFrom);
        mailSender.setPassword(gmailSecretMailApplicationKey);

        Properties mailProperties = mailSender.getJavaMailProperties();
        mailProperties.put("mail.transport.protocol", transportProtocol);
        mailProperties.put("mail.smtp.auth", mailSmtpAuth);
        mailProperties.put("mail.smtp.starttls.enable", mailStarttlsEnable);
        mailProperties.put("mail.debug", mailDebug);
        return mailSender;
    }
}
