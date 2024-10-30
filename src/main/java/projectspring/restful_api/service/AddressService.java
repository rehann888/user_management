package projectspring.restful_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import projectspring.restful_api.entity.Address;
import projectspring.restful_api.entity.Contact;
import projectspring.restful_api.entity.User;
import projectspring.restful_api.model.AddressResponse;
import projectspring.restful_api.model.CreateAddressRequest;
import projectspring.restful_api.model.UpdateAddressRequest;
import projectspring.restful_api.repository.AddressRepo;
import projectspring.restful_api.repository.ContactRepo;

@Service
public class AddressService {

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request){
        validationService.validate(request);

        Contact contact = contactRepo.findFirstByUserAndId(user, request.getContactId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

            Address address = new Address();
            address.setId(UUID.randomUUID().toString());
            address.setContact(contact);
            address.setStreet(request.getStreet());
            address.setCity(request.getCity());
            address.setProvince(request.getProvince());
            address.setCountry(request.getCountry());
            address.setPostalCode(request.getPostalCode());
    
            addressRepo.save(address);
    
            return toAddressResponse(address);
        }
    
    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
            .id(address.getId())
            .street(address.getStreet())
            .city(address.getCity())
            .province(address.getProvince())
            .country(address.getCountry())
            .postalCode(address.getPostalCode())
            .build();
    }

    @Transactional(readOnly = true)
    public AddressResponse get(User user, String contactId, String addressId){
        Contact contact = contactRepo.findFirstByUserAndId(user, contactId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));
        
        Address address = addressRepo.findFirstByContactAndId(contact, addressId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));
        return toAddressResponse(address);

    }

    @Transactional
    public AddressResponse update(User user, UpdateAddressRequest request){
        validationService.validate(request);

        Contact contact = contactRepo.findFirstByUserAndId(user, request.getContactId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));
        
        Address address = addressRepo.findFirstByContactAndId(contact, request.getAddressId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));
        
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        addressRepo.save(address);
                    
        return toAddressResponse(address);
    }

    @Transactional
    public void remove(User user, String contactId, String addressId){

        Contact contact = contactRepo.findFirstByUserAndId(user, contactId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));
    
        Address address = addressRepo.findFirstByContactAndId(contact, addressId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));
        
        addressRepo.delete(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(User user, String contactId){

        Contact contact = contactRepo.findFirstByUserAndId(user, contactId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        List<Address> addresses = addressRepo.findAllByContactId(contact.getId());
        return addresses.stream().map(this::toAddressResponse).toList();
        
    }
}

