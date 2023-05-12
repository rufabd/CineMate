package com.esiproject2023.apigateway.filter;

import com.esiproject2023.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;

import java.security.Key;
import java.util.Base64;

@Component
@Slf4j
public class ReviewAuthorizationFilter extends AbstractGatewayFilterFactory<ReviewAuthorizationFilter.Config> {
    public static final String SECRET = "4B6150645267556B58703273357638792F423F4528482B4D6251655468566D59";

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public ReviewAuthorizationFilter() {
        super(ReviewAuthorizationFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(ReviewAuthorizationFilter.Config config) {
        return ((exchange, chain) -> {
            if(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION) != null) {
//                System.out.println(routeValidator.reviewSecuredRole(exchange.getRequest()));
                if(routeValidator.reviewSecuredRole(exchange.getRequest()) != null) {
                    log.info("My method roleee is {}", routeValidator.reviewSecuredRole(exchange.getRequest()));
                    log.info("Request roleee is {}", extractRole(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0)));
                    if(routeValidator.reviewSecuredRole(exchange.getRequest()).contains(extractRole(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0)))) {
                        System.out.println("Authorization is righttttt");
                    }
                    else {
                        throw  new RuntimeException("This route is protected. No entry for you aq");
                    }
                }
            }

            return chain.filter(exchange);
        });
    }

    private Claims extractAllClaims(String token) {
        log.info("Taken tokennnnnnn is like: {}", token);
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
