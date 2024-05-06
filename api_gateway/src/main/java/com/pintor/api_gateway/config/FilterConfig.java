package com.pintor.api_gateway.config;

import com.pintor.api_gateway.filter.AuthorizationHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    private final String userModuleUrl = "http://localhost:8081";
    private final String productModuleUrl = "http://localhost:8082";
    private final String purchaseModuleUrl = "http://localhost:8083";

    private final AuthorizationHeaderFilter authorizationHeaderFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/member_module/members")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(userModuleUrl)
                )
                .route(r -> r.path("/member_module/members/password")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(userModuleUrl)
                )
                .route(r -> r.path("/member_module/auth/login")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(userModuleUrl)
                )
                .route(r -> r.path("/member_module/auth/login/mail")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(userModuleUrl)
                )
                .route(r -> r.path("/member_module/auth/refresh")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(userModuleUrl)
                )
                .route(r -> r.path("/member_module/auth/verify/mail")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(userModuleUrl)
                )
                .route(r -> r.path("/member_module/**")
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}")
                                .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config())))
                        .uri(userModuleUrl)
                )
                .route(r -> r.path("/product_module/**")
                        .filters(f -> f.rewritePath("/product_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(productModuleUrl)
                )
                .route(r -> r.path("/purchase_module/**")
                        .filters(f -> f.rewritePath("/purchase_module/(?<segment>.*)", "/api/${segment}")
                                .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config())))
                        .uri(purchaseModuleUrl)
                )
                .build();
    }
}
