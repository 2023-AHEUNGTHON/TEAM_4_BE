package aheung.likelion.twoneone.controller;

import aheung.likelion.twoneone.dto.user.UserJoinRequest;
import aheung.likelion.twoneone.dto.user.UserLoginRequest;
import aheung.likelion.twoneone.dto.user.UserUpdateRequest;
import aheung.likelion.twoneone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest dto){
        userService.join(dto.getUserName(), dto.getPassword(), dto.getEmail());
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody UserLoginRequest dto, HttpServletResponse response){
        String accessToken = userService.login(dto.getUserName(), dto.getPassword());

        // Refresh Token 생성
        String refreshToken = userService.getRefreshToken(dto.getUserName());

        Cookie cookie = new Cookie("refreshToken", refreshToken);

        // expires in 7 days
        cookie.setMaxAge(7 * 24 * 60 * 60);

        // optional properties
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);

        return ResponseEntity.ok().body(accessToken);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody UserUpdateRequest dto,Authentication authentication){
        //현재 userName
        String oldName = authentication.getName();
        String updatedName = userService.updateUserName(oldName, dto.getUserName());
        return ResponseEntity.ok().body("닉네임 변경 성공: " + updatedName);

    }

    @GetMapping("/info")
    public ResponseEntity<String> info(Authentication authentication){
        return ResponseEntity.ok().body(authentication.getName());
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh (HttpServletRequest request){

        // 쿠키에서 Refresh Token 추출
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        //userName 추출
        String userName = userService.getUserNameByRefreshToken(refreshToken);


        log.info("refreshToken:{}",refreshToken);
        log.info("userName:{}",userName);

        if (userService.validateRefreshToken(refreshToken)) {
            // 새로운 Access Token 발급
            String newAccessToken = userService.getAccessToken(userName);// 새로운 Access Token 생성 로직
            return ResponseEntity.ok().body(newAccessToken);
        } else {
            return ResponseEntity.badRequest().body("토큰 인증에 실패했습니다.");
        }

    }

}
