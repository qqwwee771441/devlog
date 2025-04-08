package com.apptive.devlog.domain.oauth2.model;

import com.apptive.devlog.domain.user.entity.User;
import com.apptive.devlog.domain.user.enums.Provider;
import com.apptive.devlog.domain.user.enums.Role;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class OAuth2Attributes {

    private final String nameAttributeKey;
    private final Provider provider;
    private final Map<String, Object> attributes;
    private final String email;
    private final String name;

    public OAuth2Attributes(String nameAttributeKey, Provider provider,
                            Map<String, Object> attributes, String email, String name) {
        this.nameAttributeKey = nameAttributeKey;
        this.provider = provider;
        // this.attributes = attributes;
        this.email = email;
        this.name = name;
        this.attributes = new HashMap<>(attributes); // 임시 방편
        this.attributes.put("email", email); // 임시 방편
    }

    public static OAuth2Attributes of(String registrationId, String userNameAttributeName,
                                      Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(attributes);
        } else if ("naver".equals(registrationId)) {
            return ofNaver(attributes);
        } else {
            return ofGoogle(userNameAttributeName, attributes);
        }
    }

    private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuth2Attributes(
                userNameAttributeName,
                Provider.GOOGLE,
                attributes,
                (String) attributes.get("email"),
                (String) attributes.get("name")
        );
    }

    private static OAuth2Attributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return new OAuth2Attributes(
                "id",
                Provider.NAVER,
                response,
                (String) response.get("email"),
                (String) response.get("name")
        );
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
        Long id = ((Number) attributes.get("id")).longValue();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return new OAuth2Attributes(
                "id",
                Provider.KAKAO,
                attributes,
                (String) kakaoAccount.get("email"),
                (String) profile.get("nickname")
        );
    }

    public User toEntity() {
        return User.of(email, name, provider, Role.USER);
    }
}
