package kr.kro.colla.auth.presentation.interceptor;

import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.exception.exception.auth.InvalidTokenException;
import kr.kro.colla.exception.exception.auth.TokenNotFoundException;
import kr.kro.colla.utils.CookieManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {

    private JwtProvider jwtProvider;

    private CookieManager cookieManager;

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthInterceptor authInterceptor;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider("secret-key", 7200000, 0);
        cookieManager = new CookieManager("domain", 100000);
        authInterceptor = new AuthInterceptor(authService, cookieManager);
    }

    @Test
    void CORS_Preflight_요청이면_true를_반환한다() {
        // given
        given(request.getMethod())
                .willReturn(HttpMethod.OPTIONS.toString());

        // when
        boolean result = authInterceptor.preHandle(request, response, null);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 올바른_accessToken이_오면_요청한_api를_실행한다() {
        // given
        Long id = 1L;
        String accessToken = jwtProvider.createAccessToken(id);

        given(request.getMethod())
                .willReturn(HttpMethod.GET.toString());
        given(request.getCookies())
                .willReturn(cookies("accessToken", accessToken));
        given(authService.validateAccessToken(accessToken))
                .willReturn(true);

        // when
        boolean result = authInterceptor.preHandle(request, response, null);

        // then
        assertThat(result).isTrue();
        verify(request, times(1)).setAttribute(any(String.class), any(String.class));
    }

    @Test
    void accessToken이_존재하지_않으면_예외가_발생한다() {
        // given
        given(request.getMethod())
                .willReturn(HttpMethod.GET.toString());
        given(request.getCookies())
                .willReturn(cookies("not-match", "not-match"));

        // when, then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, response, null))
                .isInstanceOf(TokenNotFoundException.class);
    }

    @Test
    void accessToken이_유효하지_않으면_refreshToken_확인_후_재발급한다() {
        // given
        Long id = 1L;
        String inValidAccessToken = "invalid-token";
        String newAccessToken = jwtProvider.createAccessToken(id);

        given(request.getMethod())
                .willReturn(HttpMethod.GET.toString());
        given(request.getCookies())
                .willReturn(cookies("accessToken", inValidAccessToken));
        given(authService.validateAccessToken(inValidAccessToken))
                .willReturn(false);
        given(authService.validateRefreshToken(inValidAccessToken))
                .willReturn(newAccessToken);

        // when
        boolean result = authInterceptor.preHandle(request, response, null);

        // then
        assertThat(result).isTrue();
        verify(response, times(1)).addHeader(any(String.class), any(String.class));
    }

    @Test
    void accessToken과_refreshToken_모두_유효하지_않으면_예외가_발생한다() {
        // given
        String inValidAccessToken = "invalid-token";

        given(request.getMethod())
                .willReturn(HttpMethod.GET.toString());
        given(request.getCookies())
                .willReturn(cookies("accessToken", inValidAccessToken));
        given(authService.validateAccessToken(inValidAccessToken))
                .willReturn(false);
        given(authService.validateRefreshToken(inValidAccessToken))
                .willThrow(InvalidTokenException.class);

        // when, then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, response, null))
                .isInstanceOf(InvalidTokenException.class);
    }

    Cookie[] cookies(String name, String value) {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(name, value);
        return cookies;
    }

}
