package com.codewarts.noriter.article.unit.service;

import static com.codewarts.noriter.exception.type.ArticleExceptionType.ALREADY_CHANGED_STATUS;
import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_FOUND;
import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_MATCHED_WRITER;
import static com.codewarts.noriter.exception.type.ArticleExceptionType.DELETED_ARTICLE;
import static com.codewarts.noriter.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.dto.article.ArticleListResponse;
import com.codewarts.noriter.article.dto.gathering.GatheringCreateRequest;
import com.codewarts.noriter.article.dto.gathering.GatheringDetailResponse;
import com.codewarts.noriter.article.dto.gathering.GatheringUpdateRequest;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.article.service.GatheringService;
import com.codewarts.noriter.auth.oauth.type.ResourceServer;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.MemberExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.repository.MemberRepository;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class GatheringServiceTest {

    @Autowired
    GatheringService gatheringService;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    MemberRepository memberRepository;

    private final Long NON_EXIST_INDEX = Long.MAX_VALUE;
    private Long writerId;

    @BeforeEach
    void setUp() {
        Member member = new Member(ResourceServer.GITHUB, 1L, "admin", "admin@code.com", null,
            null);
        writerId = memberRepository.save(member).getId();
    }

    @DisplayName("스터디게시판 글을 생성한다.")
    @Test
    void create() {
        // given
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목", "테스트 내용", null);
        Long articleId = gatheringService.create(request, writerId);

        // when
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new GlobalNoriterException(ARTICLE_NOT_FOUND));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(article.getId()).isEqualTo(articleId);
            softAssertions.assertThat(article.getId()).isEqualTo(articleId);
            softAssertions.assertThat(article.getTitle()).isEqualTo(request.getTitle());
            softAssertions.assertThat(article.getContent()).isEqualTo(request.getContent());
            softAssertions.assertThat(article.getWriter().getId()).isEqualTo(writerId);
        });
    }

    @DisplayName("존재하지 않는 회원이 스터디게시판 글을 생성 요청할 경우 예외를 발생시킨다.")
    @Test
    void createWithWrongWriterIdThenThrow() {
        // when
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목", "테스트 내용", null);

        // expected
        assertThatThrownBy(() -> gatheringService.create(request, NON_EXIST_INDEX))
            .message()
            .isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @DisplayName("스터디게시판 글을 상세 조회한다.")
    @Test
    void findDetail() {
        // given
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목", "테스트 내용", null);
        Long articleId = gatheringService.create(request, writerId);

        // when
        GatheringDetailResponse response = gatheringService.findDetail(articleId, writerId);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(response.getId()).isEqualTo(articleId);
            softAssertions.assertThat(response.getTitle()).isEqualTo(request.getTitle());
            softAssertions.assertThat(response.getContent()).isEqualTo(request.getContent());
            softAssertions.assertThat(response.getWriter().getId()).isEqualTo(writerId);
        });
    }

    @DisplayName("존재하지 않는 스터디게시판 글을 상세 조회 요청할 경우 예외를 발생시킨다.")
    @Test
    void findDetailWithWrongArticleIdThenThrow() {
        // expected
        assertThatThrownBy(() -> gatheringService.findDetail(NON_EXIST_INDEX, writerId))
            .message()
            .isEqualTo(ARTICLE_NOT_FOUND.getErrorMessage());
    }

    @DisplayName("삭제된 스터디게시판 글을 상세 조회 요청하면 예외를 발생시킨다.")
    @Test
    void findDetailWithAlreadyDeletedArticleIdThenThrow() {
        // when
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목", "테스트 내용",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);
        gatheringService.delete(articleId, writerId);

        // expected
        assertThatThrownBy(() -> gatheringService.findDetail(articleId, writerId))
            .message()
            .isEqualTo(DELETED_ARTICLE.getErrorMessage());
    }

    @DisplayName("스터디게시판 글을 전체 조회한다.")
    @Test
    void findList() {
        // given
        GatheringCreateRequest request1 = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        gatheringService.create(request1, writerId);

        GatheringCreateRequest request2 = new GatheringCreateRequest("테스트 제목2", "테스트 내용2",
            List.of("해시태그1", "해시태그2"));
        gatheringService.create(request2, writerId);

        // when
        List<ArticleListResponse> list = gatheringService.findList(null, null);

        // then
        assertThat(list).hasSize(2);
    }

    @DisplayName("스터디게시판 모집이 완료되지 않은 글을 조회한다.")
    @Test
    void findCompletedList() {
        // given
        GatheringCreateRequest request1 = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        gatheringService.create(request1, writerId);

        GatheringCreateRequest request2 = new GatheringCreateRequest("테스트 제목2", "테스트 내용2",
            List.of("해시태그1", "해시태그2"));
        gatheringService.create(request2, writerId);

        // when
        List<ArticleListResponse> list = gatheringService.findList(StatusType.INCOMPLETE, null);

        // then
        assertThat(list).hasSize(2);
    }

    @DisplayName("삭제되지 않은 스터디게시판 글을 전체 조회한다.")
    @Test
    void findListWithoutDeletedArticle() {
        // given
        GatheringCreateRequest request1 = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        gatheringService.create(request1, writerId);

        GatheringCreateRequest request2 = new GatheringCreateRequest("테스트 제목2", "테스트 내용2",
            List.of("해시태그1", "해시태그2"));
        gatheringService.create(request2, writerId);

        GatheringCreateRequest request3 = new GatheringCreateRequest("테스트 제목3", "테스트 내용3",
            List.of("해시태그1", "해시태그2"));
        Long deleteArticleId = gatheringService.create(request3, writerId);

        gatheringService.delete(deleteArticleId, writerId);

        // when
        List<ArticleListResponse> list = gatheringService.findList(null,null);

        // then
        assertThat(list).hasSize(2);
    }

    @DisplayName("스터디게시판 글을 수정한다.")
    @Test
    void update() {
        // given
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        Long articleId =gatheringService.create(request, writerId);

        GatheringUpdateRequest updateRequest = new GatheringUpdateRequest("수정된 제목", "수정된 내용",
            List.of("수정된 해시태그"));

        // when
        gatheringService.update(articleId, updateRequest, writerId);
        GatheringDetailResponse response = gatheringService.findDetail(articleId, writerId);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(response.getId()).isEqualTo(articleId);
            softAssertions.assertThat(response.getTitle()).isEqualTo(updateRequest.getTitle());
            softAssertions.assertThat(response.getContent()).isEqualTo(updateRequest.getContent());
            softAssertions.assertThat(response.getHashtags()).hasSize(updateRequest.getHashtags().size());
            softAssertions.assertThat(response.getWriter().getId()).isEqualTo(writerId);
        });
    }

    @DisplayName("존재하지 않는 스터디게시판 글을 수정 요청할 경우 예외를 발생시킨다.")
    @Test
    void updateWithWrongArticleIdThenThrow() {
        // when
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목", "테스트 내용",
            List.of("해시태그1", "해시태그2"));
        gatheringService.create(request, writerId);

        GatheringUpdateRequest updateRequest = new GatheringUpdateRequest("수정된 제목", "수정된 내용",
            List.of("수정된 해시태그"));

        // expected
        assertThatThrownBy(
            () -> gatheringService.update(NON_EXIST_INDEX, updateRequest, writerId))
            .message()
            .isEqualTo(ARTICLE_NOT_FOUND.getErrorMessage());
    }

    @DisplayName("존재하지 않는 회원이 스터디게시판 글을 수정 요청할 경우 예외를 발생시킨다.")
    @Test
    void updateWithWrongWriterIdThenThrow() {
        // when
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목", "테스트 내용",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);

        GatheringUpdateRequest updateRequest = new GatheringUpdateRequest("수정된 제목", "수정된 내용",
            List.of("수정된 해시태그"));

        // expected
        assertThatThrownBy(
            () -> gatheringService.update(articleId, updateRequest, NON_EXIST_INDEX))
            .message()
            .isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @DisplayName("작성자가 아닌 회원이 스터디게시판 글을 수정 요청할 경우 예외를 발생시킨다.")
    @Test
    void updateWithNotMatchedWriterThenThrow() {
        // when
        Member newMember = new Member(ResourceServer.GITHUB, 1L, "admin", "admin@code.com", null,
            null);
        Long wrongWriterId = memberRepository.save(newMember).getId();

        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목", "테스트 내용",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);


        GatheringUpdateRequest updateRequest = new GatheringUpdateRequest("수정된 제목", "수정된 내용",
            List.of("수정된 해시태그"));

        // expected
        assertThatThrownBy(
            () -> gatheringService.update(articleId, updateRequest, wrongWriterId))
            .message()
            .isEqualTo(ARTICLE_NOT_MATCHED_WRITER.getErrorMessage());
    }

    @DisplayName("삭제된 스터디게시판 글을 수정 요청할 경우 예외를 발생시킨다.")
    @Test
    void updateWithAlreadyDeletedArticleIdThenThrow() {
        // when
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목", "테스트 내용",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);

        gatheringService.delete(articleId, writerId);
        GatheringUpdateRequest updateRequest = new GatheringUpdateRequest("수정된 제목", "수정된 내용",
            List.of("수정된 해시태그"));

        // expected
        assertThatThrownBy(
            () -> gatheringService.update(articleId, updateRequest, writerId))
            .message()
            .isEqualTo(DELETED_ARTICLE.getErrorMessage());
    }

    @DisplayName("스터디게시판 글을 삭제한다.")
    @Test
    void delete() {
        // given
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);

        // when
        gatheringService.delete(articleId, writerId);
        Article article = articleRepository.findById(articleId).get();

        // then
        assertThat(article.getDeleted()).isNotNull();
    }

    @DisplayName("존재하지 않는 회원이 스터디게시판 글을 삭제 요청할 경우 예외를 발생시킨다.")
    @Test
    void deleteWithWrongWriterIdThenThrow() {
        // when
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);

        // expected
        assertThatThrownBy(() -> gatheringService.delete(articleId, NON_EXIST_INDEX))
            .message()
            .isEqualTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorMessage());
    }

    @DisplayName("작성자가 아닌 회원이 스터디게시판 글을 삭제 요청할 경우 예외를 발생시킨다.")
    @Test
    void deleteWithNotMatchedWriterIdThenThrow() {
        // when
        Member newMember = new Member(ResourceServer.GITHUB, 1L, "admin", "admin@code.com", null,
            null);
        Long wrongWriterId = memberRepository.save(newMember).getId();

        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);

        // expected
        assertThatThrownBy(() -> gatheringService.delete(articleId, wrongWriterId))
            .message()
            .isEqualTo(ArticleExceptionType.ARTICLE_NOT_MATCHED_WRITER.getErrorMessage());
    }

    @DisplayName("존재하지 않은 스터디게시판 글을 삭제 요청할 경우 예외를 발생시킨다.")
    @Test
    void deleteWithWrongArticleIdThenThrow() {
        // when
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        gatheringService.create(request, writerId);

        // expected
        assertThatThrownBy(() -> gatheringService.delete(NON_EXIST_INDEX, writerId))
            .message()
            .isEqualTo(ARTICLE_NOT_FOUND.getErrorMessage());
    }

    @DisplayName("이미 삭제된 스터디게시판 글을 삭제 요청할 경우 예외를 발생시킨다.")
    @Test
    void deleteWithAlreadyDeletedArticleIdThenThrow() {
        // given
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);

        // when
        gatheringService.delete(articleId, writerId);

        // expected
        assertThatThrownBy(() -> gatheringService.delete(articleId, writerId))
            .message()
            .isEqualTo(DELETED_ARTICLE.getErrorMessage());
    }

    @DisplayName("스터디게시판의 상태를 변경한다.")
    @Test
    void updateCompletion() {
        // given
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);

        // when
        gatheringService.updateCompletion(articleId, writerId, StatusType.COMPLETE);
        GatheringDetailResponse response = gatheringService.findDetail(articleId, writerId);

        // expected
        assertThat(response.getStatus()).isEqualTo(StatusType.COMPLETE);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(response.getId()).isEqualTo(articleId);
            softAssertions.assertThat(response.getTitle()).isEqualTo(request.getTitle());
            softAssertions.assertThat(response.getContent()).isEqualTo(request.getContent());
            softAssertions.assertThat(response.getHashtags()).isEqualTo(request.getHashtags());
            softAssertions.assertThat(response.getStatus()).isEqualTo(StatusType.COMPLETE);
        });
    }

    @DisplayName("존재하지 않는 스터디게시판 상태 변경을 요청할 경우 예외를 발생시킨다.")
    @Test
    void updateCompletionWithWrongArticleIdThenThrow() {
        // given
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        gatheringService.create(request, writerId);

        // expected
        assertThatThrownBy(
            () -> gatheringService.updateCompletion(NON_EXIST_INDEX, writerId, StatusType.COMPLETE))
            .message()
            .isEqualTo(ARTICLE_NOT_FOUND.getErrorMessage());
    }

    @DisplayName("이미 삭제된 스터디게시판 상태 변경을 요청할 경우 예외를 발생시킨다.")
    @Test
    void updateCompletionWithAlreadyDeletedArticleIdThenThrow() {
        // given
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);
        gatheringService.delete(articleId, writerId);

        // expected
        assertThatThrownBy(
            () -> gatheringService.updateCompletion(articleId, writerId, StatusType.COMPLETE))
            .message()
            .isEqualTo(DELETED_ARTICLE.getErrorMessage());
    }

    @DisplayName("존재하지 않은 회원이 스터디게시판 상태 변경을 요청할 경우 예외를 발생시킨다.")
    @Test
    void updateCompletionWithAlreadyUpdatedStatusThenThrow() {
        // given
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);

        // expected
        assertThatThrownBy(
            () -> gatheringService.updateCompletion(articleId, NON_EXIST_INDEX, StatusType.INCOMPLETE))
            .message()
            .isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @DisplayName("스터디게시판 상태 변경을 같은상태로 요청할 경우 예외를 발생시킨다.")
    @Test
    void updateCompletionWithWrongWriterIdThenThrow() {
        // given
        GatheringCreateRequest request = new GatheringCreateRequest("테스트 제목1", "테스트 내용1",
            List.of("해시태그1", "해시태그2"));
        Long articleId = gatheringService.create(request, writerId);

        // expected
        assertThatThrownBy(
            () -> gatheringService.updateCompletion(articleId, writerId, StatusType.INCOMPLETE))
            .message()
            .isEqualTo(ALREADY_CHANGED_STATUS.getErrorMessage());
    }
}
