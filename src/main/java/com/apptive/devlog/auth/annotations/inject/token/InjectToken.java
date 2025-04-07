package com.apptive.devlog.auth.annotations.inject.token;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectToken {
}
