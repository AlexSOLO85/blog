package main.repository;

import main.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User getUserByEmail(String email);
    User getUserByCode(String code);
    User getUserByName(String name);
    Optional<User> findByEmail(String email);
}
