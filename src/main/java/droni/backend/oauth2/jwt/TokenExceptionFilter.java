package droni.backend.oauth2.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import droni.backend.oauth2.execption.JWTException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JWTException jwtException) {
            this.setJWTErrorResponse(request, response, jwtException);
        }
    }

    private void setJWTErrorResponse(HttpServletRequest req, HttpServletResponse res, JWTException jwtException) throws IOException {
        res.setStatus(jwtException.getStatus());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", jwtException.getMessage());
        body.put("path", req.getServletPath());
        objectMapper.writeValue(res.getOutputStream(), body);
    }
}
