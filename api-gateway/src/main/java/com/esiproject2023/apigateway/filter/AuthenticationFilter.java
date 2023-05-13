package com.esiproject2023.apigateway.filter;

import com.esiproject2023.apigateway.util.JwtUtil;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())) {
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    return ResponseEntity
                    throw new RuntimeException("No authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader != null && authHeader.startsWith("Bearer")) {
                    authHeader = authHeader.substring(7);
//                    System.out.println("Authorization header is" + authHeader);
                }
                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this resource.");
//                    throw new RuntimeException("unauthorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
