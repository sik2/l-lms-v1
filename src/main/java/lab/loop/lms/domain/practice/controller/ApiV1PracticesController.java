package lab.loop.lms.domain.practice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.practice.dto.PracticeDto;
import lab.loop.lms.domain.practice.entity.Practice;
import lab.loop.lms.domain.practice.service.PracticeService;
import lab.loop.lms.global.rq.Rq;
import lab.loop.lms.global.rsData.RsData;
import lombok.AllArgsConstructor;
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
    private final Rq rq;

    @AllArgsConstructor
    @Getter
    public static class CreatePracticeResponse {
        private final PracticeDto practiceDto;
    }

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
    public RsData<CreatePracticeResponse> createPractice(@Valid @RequestBody CreatePracticeRequest createPracticeRequest, BindingResult bindingResult) {
        Member member = rq.getMember();
        RsData<Practice> result = practiceService.create(member, createPracticeRequest.getTitle(), createPracticeRequest.getContent(),  createPracticeRequest.getLangType(), createPracticeRequest.getIsPublic(),);

        if (bindingResult.hasErrors()) {
            return RsData.of("400-1","유효하지 않은 입력 입니다.");
        }

        return RsData.of(
                result.getResultCode(),
                result.getMsg(),
                new CreatePracticeResponse(new PracticeDto(result.getData()))
        );
     }

    @AllArgsConstructor
    @Getter
    public static class PracticeResponse {
        private final PracticeDto practiceDto;
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "실습문제 단건 조회")
    public RsData<PracticeResponse> getPracticeById(@PathVariable("id") Long id) {
        RsData<PracticeDto> result = practiceService.getPracticeById(id);
        return RsData.of(
                result.getResultCode(),
                result.getResultCode(),
                new PracticeResponse(result.getData())
        );
    }

    @AllArgsConstructor
    @Getter
    public static class PracticesResponse {
        private final List<PracticeDto> practices;
    }

    @GetMapping(value = "")
    @Operation(summary = "실습문제 다건 조회")
    public RsData<PracticesResponse> getPractices() {
        RsData<List<PracticeDto>> result =  practiceService.getPractices();
        return RsData.of(
                result.getResultCode(),
                result.getMsg(),
                new PracticesResponse(result.getData())
        );
    }

    @AllArgsConstructor
    @Getter
    public static class UpdatePracticeResponse {
        private final Practice practice;
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

    @PatchMapping(value = "/{id}")
    @Operation(summary = "실습문제 수정")
    public RsData<UpdatePracticeResponse> updatePractice(@Valid @RequestBody UpdatePracticeRequest updatePracticeRequest, BindingResult bindingResult, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return RsData.of("400-1","유효하지 않은 입력 입니다.");
        }

        Member member = rq.getMember();

        RsData< Practice> result = practiceService.updatePractice(updatePracticeRequest, member);

        return RsData.of(
                result.getResultCode(),
                result.getMsg(),
                new UpdatePracticeResponse(result.getData())
        );
    }

    @AllArgsConstructor
    @Getter
    public static class RemovePracticeResponse {
        private final Practice practice;
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "실습문제 삭제")
    public RsData<RemovePracticeResponse> deletePracticeById(@PathVariable("id") Long id) {
        RsData<Practice> result = practiceService.deletePracticeById(id);
        return RsData.of(
                result.getResultCode(),
                result.getMsg(),
                new RemovePracticeResponse(result.getData())
        );
    }
}
