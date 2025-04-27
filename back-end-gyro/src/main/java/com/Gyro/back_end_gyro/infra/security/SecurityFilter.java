package com.Gyro.back_end_gyro.infra.security;

import com.Gyro.back_end_gyro.domain.user.entity.User;
import com.Gyro.back_end_gyro.domain.user.repository.UserRepository;
import com.Gyro.back_end_gyro.domain.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {


    private final TokenService tokenService;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token != null) {
            String email = tokenService.getSubjectFromToken(token);
            Optional<UserDetails> userOptional = userRepository.findUserByEmail(email);

            if (userOptional.isPresent()) {
                User user = (User) userOptional.get();
                String userRole = tokenService.getUserRoleFromToken(token);
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userRole));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);

    }


    private String getTokenFromRequest(HttpServletRequest request) {
        String tokenFromRequest = request.getHeader("Authorization");


        if (tokenFromRequest != null) {
            return tokenFromRequest.replace("Bearer ", "");
        }

        return null;
    }
}
