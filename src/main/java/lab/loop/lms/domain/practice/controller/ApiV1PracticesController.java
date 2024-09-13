package lab.loop.lms.domain.practice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lab.loop.lms.domain.practice.dto.PracticeDto;
import lab.loop.lms.domain.practice.service.PracticeService;
import lab.loop.lms.global.rsData.RsData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/practices", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1PracticesController", description = "실습 컨트롤러")
public class ApiV1PracticesController {

    private final PracticeService practiceService;

    @Getter
    @Setter
    public static class CreatePracticeRequest {

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        @NotBlank
        private String langType;

        @NotNull
        private Boolean isPublic;

    }

    @PostMapping(value = "")
    @Operation(summary = "실습문제 등록")
    public RsData<?> createPractice(@Valid @RequestBody CreatePracticeRequest req, BindingResult br) {

        if(br.hasErrors()){
            return RsData.of("400-1","유효하지 않은 입력 입니다.");
        }

        return practiceService.createPractice(req);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "실습문제 단건 조회")
    public RsData<PracticeDto> getPracticeById(@PathVariable("id") Long id) {
        return practiceService.getPracticeById(id);
    }

    @GetMapping(value = "")
    @Operation(summary = "실습문제 다건 조회")
    public RsData<List<PracticeDto>> getPractices() {
        return practiceService.getPractices();
    }

    @Getter
    @Setter
    public static class UpdatePracticeRequest {

        @NotNull
        private Long id;

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        @NotBlank
        private String langType;

        @NotNull
        private Boolean isPublic;

    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "실습문제 삭제")
    public RsData<?> deletePracticeById(@PathVariable("id") Long id) {

        return practiceService.deletePracticeById(id);
    }

    @PatchMapping(value = "")
    @Operation(summary = "실습문제 수정")
    public RsData<?> updatePractice(@Valid @RequestBody UpdatePracticeRequest req, BindingResult br) {

        if(br.hasErrors()){
            return RsData.of("400-1","유효하지 않은 입력 입니다.",1);
        }

        return practiceService.updatePractice(req);
    }
}
