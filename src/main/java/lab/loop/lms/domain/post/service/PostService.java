package lab.loop.lms.domain.post.service;

import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.post.entity.Post;
import lab.loop.lms.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private  final PostRepository postRepository;

    public void create(Member member, String title, String content, boolean isPublic) {
        Post post = Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .isPublic(isPublic)
                .build();

        postRepository.save(post);
    }
}
