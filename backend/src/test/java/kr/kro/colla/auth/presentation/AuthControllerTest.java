package kr.kro.colla.auth.presentation;

import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.auth.service.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void 로그인_성공_시_Jwt토큰이_반환된다() throws Exception {
        // given
        String oauthCode = "oauth-code";
        String jwtAccessToken = "jwt-access-token";
        int twoHour = 2 * 60 * 60;
        given(authService.githubLogin(oauthCode)).willReturn(jwtAccessToken);

        // when
        ResultActions perform = mockMvc.perform(get("/auth/login?code=" + oauthCode));

        // then
        perform.andExpect(status().isOk())
                .andExpect(cookie().value("accessToken", jwtAccessToken))
                .andExpect(cookie().maxAge("accessToken", twoHour));
    }

    @Test
    void 올바르지_않은_OAuth코드가_올_경우_로그인에_실패한다() throws Exception {
        // given
        String unauthorizedCode = "unauthorized-code";
        given(authService.githubLogin(unauthorizedCode)).willReturn(null);

        // when
        ResultActions perform = mockMvc.perform(get("/auth/login?code=" + unauthorizedCode));

        // then
        perform.andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("accessToken"));
    }

}
