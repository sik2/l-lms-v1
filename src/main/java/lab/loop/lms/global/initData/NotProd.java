package lab.loop.lms.global.initData;

import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.member.service.MemberService;
import lab.loop.lms.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;
    private final PostService postService;

    @Bean
    public ApplicationRunner initNotProd() {
        return new ApplicationRunner() {
            @Transactional
            @Override
            public void run(ApplicationArguments args) throws Exception {
                if (memberService.findByUsername("user1").isPresent()) return;

                Member memberUser1 = memberService.join("user1", "1234").getData();
                memberUser1.setRefreshToken("user1");

                Member memberUser2 = memberService.join("user2", "1234").getData();
                memberUser2.setRefreshToken("user2");

                Member memberUser3 = memberService.join("user3", "1234").getData();
                memberUser3.setRefreshToken("user3");

                Member memberUser4 = memberService.join("user4", "1234").getData();
                memberUser4.setRefreshToken("user4");
            }
        };
    }
}