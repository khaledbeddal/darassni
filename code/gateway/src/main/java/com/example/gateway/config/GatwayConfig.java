package com.example.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatwayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("ms-auth", r -> r.path("/auth/**")
                        .filters(f -> f.rewritePath("/auth/(?<s>.*)", "/${s}").filter(filter))
                        .uri("http://localhost:8094"))

                .route("ms-payment", r -> r.path("/payment/**")
                        .filters(f -> f.rewritePath("/payment/(?<s>.*)", "/${s}").filter(filter))
                        .uri("http://localhost:8080"))

                .route("ms-cours", r -> r.path("/cours/**")
                        .filters(f -> f.rewritePath("/cours/(?<s>.*)", "/${s}").filter(filter))
                        .uri("http://localhost:8081"))

                .route("video_call_app", r -> r.path("/conf/**")
                        .filters(f -> f.rewritePath("/conf/(?<s>.*)", "/${s}").filter(filter))
                        .uri("http://localhost:8087"))

                .build();
    }

}
