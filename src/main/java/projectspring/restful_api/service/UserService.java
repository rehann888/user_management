package projectspring.restful_api.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import projectspring.restful_api.entity.User;
import projectspring.restful_api.model.RegisterUserRequest;
import projectspring.restful_api.model.UpdateUserRequest;
import projectspring.restful_api.model.UserResponse;
import projectspring.restful_api.repository.UserRepo;
import projectspring.restful_api.security.BCrypt;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request){
        validationService.validate(request);

       if (userRepo.existsById(request.getUsername())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
       }

       User user = new User();
       user.setUsername(request.getUsername());
       user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
       user.setName(request.getName());
       userRepo.save(user);
    }

    public UserResponse get(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    public UserResponse update(User user, UpdateUserRequest request){
        validationService.validate(request);

        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        userRepo.save(user);

        return UserResponse.builder()
        .name(user.getName())
        .username(user.getUsername())
        .build();
    }

    @Transactional
    public void logout (User user){
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepo.save(user);
    }
}
