package com.apptive.devlog.global.resolver;

import com.apptive.devlog.global.annotation.InjectEmail;
import com.apptive.devlog.global.response.error.exception.TokenInjectionFailedException;
import com.apptive.devlog.global.response.error.exception.UnauthenticatedUserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;

@Slf4j
@Component
@RequiredArgsConstructor
public class InjectEmailArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(InjectEmail.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String email = getCurrentUserEmail();

        Class<?> parameterType = parameter.getParameterType();
        Object dto;

        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            dto = objectMapper.readValue(request.getInputStream(), parameterType);
        } else {
            dto = parameterType.getDeclaredConstructor().newInstance();
        }

        injectIfFieldExists(dto, "email", email);

        return dto;
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthenticatedUserException();
        }
        return authentication.getName();
    }

    private void injectIfFieldExists(Object target, String fieldName, String value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            if (field.getType().equals(String.class)) {
                field.setAccessible(true);
                field.set(target, value);
            }
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException e) {
            throw new TokenInjectionFailedException(fieldName, e);
        }
    }
}
