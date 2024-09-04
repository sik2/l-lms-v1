package lab.loop.lms.domain.member.service;

import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private  final MemberRepository memberRepository;

    public Member join(String username, String password, String nickname, byte level, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .level(level)
                .email(email)
                .refreshToken("a3cz4d2ssd3dsd5das7v21hg38dfc4uo23ss3s2df3sd6d")
                .build();
        memberRepository.save(member);

        return member;
    }
}
