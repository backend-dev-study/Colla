package kr.kro.colla.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class CookieManager {

    @Value("${cookie.domain}")
    private String credentialDomain;

    @Value("${cookie.expiration_time}")
    private long expirationTime;

    public ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .maxAge(expirationTime)
                .domain(credentialDomain)
                .path("/")
                .build();
    }

    public Cookie parseCookies(Cookie[] cookies, String name) {
        return Optional.ofNullable(cookies)
                .map(Arrays::stream)
                .orElse(Stream.empty())
                .filter(cookie -> cookie.getName().equals(name))
                .findAny()
                .orElse(null);
    }

}
