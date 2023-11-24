package aheung.likelion.twoneone.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinRequest {
    private String userName;
    private String password;
    private String nickName;
}
