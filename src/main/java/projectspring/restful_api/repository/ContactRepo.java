package projectspring.restful_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import projectspring.restful_api.entity.Contact;
import projectspring.restful_api.entity.User;

import java.util.Optional;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String>, JpaSpecificationExecutor<Contact> {

    Optional<Contact> findFirstByUserAndId(User user, String id);

}
