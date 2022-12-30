package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.domain.dto.study.StudyDetailResponse;
import com.codewarts.noriter.article.domain.dto.study.StudyEditRequest;
import com.codewarts.noriter.article.domain.dto.study.StudyListResponse;
import com.codewarts.noriter.article.domain.dto.study.StudyPostRequest;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.article.repository.StudyRepository;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final MemberService memberService;
    private final ArticleRepository articleRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public void register(StudyPostRequest studyPostRequest, Long memberId) {
        Member member = memberService.findMember(memberId);
        Study study = studyPostRequest.toEntity(member);
        study.addHashtags(studyPostRequest.getHashtags());
        studyRepository.save(study);
    }

    public List<StudyListResponse> findList(StatusType status) {

        if (status == null) {
            return studyRepository.findAllStudy().stream()
                .map(StudyListResponse::new).collect(Collectors.toList());
        }
        return studyRepository.findStudyByCompleted(status).stream()
            .map(StudyListResponse::new).collect(Collectors.toList());
    }

    public StudyDetailResponse findDetail(Long id) {
        Study study = findStudy(id);
        return new StudyDetailResponse(study);
    }

    @Transactional
    public void delete(Long id, Long writerId) {
        memberService.findMember(writerId);
        Study study = findStudy(id);
        study.checkWriter(writerId);
        articleRepository.deleteByIdAndWriterId(id, writerId);
    }

    @Transactional
    public void updateCompletion(Long id, Long writerId, String status) {
        Study study = studyRepository.findByStudyIdAndWriterId(id, writerId)
            .orElseThrow(RuntimeException::new);

        if (status.equals(StatusType.COMPLETE.toString())) {
            study.completion();
        } else if (status.equals(StatusType.INCOMPLETE.toString())){
            study.incomplete();
        }
    }

    @Transactional
    public void update(Long id, StudyEditRequest request, Long writerId) {
        memberService.findMember(writerId);
        Study study = findStudy(id);
        study.checkWriter(writerId);
        study.update(request.getTitle(), request.getContent(), request.getHashtags());
    }

    public Study findStudy(Long id) {
        return studyRepository.findById(id).
            orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
    }
}
