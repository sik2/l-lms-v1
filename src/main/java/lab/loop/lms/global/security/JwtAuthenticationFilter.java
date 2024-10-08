package lab.loop.lms.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab.loop.lms.domain.member.service.MemberService;
import lab.loop.lms.global.rq.Rq;
import lab.loop.lms.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Rq rq;
    private final MemberService memberService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (List.of("/api/v1/members/login", "/api/v1/members/join").contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            String tokensStr = bearerToken.substring("Bearer ".length());
            String[] tokens = tokensStr.split("#", 2);
            String refreshToken = tokens[0];
            String accessToken = tokens.length == 2 ? tokens[1] : "";

            if (!accessToken.isBlank()) {
                if (!memberService.validateToken(accessToken)) {
                    RsData<String> rs = memberService.refreshAccessToken(refreshToken);
                    accessToken = rs.getData();
                    rq.setHeader("Authorization", "Bearer " + refreshToken + "#" + accessToken);
                }

                SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
                rq.setLogin(securityUser);
            }
        } else {
            String accessToken = rq.getCookieValue("accessToken", "");

            if (!accessToken.isBlank()) {
                if (!memberService.validateToken(accessToken)) {
                    String refreshToken = rq.getCookieValue("refreshToken", "");

                    RsData<String> rs = memberService.refreshAccessToken(refreshToken);
                    accessToken = rs.getData();
                    rq.setCrossDomainCookie("accessToken", accessToken);
                }

                SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
                rq.setLogin(securityUser);
            }
        }

        filterChain.doFilter(request, response);
    }
}