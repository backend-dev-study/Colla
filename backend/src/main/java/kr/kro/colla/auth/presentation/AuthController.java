package kr.kro.colla.auth.presentation;

import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.presentation.argument_resolver.Authenticated;
import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.utils.CookieManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;

    @GetMapping("/login")
    public ResponseEntity githubLogin(@RequestParam String code) {
        String accessToken = authService.githubLogin(code);
        ResponseCookie cookie = cookieManager.createCookie("accessToken", accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@Authenticated LoginUser loginUser, HttpServletResponse response) {
        cookieManager.expireCookie(response, "accessToken");
        authService.removeRefreshToken(loginUser.getId());

        return ResponseEntity.noContent()
                .build();
    }

}
