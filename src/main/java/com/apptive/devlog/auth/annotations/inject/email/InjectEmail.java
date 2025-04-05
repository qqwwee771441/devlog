package com.apptive.devlog.auth.annotations.inject.email;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectEmail {
}
