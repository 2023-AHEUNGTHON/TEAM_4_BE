package aheung.likelion.twoneone.service;

import aheung.likelion.twoneone.domain.entity.User;
import aheung.likelion.twoneone.exception.AppException;
import aheung.likelion.twoneone.exception.ErrorCode;
import aheung.likelion.twoneone.repository.UserRepository;
import aheung.likelion.twoneone.security.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static aheung.likelion.twoneone.domain.enums.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String acessKey; //secret key

    @Value("${jwt.token.refresh}")
    private String refreshKey;

    public String join(String userName, String password, String email){

        // userName 중복 check
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + "는 이미 존재합니다.");
                        }
                );

        // email 중복 check
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                            throw new AppException(ErrorCode.EMAIL_DUPLICATED, email + "는 이미 존재합니다.");
                        }
                );

        // 저장
        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .email(email)
                .role(ROLE_USER)
                .build();
        userRepository.save(user);

        return "SUCCESS";
    }

    public String login(String userName, String password){

        //userName 없음
        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));

        //password 틀림
        if(!encoder.matches(password, selectedUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
        }

        //access token
        String accessToken = getAccessToken(selectedUser.getUserName());


        // Exception 없으면 토큰 발행함
        return accessToken;
    }

    public String updateUserName(String oldUserName, String newUserName) {
        User user = userRepository.findByUserName(oldUserName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, oldUserName + "이 없습니다."));

        // userName 중복 check
        userRepository.findByUserName(newUserName)
                .ifPresent(thatUser -> {
                            throw new AppException(ErrorCode.USERNAME_DUPLICATED, newUserName + "는 이미 존재합니다.");
                        }
                );

        user.setUserName(newUserName);
        userRepository.save(user);
        return user.getUserName();
    }

    public String getAccessToken(String userName){
        Long accessExpireTimeMs = 1000 * 60 * 60l;
//        Long accessExpireTimeMs = 1000 * 60 * 1l;   //test

        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));

        String accessToken = JwtTokenUtil.createToken(userName, selectedUser.getRole(),acessKey, accessExpireTimeMs);

        return accessToken;
    }

    public String getRefreshToken(String userName){
        Long refreshExpireTimeMs = 1000 *60 * 60 *24 *30l;
//        Long refreshExpireTimeMs = 1000 *60 * 2l;   //test

        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));

        String refreshToken = JwtTokenUtil.createToken(userName, selectedUser.getRole() ,refreshKey, refreshExpireTimeMs);

        return refreshToken;
    }

    public boolean validateRefreshToken(String refreshToken){
        return JwtTokenUtil.isValidate(refreshToken,refreshKey);
    }

    public String getUserNameByRefreshToken(String refreshToken){
        return JwtTokenUtil.getUserName(refreshToken,refreshKey);
    }

}
