package main.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Component
public class UserDTO {
    public final User userToEntity(final boolean isModerator,
                                   final LocalDateTime now,
                                   final String name,
                                   final String email,
                                   final String hashedPassword) {
        User user = new User();
        user.setIsModerator(isModerator);
        user.setRegTime(now);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        return user;
    }
}
