package me.ldclrcq.filature.targets.nextcloud.client;

import jakarta.ws.rs.HttpMethod;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod(value = "MKCOL")
@Documented
public @interface MKCOL {
}