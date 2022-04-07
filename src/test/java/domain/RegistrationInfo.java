package domain;

import lombok.*;


@NoArgsConstructor

@Data
public class RegistrationInfo {
    private String login;
    private String password;
    private String status;


}
