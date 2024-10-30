package projectspring.restful_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projectspring.restful_api.entity.Address;
import projectspring.restful_api.entity.Contact;

@Repository
public interface AddressRepo extends JpaRepository<Address, String> {

    Optional<Address> findFirstByContactAndId(Contact contact, String id);

    List<Address> findAllByContactId(String contactId);

}
