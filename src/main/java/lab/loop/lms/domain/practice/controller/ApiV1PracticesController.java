package lab.loop.lms.domain.practice.controller;

import jakarta.persistence.Column;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/practices")
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

    // 문제 등록
    @PostMapping(value = "")
    public RsData<?> createPractice(@Valid @RequestBody CreatePracticeRequest req, BindingResult br) {

        if(br.hasErrors()){
            return RsData.of("400-1","유효하지 않은 입력 입니다.",1);
        }

        return practiceService.createPractice(req);
    }

    // 단건 조회
    @GetMapping(value = "/{id}")
    public RsData<PracticeDto> getPracticeById(@PathVariable("id") Long id) {
        return practiceService.getPracticeById(id);
    }

    // 다건 조회
    @GetMapping(value = "")
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

    // 삭제
    @DeleteMapping(value = "/{id}")
    public RsData<?> deletePracticeById(@PathVariable("id") Long id) {

        return practiceService.deletePracticeById(id);
    }

    // 수정
    @PatchMapping(value = "")
    public RsData<?> updatePractice(@Valid @RequestBody UpdatePracticeRequest req, BindingResult br) {

        if(br.hasErrors()){
            return RsData.of("400-1","유효하지 않은 입력 입니다.",1);
        }

        return practiceService.updatePractice(req);
    }



}
