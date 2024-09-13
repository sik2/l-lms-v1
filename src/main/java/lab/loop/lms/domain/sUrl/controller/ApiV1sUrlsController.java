package lab.loop.lms.domain.sUrl.controller;

import jakarta.validation.Valid;
import lab.loop.lms.domain.sUrl.entity.SUrl;
import lab.loop.lms.domain.sUrl.request.SUrlRequest;
import lab.loop.lms.domain.sUrl.response.SUrlResponse;
import lab.loop.lms.domain.sUrl.service.SUrlService;
import lab.loop.lms.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
public class ApiV1sUrlsController {

    private final SUrlService sUrlService;

    @PostMapping("")
    public RsData create(@Valid @RequestBody SUrlRequest.CreateRequest createRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return RsData.of("1", "올바른 요청이 아닙니다.");
        }

        RsData<SUrl> result = sUrlService.create(createRequest);

        if (result.isSuccess()) {
            return RsData.of("1", "SURL 등록 완료", new SUrlResponse(result.getData()));
        } else {
            return RsData.of("1", "SURL 등록 실패: " + result.getMsg(), null);
        }
    }

    @GetMapping("/{id}")
    public RsData<SUrlResponse> getSUrlById(@PathVariable("id") Long id) {
        Optional<SUrl> sUrlOptional = sUrlService.findById(id);
        if (sUrlOptional.isPresent()) {
            return RsData.of("1", "SURL 조회 완료", new SUrlResponse(sUrlOptional.get()));
        } else {
            return RsData.of("1", "해당 SURL을 찾을 수 없습니다.",null);
        }
    }

    @GetMapping("")
    public RsData<List<SUrlResponse>> getAllSUrls() {
        List<SUrl> sUrls = sUrlService.findAll();
        List<SUrlResponse> sUrlResponses = sUrls.stream()
                .map(SUrlResponse::new)
                .collect(Collectors.toList());
        return RsData.of("1", "SURL 목록 조회 완료", sUrlResponses);
    }

    @PutMapping("/{id}")
    public RsData<SUrlResponse> updateSUrl(@PathVariable("id") Long id,
                                           @Valid @RequestBody SUrlRequest.ModifyRequest modifyRequest,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return RsData.of("1", "올바른 요청이 아닙니다.",null);
        }

        RsData<SUrl> result = sUrlService.modify(id, modifyRequest);

        if (result.isSuccess()) {
            return RsData.of("1", "해당 SURL 수정 완료", new SUrlResponse(result.getData()));
        } else {
            return RsData.of("1", "해당 SURL 수정 실패",null);
        }
    }

    @DeleteMapping("/{id}")
    public RsData deleteSUrl(@PathVariable("id") Long id) {
        RsData result = sUrlService.deleteById(id);
        if (result.isSuccess()) {
            return RsData.of("1", "해당 SURL 삭제 완료" );
        } else {
            return RsData.of("1", "해당 SURL 삭제 실패",null);
        }
    }
}

