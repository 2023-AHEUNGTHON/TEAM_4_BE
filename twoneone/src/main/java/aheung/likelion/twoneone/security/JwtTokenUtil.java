package aheung.likelion.twoneone.security;

import aheung.likelion.twoneone.domain.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtTokenUtil {

    public static String getUserName(String token, String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("userName", String.class); //toString도 가능
    }

    public static Role getRoleFromToken(String token, String secretKey) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

        String roleName = claims.get("role", String.class);
        return Role.valueOf(roleName); // String 값을 Enum으로 변환
    }

    public static boolean isValidate(String token, String secretKey){   // throws ExpiredJwtException
            return !Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());  //지금보다 전이다
    }

    public static String createToken(String userName, Role role, String key, Long expireTimeMs){
        Claims claims = Jwts.claims(); //일종의 map
        claims.put("userName",userName);    //userName 저장
        claims.put("role", role.name());//권한 저장

        return  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact()
                ;
    }

}
