package kr.kro.colla.auth.presentation.interceptor;

import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.exception.exception.auth.TokenNotFoundException;
import kr.kro.colla.utils.CookieManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final CookieManager cookieManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("OPTIONS")){
            return true;
        }

        Cookie[] cookies = request.getCookies();
        Cookie accessToken = cookieManager.parseCookies(cookies, "accessToken");

        if(accessToken == null) {
            throw new TokenNotFoundException();
        }

        boolean isValid = authService.validateAccessToken(accessToken.getValue());

        if(!isValid) {
            String newAccessToken = authService.validateRefreshToken(accessToken.getValue());
            ResponseCookie cookie = cookieManager.createCookie("accessToken", newAccessToken);

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            accessToken.setValue(newAccessToken);
        }

        request.setAttribute("accessToken", accessToken.getValue());
        return true;
    }

}
