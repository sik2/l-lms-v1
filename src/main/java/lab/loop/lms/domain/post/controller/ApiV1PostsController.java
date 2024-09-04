package lab.loop.lms.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/members", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1PostsController", description = "맴버 컨트롤러")
public class ApiV1PostsController {
    @GetMapping(value = "", consumes = ALL_VALUE)
    @Operation(summary = "글 다건")
    public void  getList() {

    }

    @GetMapping(value = "/{id}", consumes = ALL_VALUE)
    @Operation(summary = "글 단건")
    public void  getPost(@PathVariable String id) {

    }
}
