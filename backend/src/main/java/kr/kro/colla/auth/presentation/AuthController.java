package kr.kro.colla.auth.presentation;

import kr.kro.colla.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    @Value("${cookie.domain}")
    private String credentialDomain;

    @Value("${cookie.expiration_time}")
    private long expirationTime;

    private final AuthService authService;

    @GetMapping("/login")
    public ResponseEntity githubLogin(@RequestParam String code) {
        String accessToken = this.authService.githubLogin(code);

        if(accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        ResponseCookie cookie = createCookie(accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    private ResponseCookie createCookie(String value) {
        return ResponseCookie.from("accessToken", value)
                .maxAge(expirationTime)
                .domain(credentialDomain)
                .path("/")
                .build();
    }

}
