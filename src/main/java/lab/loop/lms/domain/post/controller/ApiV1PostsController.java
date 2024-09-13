package lab.loop.lms.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.post.dto.PostDto;
import lab.loop.lms.domain.post.entity.Post;
import lab.loop.lms.domain.post.repository.PostRepository;
import lab.loop.lms.domain.post.service.PostService;
import lab.loop.lms.global.rq.Rq;
import lab.loop.lms.global.rsData.RsData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/posts", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1PostsController", description = "맴버 컨트롤러")
public class ApiV1PostsController {
    private final PostService postService;
    private final Rq rq;
    private final PostRepository postRepository;

    @GetMapping(value = "", consumes = ALL_VALUE)
    @Operation(summary = "포스팅 다건")
    public RsData<PostsResponse> getList() {
        List<PostDto> postDtoList = this.postService
                .getList()
                .stream()
                .map(post -> new PostDto(post))
                .toList();

        return RsData.of("200", "성공", new PostsResponse(postDtoList));
    }

    @GetMapping(value = "/{id}", consumes = ALL_VALUE)
    @Operation(summary = "포스팅 단건")
    public RsData<PostResponse>  getPost(@PathVariable Long id) {
        return postService.getPost(id).map(post -> RsData.of(
                "200",
                "성공",
                new PostResponse(new PostDto(post))
        )).orElseGet(() -> RsData.of("400",  "%d 번 게시물은 존재하지 않습니다.".formatted(id), null));
    }

    @PostMapping("")
    @Operation(summary = "포스팅 등록")
    public RsData<WriteResponse> write(@Valid @RequestBody WriteRequest writeRequest) {
        Member member = rq.getMember();
        RsData<Post> result = this.postService.create(member, writeRequest.getTitle(), writeRequest.getContent(), writeRequest.getIsPublic());

        if (result.isFail()) return (RsData) result;

        return RsData.of(
                result.getResultCode(),
                result.getMsg(),
                new WriteResponse(new PostDto(result.getData()))
        );
    }

    @PatchMapping("/{id}")
    @Operation(summary = "포스팅 수정")
    public RsData<ModifyResponse> write(@Valid @RequestBody ModifyRequest modifyRequest, @PathVariable("id") Long id) {
        Optional<Post> optionalPost = this.postService.getPost(id);

        if (optionalPost.isEmpty()) {
            return RsData.of(
                    "400",
                    "%d번 포스팅은 존재하지 않습니다.".formatted(id),
                    null
            );
        }

        RsData<Post> result = this.postService.modify(optionalPost.get(), modifyRequest.getTitle(), modifyRequest.getContent(), modifyRequest.getIsPublic());

        return RsData.of(
                result.getResultCode(),
                result.getMsg(),
                new ModifyResponse(result.getData())
        );
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "포스팅 삭제")
    public RsData<RemoveResponse> remove(@PathVariable("id") Long id) {
        Optional<Post> optionalPost = this.postService.getPost(id);

        if (optionalPost.isEmpty()) {
            return RsData.of(
            "400",
            "%d번 게시물은 존재하지 않습니다.".formatted(id),
            null
            );
        }

        RsData<Post> result = postService.deleteById(id);

        return RsData.of(
                result.getResultCode(),
                result.getMsg(),
                new RemoveResponse(optionalPost.get())
        );

    }


    @AllArgsConstructor
    @Getter
    public static class PostsResponse {
        private final List<PostDto> posts;
    }


    @AllArgsConstructor
    @Getter
    public static class PostResponse {
        private final PostDto post;
    }

    @Data
    public static class WriteRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        @NotBlank
        private Boolean isPublic;
    }

    @AllArgsConstructor
    @Getter
    public static class WriteResponse {
        private final PostDto postDto;
    }

    @Data
    public static class ModifyRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        @NotBlank
        private Boolean isPublic;
    }

    @AllArgsConstructor
    @Getter
    public static class ModifyResponse {
        private final Post post;
    }

    @AllArgsConstructor
    @Getter
    public static class RemoveResponse {
        private final Post Post;
    }
}
