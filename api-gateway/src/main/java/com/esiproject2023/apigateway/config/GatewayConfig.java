package com.esiproject2023.apigateway.config;

import com.esiproject2023.apigateway.filter.MetadataFallbackGatewayFilterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {

    @Autowired
    private WebClient.Builder webClient;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, MetadataFallbackGatewayFilterFactory metadataFallbackGatewayFilterFactory) {
        return builder.routes()
                .route(r -> r.path("/metadata/**")
                        .and().not(p -> p.path("/metadata/status"))
                        .filters(f -> f.filter(metadataFallbackGatewayFilterFactory.apply(new MetadataFallbackGatewayFilterFactory.Config())))
                        .uri("http://localhost:8081/metadata"))
                .build();
    }

    @Bean
    public MetadataFallbackGatewayFilterFactory metadataFallbackGatewayFilterFactory() {
        return new MetadataFallbackGatewayFilterFactory(webClient);
    }

}
