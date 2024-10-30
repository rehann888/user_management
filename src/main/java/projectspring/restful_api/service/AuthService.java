package projectspring.restful_api.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import projectspring.restful_api.entity.User;
import projectspring.restful_api.model.LoginUserRequest;
import projectspring.restful_api.model.TokenResponse;
import projectspring.restful_api.repository.UserRepo;
import projectspring.restful_api.security.BCrypt;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login (LoginUserRequest request){
        validationService.validate(request);

        User user = userRepo.findById(request.getUsername())
                    .orElseThrow(() -> new ResponseStatusException (HttpStatus.UNAUTHORIZED, "Username or password is wrong"));
        
        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next300days());
            userRepo.save(user);

            return TokenResponse.builder().token(user.getToken()).expiredAt(user.getTokenExpiredAt()).build();
        } else
        throw new ResponseStatusException (HttpStatus.UNAUTHORIZED, "Username or password is wrong");
    }

    private Long next300days(){
        return System.currentTimeMillis() + (1000*16*24*30);
    }

}
