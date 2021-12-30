package kr.kro.colla.auth.presentation;

import kr.kro.colla.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void 로그인_성공_시_Jwt토큰이_반환된다() throws Exception {
        // given
        String oauthCode = "oauth-code";
        String jwtAccessToken = "jwt-access-token";
        given(authService.githubLogin(oauthCode)).willReturn(jwtAccessToken);

        // when
        ResultActions perform = mockMvc.perform(get("/auth/login?code=" + oauthCode));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").value(jwtAccessToken));
    }

}
