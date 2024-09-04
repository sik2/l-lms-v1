package lab.loop.lms.global.initData;

import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.member.service.MemberService;
import lab.loop.lms.domain.post.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(PostService postService, MemberService memberService, PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode("1234");
        return args -> {
            // 회원 3명 추가
            Member user1 = memberService.join("user1", password, "user1", (byte)0, "user1@test.com");
            Member user2 = memberService.join("user2", password, "user2", (byte)0, "user2@test.com");
            Member manager = memberService.join("manager", password, "manager", (byte)1, "manager@test.com");
            Member admin = memberService.join("admin", password, "admin", (byte)2, "admin@test.com");

            // 작성자 회원 추가
            postService.create(user1, "제목 1", "내용 1", true);
            postService.create(user1, "제목 2", "내용 2", true);
            postService.create(manager, "제목 3", "내용 3", true);
            postService.create(manager, "제목 4", "내용 4", true);
            postService.create(admin, "제목 5", "내용 5", false);
        };
    }
}
