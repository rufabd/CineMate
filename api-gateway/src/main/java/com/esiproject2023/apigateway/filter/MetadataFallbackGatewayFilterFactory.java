package com.esiproject2023.apigateway.filter;

import jakarta.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Predicate;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Component
public class MetadataFallbackGatewayFilterFactory extends AbstractGatewayFilterFactory<MetadataFallbackGatewayFilterFactory.Config> {

    @Autowired
    private WebClient.Builder webClient;

    public MetadataFallbackGatewayFilterFactory(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            return webClient
                    .build()
                    .get()
                    .uri("http://metadata-service/metadata/status")
                    .retrieve()
                    .toBodilessEntity()
                    .flatMap(response -> {
                        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
                            URI backupUri = UriBuilder
                                    .fromUri("http://metadata-backup-service")
                                    .path("/backup/get//search") // + originalUri.getPath()
                                    .build(true);
                            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, backupUri);
                        }
                        return Mono.empty();
                    })
                    .then(chain.filter(exchange));
        });
    }
    public static Predicate<HttpStatus> isErrorCode() {
        return status -> status.is4xxClientError() || status.is5xxServerError();
    }
    public static class Config {
        // No configuration properties needed
    }
}
