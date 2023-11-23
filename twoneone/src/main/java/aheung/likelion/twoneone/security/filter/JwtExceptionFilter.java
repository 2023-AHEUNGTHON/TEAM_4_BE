package aheung.likelion.twoneone.security.filter;//package aheung.likelion.twoneone.exception;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpStatus;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // JWT 만료 예외 처리
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Expired JWT token");
        }
//        catch (Exception e) {
//            // 다른 JWT 관련 예외 처리
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.getWriter().write("Internal Server Error");
//        }
    }
}

