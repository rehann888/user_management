package projectspring.restful_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactResponse {

    private String id;

    private String firstName;
    
    private String LastName;

    private String email;

    private String phone;


}
