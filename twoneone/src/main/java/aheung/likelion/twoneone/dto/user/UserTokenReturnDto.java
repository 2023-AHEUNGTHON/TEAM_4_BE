package aheung.likelion.twoneone.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTokenReturnDto {
    private String token;
}
