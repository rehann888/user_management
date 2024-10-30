// package projectspring.restful_api.controller;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import projectspring.restful_api.model.RegisterUserRequest;
// import projectspring.restful_api.repository.UserRepo;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class UserControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @Autowired
//     private UserRepo userRepo;

//     @BeforeEach
//     void setUp(){
//         userRepo.deleteAll();
//     }

//     @Test
//     void testRegisteredSuccess() throws Exception{
//         RegisterUserRequest request = new RegisterUserRequest();
//         request.setUsername("rere");
//         request.setPassword("rahasia");
//         request.setName("test");

//         mockMvc.perform(
//                 post("/api/users")
//                 .accept(MediaType.APPLICATION_JSON)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(request))
//         ).andExpectAll(
//                 stat
//         )
//     }
// }
