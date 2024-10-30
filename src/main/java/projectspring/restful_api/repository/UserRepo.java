package projectspring.restful_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projectspring.restful_api.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    
    Optional<User> findFirstByToken(String token);
}
