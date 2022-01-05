package kr.kro.colla.auth.presentation.interceptor;

import kr.kro.colla.auth.infrastructure.RedisManager;
import kr.kro.colla.auth.presentation.CookieManager;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.exception.exception.auth.InvalidTokenException;
import kr.kro.colla.exception.exception.auth.TokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final CookieManager cookieManager;
    private final JwtProvider jwtProvider;
    private final RedisManager redisManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        Cookie accessToken = this.cookieManager.parseCookies(cookies, "accessToken");

        if(accessToken == null) {
            throw new TokenNotFoundException();
        }

        boolean isValid = this.jwtProvider.validateToken(accessToken.getValue());

        if(!isValid) {
            String newAccessToken = validateRefreshToken(accessToken.getValue());
            ResponseCookie cookie = this.cookieManager.createCookie("accessToken", newAccessToken);

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            accessToken.setValue(newAccessToken);
        }

        request.setAttribute("accessToken", accessToken.getValue());
        return true;
    }

    private String validateRefreshToken(String accessToken) {
        Long id = this.jwtProvider.parseToken(accessToken);
        String refreshToken = this.redisManager.findValue(id.toString());
        boolean isValid = this.jwtProvider.validateToken(refreshToken);

        if(!isValid) {
            throw new InvalidTokenException();
        }

        return this.jwtProvider.createAccessToken(id);
    }

}
