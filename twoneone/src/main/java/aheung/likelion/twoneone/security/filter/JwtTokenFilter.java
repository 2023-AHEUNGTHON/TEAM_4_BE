package aheung.likelion.twoneone.security.filter;

import aheung.likelion.twoneone.domain.enums.Role;
import aheung.likelion.twoneone.security.JwtTokenUtil;
import aheung.likelion.twoneone.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ExpiredJwtException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization:{}", authorization);

        //token 안보내면 block
        if(authorization == null || !authorization.startsWith("Bearer ")){
            log.error("authorization 잘못 보냄.");
            filterChain.doFilter(request, response);
            return;
        }

        // token 꺼내기
        String token = authorization.split(" ")[1];

//         Token Expired 되었는지 여부
        if (!JwtTokenUtil.isValidate(token, secretKey)) {
            log.error("토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        //userName token에서 꺼내기
        String userName = JwtTokenUtil.getUserName(token,secretKey);
        log.info("userName:{}",userName);

        //role token에서 꺼내기
        Role role = JwtTokenUtil.getRoleFromToken(token, secretKey);

        //권한 부여
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null, Collections.singletonList(authority));

        //detail 넣어주기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }


}
