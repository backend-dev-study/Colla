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

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;

    @GetMapping("/login")
    public ResponseEntity githubLogin(@RequestParam String code) {
        String accessToken = this.authService.githubLogin(code);
        ResponseCookie cookie = this.cookieManager.createCookie("accessToken", accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/log")
    public ResponseEntity Log(@Authenticated LoginUser loginUser) {
        return ResponseEntity.ok(loginUser);
    }

}
