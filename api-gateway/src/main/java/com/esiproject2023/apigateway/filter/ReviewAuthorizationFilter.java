package com.esiproject2023.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ResponseStatusException;
import java.security.Key;
import java.util.Base64;

@Component
@Slf4j
public class ReviewAuthorizationFilter extends AbstractGatewayFilterFactory<ReviewAuthorizationFilter.Config> {
    public static final String SECRET = "4B6150645267556B58703273357638792F423F4528482B4D6251655468566D59";

    @Autowired
    private RouteValidator routeValidator;

    public ReviewAuthorizationFilter() {
        super(ReviewAuthorizationFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(ReviewAuthorizationFilter.Config config) {
        return ((exchange, chain) -> {
            if(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION) != null) {
//                System.out.println(routeValidator.reviewSecuredRole(exchange.getRequest()));
                if(routeValidator.reviewSecuredRole(exchange.getRequest()) != null) {
                    if(!routeValidator.reviewSecuredRole(exchange.getRequest()).contains(extractRole(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0)))) {
                        try {
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this resource.");
                        } catch (RuntimeException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            return chain.filter(exchange);
        });
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token.substring(7))
                .getBody();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    private Key signingKey() {
        byte[] keyDecoder = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyDecoder);
    }



    public static class Config {
    }
}
