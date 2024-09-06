package lab.loop.lms.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lab.loop.lms.domain.member.dto.MemberDto;
import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.member.service.MemberService;
import lab.loop.lms.global.rq.Rq;
import lab.loop.lms.global.rsData.RsData;
import lab.loop.lms.standard.base.Empty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/members", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1MembersController", description = "회원 컨트롤러")
public class ApiV1MembersController {
    private final MemberService memberService;
    private final Rq rq;

    public record LoginRequestBody(
            @NotBlank String username,
            @NotBlank String password
    ) {
    }

    public record LoginResponseBody(@NonNull MemberDto item) {
        public LoginResponseBody(Member item) {
            this(new MemberDto(item));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "로그인, 로그인 성공시 accessToken, refreshToken 쿠키 설정")
    public RsData<LoginResponseBody> login(
            @Valid @RequestBody LoginRequestBody body
    ) {
        RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
                body.username,
                body.password
        );

        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().getRefreshToken());
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().getAccessToken());

        return authAndMakeTokensRs.of(
                new LoginResponseBody(authAndMakeTokensRs.getData().getMember())
        );
    }

    public record MeResponseBody(@NonNull MemberDto item) {
        public MeResponseBody(Member item) {
            this(new MemberDto(item));
        }
    }

    @GetMapping(value = "/me", consumes = ALL_VALUE)
    @Operation(summary = "내 정보")
    public RsData<MeResponseBody> getMe() {
        return RsData.of(
                "200",
                "내 정보 가져오기 성공",
                new MeResponseBody(rq.getMember())
        );
    }

    @PostMapping(value = "/logout", consumes = ALL_VALUE)
    @Operation(summary = "로그아웃")
    public RsData<Empty> logout() {
        rq.setLogout();

        return RsData.of("200", "로그아웃 성공");
    }
}
