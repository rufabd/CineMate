package com.esiproject2023.apigateway.filter;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.regex.RegExp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
@Slf4j
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "auth/authenticate",
            "auth/register",
            "auth/login",
            "auth/{username}",
            "review/content/{contentId}"
    );
    public static final Map<String, List<String>> reviewEndpointsSecured = Map.of("/review/all", List.of("ADMIN"), "/review/add",
            List.of("USER"), "/review/delete/", List.of("ADMIN"), "/review/view/", List.of("ADMIN"), "/review/content/", List.of("ADMIN", "USER"));


    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));

    public static List<String> containsSubstring(Map<String, List<String>> map, String path) {
        for (String key : map.keySet()) {
            if (path.contains(key)) {
                return map.get(key);
            }
        }
        return List.of("");
    }

    public List<String> reviewSecuredRole(ServerHttpRequest exchange) {
        String path = exchange.getURI().getPath();
        return containsSubstring(reviewEndpointsSecured, path);
    }
}
