package com.Gyro.back_end_gyro.infra.security;

import com.Gyro.back_end_gyro.domain.user.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {


    @Value("${API_SECRET}")
    private String secret;


    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("gyro-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(expiresAt())
                    .withClaim("companyId", getIdFromCompanyOrEmployee(user))
                    .withClaim("userRole", user.getRoles().name())
                    .withClaim("employeeId", getEmployeeIdOrNull(user))
                    .sign(algorithm);


        } catch (JWTCreationException exception) {
            throw new RuntimeException("JWT generation failed");
        }
    }


    public String getSubjectFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String formattedToken = formattedToken(token);

            return JWT.require(algorithm)
                    .withIssuer("gyro-api")
                    .build()
                    .verify(formattedToken)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("JWT verification failed on get subject");
        }
    }

    public Long getCompanyIdFromToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String formattedToken = formattedToken(token);
            return JWT.require(algorithm)
                    .withIssuer("gyro-api")
                    .build()
                    .verify(formattedToken)
                    .getClaim("companyId")
                    .asLong();

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("JWT verification failed on get companyId");
        }
    }


    public String getUserRoleFromToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String formattedToken = formattedToken(token);
            return JWT.require(algorithm)
                    .withIssuer("gyro-api")
                    .build()
                    .verify(formattedToken)
                    .getClaim("userRole")
                    .asString();

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("JWT verification failed on get userRole");
        }
    }


    public Long getEmployeeIdFromToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String formattedToken = formattedToken(token);

            return JWT.require(algorithm)
                    .withIssuer("gyro-api")
                    .build()
                    .verify(formattedToken)
                    .getClaim("employeeId")
                    .asLong();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("JWT verification failed on getEmployeeId");
        }
    }


    private String formattedToken(String token) {
        return token.replace("Bearer ", "");
    }

    private Instant expiresAt() {
        return ZonedDateTime.now(ZoneOffset.of("-03:00")).plusHours(2).toInstant();
    }

    private Long getIdFromCompanyOrEmployee(User user) {
        if (user.getCompany() != null && user.getCompany().getId() != null) {
            return user.getCompany().getId();
        }

        if (user.getEmployee().getCompany() != null && user.getEmployee().getCompany().getId() != null) {
            return user.getEmployee().getCompany().getId();
        }
        return null;
    }

    private Long getEmployeeIdOrNull(User user) {
        if (user.getEmployee() != null && user.getEmployee().getId() != null) {
            return user.getEmployee().getId();
        }
        return null;
    }
}
