package main.repository;

import main.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query(
            value =
                    "SELECT * "
                            + "FROM users u "
                            + "WHERE u.email = ?",
            nativeQuery = true)
    User getUserByEmail(String email);

    @Query(
            value =
                    "SELECT * "
                            + "FROM users u "
                            + "WHERE u.code = ?",
            nativeQuery = true)
    User getUserByCode(String code);

    @Query(
            value =
                    "SELECT * "
                            + "FROM users u "
                            + "WHERE u.name = ?",
            nativeQuery = true)
    User getUserByName(String name);
    Optional<User> findByEmail(String email);
}
