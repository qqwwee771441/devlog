package com.apptive.devlog.auth.annotations.inject.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(0)
public class InjectTokenArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(InjectToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Object dto = objectMapper.readValue(request.getInputStream(), parameter.getParameterType());

        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        injectIfFieldExists(dto, "accessToken", accessToken);

        return dto;
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
            throw new RuntimeException("필드 접근 불가: " + fieldName, e);
        }
    }
}
