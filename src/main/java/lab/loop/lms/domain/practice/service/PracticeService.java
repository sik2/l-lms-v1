package lab.loop.lms.domain.practice.service;

import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.practice.controller.ApiV1PracticesController;
import lab.loop.lms.domain.practice.dto.PracticeDto;
import lab.loop.lms.domain.practice.entity.Practice;
import lab.loop.lms.domain.practice.repository.PracticeRepository;
import lab.loop.lms.global.exceptions.GlobalException;
import lab.loop.lms.global.rq.Rq;
import lab.loop.lms.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeService {

    private final PracticeRepository practiceRepository;
    private final Rq rq;

    public RsData<Practice> create(Member member, String title, String content, String langType, boolean isPublic) {

        if (practiceRepository.existsByTitle(title)) {
            return RsData.of("400", "해당 제목으로 이미 실습문제가 있습니다.");
        }

        Practice practice = Practice.builder()
                .member(member)
                .title(title)
                .content(content)
                .langType(langType)
                .isPublic(isPublic)
                .build();
        practiceRepository.save(practice);

        return RsData.of("200","실습문제 생성 성공", practice);
    }

    public RsData<PracticeDto> getPracticeById(Long id) {

        Practice practice = practiceRepository.findById(id).orElseThrow(() -> new GlobalException("400-1", "해당 문제가 존재하지 않습니다."));

        return RsData.of("200","문제 상세 조회",new PracticeDto(practice));
    }

    public RsData<List<PracticeDto>> getPractices() {

        List<PracticeDto> practices = practiceRepository.findAll().stream().map(PracticeDto::new).toList();

        return RsData.of("200","문제 상세 조회", practices);
    }

    public RsData<Practice> deletePracticeById(Long id) {

        Practice practice = practiceRepository.findById(id).orElseThrow(() -> new GlobalException("400-1", "존재하지 않는 문제 입니다."));

        if(practice.getMember().getId() != rq.getMember().getId()){
            return RsData.of("400-2", "권한이 없습니다.");
        }

        practiceRepository.delete(practice);

        return RsData.of("200","해당 문제를 삭제 했습니다.", practice);
    }

    public RsData<Practice> updatePractice(ApiV1PracticesController.UpdatePracticeRequest updatePracticeRequest, Member member) {

        Practice practice = practiceRepository.findById(updatePracticeRequest.getId()).orElseThrow(() -> new GlobalException("400-1", "존재하지 않는 문제 입니다."));

        if (practice.getMember().getId() != member.getId()) {
            return RsData.of("400-2", "권한이 없습니다.");
        }

        // 제목 변경시 중복 검사
        if(!practice.getTitle().equals(updatePracticeRequest.getTitle())){
            if(practiceRepository.existsByTitle(updatePracticeRequest.getTitle())){
                return RsData.of("400-2", "해당 제목으로 이미 문제가 있습니다.");
            }
        }

        Practice updatePractice = practice.toBuilder()
                .title(updatePracticeRequest.getTitle())
                .content(updatePracticeRequest.getContent())
                .langType(updatePracticeRequest.getLangType())
                .isPublic(updatePracticeRequest.getIsPublic())
                .build();

        practiceRepository.save(updatePractice);

        return RsData.of("200","해당 문제를 업데이트 했습니다.", practice);
    }
}
