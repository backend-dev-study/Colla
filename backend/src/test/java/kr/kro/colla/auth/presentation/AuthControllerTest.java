package kr.kro.colla.auth.presentation;

import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.utils.CookieManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CookieManager cookieManager;

    @MockBean
    private AuthService authService;

    @Test
    void 로그인_성공_시_Jwt토큰이_반환된다() throws Exception {
        // given
        String oauthCode = "oauth-code";
        String jwtAccessToken = "jwt-access-token";
        ResponseCookie cookie = ResponseCookie.from("accessToken", jwtAccessToken)
                .build();

        given(authService.githubLogin(oauthCode))
                .willReturn(jwtAccessToken);
        given(cookieManager.createCookie("accessToken", jwtAccessToken))
                .willReturn(cookie);


        // when
        ResultActions perform = mockMvc.perform(get("/auth/login?code=" + oauthCode));

        // then
        perform.andExpect(status().isOk())
                .andExpect(cookie().value("accessToken", jwtAccessToken));
    }

    @Test
    void 올바르지_않은_OAuth코드가_올_경우_로그인에_실패한다() throws Exception {
        // given
        String unauthorizedCode = "unauthorized-code";

        given(authService.githubLogin(unauthorizedCode))
                .willThrow(HttpClientErrorException.class);

        // when
        ResultActions perform = mockMvc.perform(get("/auth/login?code=" + unauthorizedCode));

        // then
        perform.andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("accessToken"));
    }

}
