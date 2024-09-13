package lab.loop.lms.domain.sUrl.controller;

import jakarta.validation.Valid;
import lab.loop.lms.domain.sUrl.entity.SUrl;
import lab.loop.lms.domain.sUrl.request.SUrlRequest;
import lab.loop.lms.domain.sUrl.response.SUrlResponse;
import lab.loop.lms.domain.sUrl.service.SUrlService;
import lab.loop.lms.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
public class ApiV1sUrlsController {

    private final SUrlService sUrlService;

    // SURL 등록
    @PostMapping("")
    public RsData<SUrlResponse> create(@Valid @RequestBody SUrlRequest.CreateRequest createRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return RsData.of("400", "올바른 요청이 아닙니다.", null);
        }
        RsData<SUrl> SUrlRsData = sUrlService.create(createRequest);

        return RsData.of("201", "SURL 등록 완료", new SUrlResponse(SUrlRsData.getData()));
    }

    // SURL 리다이렉트 (단축 shortUrl에서 실제 originUrl로 이동)
    @GetMapping("/go/{id}")
    public RedirectView redirectViewOriginUrl(@PathVariable("id") Long id) {
        // URL이 존재하지 않을 경우 404 오류 처리
        SUrl sUrl = sUrlService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SURL을 찾을 수 없습니다."));

        // redirectCount 증가
        sUrlService.increaseRedirectCount(sUrl);

        // 실제 URL로 리다이렉트 처리
        RedirectView redirectView = new RedirectView(sUrl.getOriginUrl());
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY); // 301 상태 코드 설정
        return redirectView;
    }

    // SUR 단건 조회
    @GetMapping("/{id}")
    public RsData<SUrlResponse> getSUrlById(@PathVariable("id") Long id) {
        Optional<SUrl> sUrlOptional = sUrlService.findById(id);
        if (sUrlOptional.isPresent()) {
            return RsData.of("200", "SURL 단건 조회 완료", new SUrlResponse(sUrlOptional.get()));
        } else {
            return RsData.of("400", "해당 SURL을 찾을 수 없습니다.",null);
        }
    }


    // SURL 목록 조회
    @GetMapping("")
    public RsData<List<SUrlResponse>> getAllSUrls() {
        List<SUrl> sUrls = sUrlService.findAll();
        List<SUrlResponse> sUrlResponses = sUrls.stream()
                .map(SUrlResponse::new)
                .collect(Collectors.toList());
        return RsData.of("200", "SURL 목록 조회 완료", sUrlResponses);
    }


    // SURL 수정
    @PutMapping("/{id}")
    public RsData<SUrlResponse> modifySUrl(@PathVariable("id") Long id,
                                           @Valid @RequestBody SUrlRequest.ModifyRequest modifyRequest,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return RsData.of("400", "올바른 요청이 아닙니다.", null);
        }

        RsData<SUrl> result = sUrlService.modify(id, modifyRequest);

        if (result.isSuccess()) {
            SUrl sUrl = result.getData();

            return RsData.of("200", "해당 SURL 수정 완료", new SUrlResponse(sUrl));
        } else {
            return RsData.of("400", "해당 SURL 수정 실패", null);
        }
    }

    // SURL 삭제
    @DeleteMapping("/{id}")
    public RsData deleteSUrl(@PathVariable("id") Long id) {
        return sUrlService.deleteById(id);
    }
}

