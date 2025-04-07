package com.apptive.devlog.auth.config;

import com.apptive.devlog.auth.annotations.inject.token.InjectTokenArgumentResolver;
import com.apptive.devlog.auth.annotations.inject.email.InjectEmailArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final InjectEmailArgumentResolver injectEmailArgumentResolver;
    private final InjectTokenArgumentResolver injectTokenArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(injectEmailArgumentResolver);
        resolvers.add(injectTokenArgumentResolver);
    }
}
