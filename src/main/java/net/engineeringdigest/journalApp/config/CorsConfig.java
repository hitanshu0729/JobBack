package net.engineeringdigest.journalApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*"); // Allow all origins
        corsConfiguration.addAllowedMethod("GET");      // Allow GET
        corsConfiguration.addAllowedMethod("POST");     // Allow POST
        corsConfiguration.addAllowedMethod("PUT");      // Allow PUT
        corsConfiguration.addAllowedMethod("DELETE");   // Allow DELETE
        corsConfiguration.addAllowedHeader("*");        // Allow all headers
        corsConfiguration.setAllowCredentials(true);    // Allow credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Apply to all endpoints

        return new CorsFilter(source);
    }
}
