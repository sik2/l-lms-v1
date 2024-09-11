package lab.loop.lms.domain.practice.service;

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

    public RsData<?> createPractice(ApiV1PracticesController.CreatePracticeRequest req) {

        if(practiceRepository.existsByTitle(req.getTitle())){
            return RsData.of("400-2", "해당 제목으로 이미 문제가 있습니다.",1);
        }

        Practice prc = Practice.builder()
                .member(rq.getMember())
                .title(req.getTitle())
                .content(req.getContent())
                .langType(req.getLangType())
                .isPublic(req.getIsPublic())
                .build();

        practiceRepository.save(prc);
        
        // 현재 RsData의 마지막 파라미터를 빼면 오류, null로 넣어도 오류 나서 일단 1로 넣어둠
        return RsData.of("200","문제 생성 성공",1);
    }

    public RsData<PracticeDto> getPracticeById(Long id) {

        Practice practice = practiceRepository.findById(id).orElseThrow(() -> new GlobalException("400-1", "해당 문제가 존재하지 않습니다."));

        return RsData.of("200","문제 상세 조회",new PracticeDto(practice));
    }

    public RsData<List<PracticeDto>> getPractices() {

        List<PracticeDto> practices = practiceRepository.findAll().stream().map(PracticeDto::new).toList();

        return RsData.of("200","문제 상세 조회",practices);
    }

    public RsData<?> deletePracticeById(Long id) {

        Practice practice = practiceRepository.findById(id).orElseThrow(() -> new GlobalException("400-1", "존재하지 않는 문제 입니다."));

        if(practice.getMember().getId() != rq.getMember().getId()){
            return RsData.of("400-2", "권한이 없습니다.",1);
        }

        practiceRepository.delete(practice);

        return RsData.of("200","해당 문제를 삭제 했습니다.",1);
    }

    public RsData<?> updatePractice(ApiV1PracticesController.UpdatePracticeRequest req) {

        Practice practice = practiceRepository.findById(req.getId()).orElseThrow(() -> new GlobalException("400-1", "존재하지 않는 문제 입니다."));

        if(practice.getMember().getId() != rq.getMember().getId()){
            return RsData.of("400-2", "권한이 없습니다.",1);
        }

        // 제목 변경시 중복 검사
        if(!practice.getTitle().equals(req.getTitle())){
            if(practiceRepository.existsByTitle(req.getTitle())){
                return RsData.of("400-2", "해당 제목으로 이미 문제가 있습니다.",1);
            }
        }

        Practice updatePractice = practice.toBuilder()
                .title(req.getTitle())
                .content(req.getContent())
                .langType(req.getLangType())
                .isPublic(req.getIsPublic())
                .build();

        practiceRepository.save(updatePractice);

        return RsData.of("200","해당 문제를 업데이트 했습니다.",new PracticeDto(updatePractice));
    }
}
