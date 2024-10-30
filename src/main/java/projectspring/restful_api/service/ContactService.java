package projectspring.restful_api.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import projectspring.restful_api.entity.Contact;
import projectspring.restful_api.entity.User;
import projectspring.restful_api.model.ContactResponse;
import projectspring.restful_api.model.CreateContactRequest;
import projectspring.restful_api.model.SearchContactRequest;
import projectspring.restful_api.model.UpdateContactRequest;
import projectspring.restful_api.repository.ContactRepo;

@Service
public class ContactService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ContactRepo contactRepo;

    @Transactional
    public ContactResponse createContact (User user, CreateContactRequest request){
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepo.save(contact);

        return toContactResponse(contact);
    }

    private ContactResponse toContactResponse(Contact contact){
        return ContactResponse.builder()
        .id(contact.getId())
        .firstName(contact.getFirstName())
        .LastName(contact.getLastName())
        .email(contact.getEmail())
        .phone(contact.getPhone())
        .build();
    }

    @Transactional(readOnly = true)
    public ContactResponse get(User user, String id){
        Contact contact = contactRepo.findFirstByUserAndId(user, id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));
        return toContactResponse(contact);
    }

    @Transactional
    public ContactResponse update(User user, UpdateContactRequest request){
        validationService.validate(request);

        Contact contact = contactRepo.findFirstByUserAndId(user, request.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());

        contactRepo.save(contact);
        return toContactResponse(contact);
    }

    @Transactional
    public void delete(User user, String contactId){
        Contact contact = contactRepo.findFirstByUserAndId(user, contactId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));
        contactRepo.delete(contact);
    }

    @Transactional(readOnly = true)
    public Page<ContactResponse> search (User user, SearchContactRequest request){
        Specification<Contact> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));
            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.or(
                    builder.like(root.get("firstName"), "%" + request.getName()+"%"),
                    builder.like(root.get("lastName"), "%" + request.getName()+"%")

                ));
            }

            if (Objects.nonNull(request.getEmail())) {
                predicates.add(builder.like(root.get("email"), "%" + request.getEmail()+"%"));
            }
            if (Objects.nonNull(request.getPhone())) {
                predicates.add(builder.like(root.get("phone"), "%" + request.getPhone()+"%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepo.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts.getContent().stream()
                .map(this::toContactResponse)
                .collect(toList());

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }   

}
