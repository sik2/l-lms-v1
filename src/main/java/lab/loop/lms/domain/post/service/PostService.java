package lab.loop.lms.domain.post.service;

import jakarta.transaction.Transactional;
import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.post.entity.Post;
import lab.loop.lms.domain.post.repository.PostRepository;
import lab.loop.lms.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private  final PostRepository postRepository;

    @Transactional
    public RsData<Post> create(Member member, String title, String content, boolean isPublic) {
        Post post = Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .isPublic(isPublic)
                .build();
        postRepository.save(post);

        return RsData.of(
                "200",
                "포스팅이 등록 되었습니다",
                post
        );
    }

    public List<Post> getList () {
            return this.postRepository.findAll();
    }

    public Optional<Post> getPost(Long id) {
        return this.postRepository.findById(id);
    }

    public RsData<Post> modify(Post post, String title, String content, boolean isPublic) {
        post.setTitle(title);
        post.setContent(content);
        post.setIsPublic(isPublic);

        return RsData.of(
                "200",
                "%d번 포스팅이 수정 되었습니다.".formatted(post.getId()),
                post
        );
    }

    public RsData<Post> deleteById(Long id) {
        postRepository.deleteById(id);

        return RsData.of(
                "200",
                "%d번 포스팅이 삭제 되었습니다.".formatted(id),
                null
        );
    }
}
