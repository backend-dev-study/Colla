package kr.kro.colla.auth.presentation;

import kr.kro.colla.auth.presentation.dto.GithubLoginResponse;
import kr.kro.colla.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(value = "*")
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<GithubLoginResponse> githubLogin(@RequestParam String code) {
        String accessToken = this.authService.githubLogin(code);

        if(accessToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GithubLoginResponse.builder()
                            .message("permission denied")
                            .build()
            );
        }

        return ResponseEntity.ok(
                GithubLoginResponse.builder()
                        .accessToken(accessToken)
                        .build()
        );
    }

}
