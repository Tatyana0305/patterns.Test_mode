package domain;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor


public class RegistrationInfo {
    private String login;
    private String password;
    private String status;


}
