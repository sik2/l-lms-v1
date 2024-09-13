package lab.loop.lms.domain.sUrl.service;

import lab.loop.lms.domain.sUrl.entity.SUrl;
import lab.loop.lms.domain.sUrl.repository.SUrlRepository;
import lab.loop.lms.domain.sUrl.request.SUrlRequest;
import lab.loop.lms.domain.sUrl.response.SUrlResponse;
import lab.loop.lms.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SUrlService {

    private final SUrlRepository sUrlRepository;

    // SURL 등록
    @Transactional
    public RsData<SUrl> create(SUrlRequest.CreateRequest sUrlRequest) {
        // 단축 URL 생성 로직 적용
        String shortUrl = generateShortUrl(sUrlRequest.getOriginUrl());

        SUrl sUrl = SUrl.builder()
                .originUrl(sUrlRequest.getOriginUrl())
                .shortUrl(shortUrl) // 단축 URL 설정
                .redirectCount(sUrlRequest.getRedirectCount())
                .isActive(sUrlRequest.getIsActive())
                .build();

        sUrlRepository.save(sUrl);

        return RsData.of("201", "SURL 등록 완료",sUrl);
    }

    // 단축 URL 생성 메서드
    private String generateShortUrl(String originUrl) {
        // 임시로 lms.v1/ 을 기본 설정하였습니다.
        // lms.v1/6자리 숫자
        String baseUrl = "lms.v1/";
        String hashedUrl = hashUrl(originUrl);
        // 6자리 단축 URL 반환
        return baseUrl + hashedUrl.substring(0, 6);
    }

    // URL 해시 처리 메서드
    private String hashUrl(String url) {
        try {
            // SHA-256 해시 알고리즘을 사용하여 MessageDigest 객체 생성
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // URL 문자열을 바이트 배열로 변환한 후, 이를 digest.digest() 메서드로 해시 처리
            byte[] hash = digest.digest(url.getBytes());
            // BigInteger 객체를 16진수 문자열(해시 값)로 변환
            BigInteger number = new BigInteger(1, hash);
            String hexString = number.toString(16);

            // SHA-256 해시는 64자리의 16진수로 생성되지만, 혹시 길이가 짧은 경우를 대비하여
            // 해시 값의 길이가 32자보다 짧을 경우, 앞에 0을 추가하여 32자로 맞춥
            while (hexString.length() < 32) {
                hexString = "0" + hexString;
            }

            return hexString;
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 알고리즘이 지원되지 않을 경우 발생하는 예외를 처리
            throw new RuntimeException("SHA-256 알고리즘을 사용할 수 없습니다.", e);
        }
    }

    // SURL 수정
    @Transactional
    public RsData<SUrl> modify(Long id, SUrlRequest.ModifyRequest modifyRequest) {
        Optional<SUrl> sUrlOptional = sUrlRepository.findById(id);

        if (!sUrlOptional.isPresent()) {
            return RsData.of("400", "SURL을 찾을 수 없습니다.", null);
        }

        SUrl sUrl = sUrlOptional.get();

        // 새로운 originUrl이 있을 경우 shortUrl 다시 생성
        if (!sUrl.getOriginUrl().equals(modifyRequest.getOriginUrl())) {
            String newShortUrl = generateShortUrl(modifyRequest.getOriginUrl());
            sUrl.setShortUrl(newShortUrl);
        }

        sUrl.setOriginUrl(modifyRequest.getOriginUrl());
        sUrl.setIsActive(modifyRequest.getIsActive());
        sUrl.setRedirectCount(modifyRequest.getRedirectCount());

        sUrlRepository.save(sUrl);

        // 수정된 SUrl 객체 반환
        return RsData.of("200", "SURL 수정 완료", sUrl);
    }


    // SURL 단건 조회
    @Transactional(readOnly = true)
    public Optional<SUrl> findById(Long id) {
        return sUrlRepository.findById(id);
    }

    // SURL 목록 조회
    @Transactional(readOnly = true)
    public List<SUrl> findAll() {
        return sUrlRepository.findAll();
    }


    // SURL 삭제
    @Transactional
    public RsData deleteById(Long id) {
        Optional<SUrl> sUrlOptional = sUrlRepository.findById(id);

        if (!sUrlOptional.isPresent()) {
            return RsData.of("400", "해당 SURL을 찾을 수 없습니다.");
        }

        sUrlRepository.deleteById(id);

        return RsData.of("200", "SURL 삭제 완료", new SUrlResponse(sUrlOptional.get()));
    }

    // redirectCount 증가 메서드
    @Transactional
    public void increaseRedirectCount(SUrl sUrl) {

        sUrl.setRedirectCount(sUrl.getRedirectCount() + 1);

        sUrlRepository.save(sUrl);
    }


}
