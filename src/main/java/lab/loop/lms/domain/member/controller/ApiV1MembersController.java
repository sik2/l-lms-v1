package lab.loop.lms.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lab.loop.lms.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/members", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1MembersController", description = "회원 컨트롤러")
public class ApiV1MembersController {
    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "로그인, 로그인 성공시 accessToken, refreshToken 쿠키 설정")
    public void login() {
    }

    @GetMapping(value = "/me", consumes = ALL_VALUE)
    @Operation(summary = "내 정보")
    public void getMe() {
    }

    @PostMapping(value = "/logout", consumes = ALL_VALUE)
    @Operation(summary = "로그아웃")
    public void logout() {
    }
}
