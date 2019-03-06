package pl.betse.beontime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonIgnore
    private Integer userId;

    @JsonProperty("userId")
    private String userGUID;

    private String emailLogin;

    private String firstName;

    private String lastName;

    @JsonIgnore
    private String password;

    @JsonProperty("active")
    private boolean isActive;

    private String department;

    private Set<String> roles;

}
