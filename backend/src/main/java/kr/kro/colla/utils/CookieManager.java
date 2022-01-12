package kr.kro.colla.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class CookieManager {

    private final String credentialDomain;

    private final long expirationTime;

    public CookieManager(String credentialDomain, long expirationTime) {
        this.credentialDomain = credentialDomain;
        this.expirationTime = expirationTime;
    }

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

    public void expireCookie(HttpServletResponse response, String name) {
        ResponseCookie clearCookie = ResponseCookie.from(name, null)
                .maxAge(0)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
    }

}
