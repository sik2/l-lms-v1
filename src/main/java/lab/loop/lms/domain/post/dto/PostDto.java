package lab.loop.lms.domain.post.dto;

import lab.loop.lms.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdDate;

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getMember().getUsername();
        this.createdDate = post.getCreatedDate();
    }
}
