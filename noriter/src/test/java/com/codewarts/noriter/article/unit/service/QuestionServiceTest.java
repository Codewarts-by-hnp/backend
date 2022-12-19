package com.codewarts.noriter.article.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
import com.codewarts.noriter.article.domain.dto.question.QuestionUpdateRequest;
import com.codewarts.noriter.article.repository.QuestionRepository;
import com.codewarts.noriter.article.service.QuestionService;
import com.codewarts.noriter.common.domain.Member;
import com.codewarts.noriter.common.repository.MemberRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("요청 받은 제목과 내용의 글을 저장한다")
    void post_title() {
        // given
        Member member = Member.builder().nickname("phil").build();
        Member savedMember = memberRepository.save(member);
        List<String> hashtagsRequest = Arrays.asList("Spring", "Test");
        QuestionPostRequest request = new QuestionPostRequest("안녕 나는 질문이야", "헬륨가스 먹고 요렇게 됐지",
            hashtagsRequest);
        Long questionId = questionService.add(request, savedMember.getId());
        // when
        Article question = questionRepository.findById(questionId)
            .orElseThrow(RuntimeException::new);
        // then
        assertThat(question.getTitle()).isEqualTo(request.getTitle());
        assertThat(question.getContent()).isEqualTo(request.getContent());
        assertThat(question.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("요청 받은 해시태그와 함께 글을 저장한다")
    void post_hashtags() {
        // given
        Member member = Member.builder().nickname("phil").build();
        Member savedMember = memberRepository.save(member);
        List<String> hashtagsRequest = Arrays.asList("Spring", "Test");
        QuestionPostRequest request = new QuestionPostRequest("안녕 나는 질문이야", "헬륨가스 먹고 요렇게 됐지",
            hashtagsRequest);
        Long questionId = questionService.add(request, savedMember.getId());
        Article question = questionRepository.findById(questionId)
            .orElseThrow(RuntimeException::new);
        // when
        List<Hashtag> hashtags = question.getHashtags();
        // then
        assertThat(hashtags).hasSize(2);
        assertThat(hashtags.get(0).getContent()).isEqualTo("Spring");
        assertThat(hashtags.get(0).getArticle()).isEqualTo(question);
        assertThat(hashtags.get(0).isDeleted()).isFalse();
        assertThat(hashtags.get(1).getContent()).isEqualTo("Test");
        assertThat(hashtags.get(1).getArticle()).isEqualTo(question);
        assertThat(hashtags.get(1).isDeleted()).isFalse();
    }

    @Test
    @DisplayName("요청 받은 해시태그가 NULL 인 경우")
    void post_hashtag_null() {
        // given
        Member member = Member.builder().nickname("phil").build();
        Member savedMember = memberRepository.save(member);
        QuestionPostRequest request = new QuestionPostRequest("안녕 나는 질문이야", "헬륨가스 먹고 요렇게 됐지", null);
        Long questionId = questionService.add(request, savedMember.getId());
        Article question = questionRepository.findById(questionId)
            .orElseThrow(RuntimeException::new);
        // when
        List<Hashtag> hashtags = question.getHashtags();
        // then
        assertThat(hashtags).isEmpty();
    }

    @Test
    @DisplayName("Question 글을 전부 조회 한다")
    void findAll() {
        // given
        Member member = Member.builder().nickname("phil").build();
        Member savedMember = memberRepository.save(member);
        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "헬륨가스 먹고 요렇게 됐지", null);
        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "헬륨가스 먹고 요렇게 됐지", null);
        questionService.add(request1, savedMember.getId());
        questionService.add(request2, savedMember.getId());
        // when
        List<Question> questions = questionRepository.findAllQuestion();
        // then
        assertThat(questions).hasSize(5);
    }

    @Test
    @DisplayName("해결된 질문 글만 조회 한다")
    void findCompleteQuestion() {
        // given
        Member member = Member.builder().nickname("phil").build();
        Member savedMember = memberRepository.save(member);
        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "해결했지", null);
        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "해결했지", null);
        QuestionPostRequest request3 = new QuestionPostRequest("안녕 나는 질문3", "해결되지 못했지", null);
        Long questionId1 = questionService.add(request1, savedMember.getId());
        Long questionId2 = questionService.add(request2, savedMember.getId());
        questionService.add(request3, savedMember.getId());
        Question question1 = questionRepository.findById(questionId1).get();
        Question question2 = questionRepository.findById(questionId2).get();
        question1.changeStatus(true);
        question2.changeStatus(true);
        // when
        List<Question> questions = questionRepository.findQuestionByCompleted(true);
        // then
        assertThat(questions).hasSize(3);
    }

    @Test
    @DisplayName("미해결 질문 글만 조회 한다")
    void findIncompleteQuestion() {
        // given
        Member member = Member.builder().nickname("phil").build();
        Member savedMember = memberRepository.save(member);
        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "해결되지 못했지", null);
        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "해결했지", null);
        QuestionPostRequest request3 = new QuestionPostRequest("안녕 나는 질문3", "해결되지 못했지", null);
        questionService.add(request1, savedMember.getId());
        Long questionId2 = questionService.add(request2, savedMember.getId());
        questionService.add(request3, savedMember.getId());
        Question question2 = questionRepository.findById(questionId2).get();
        question2.changeStatus(true);
        // when
        List<Question> questions = questionRepository.findQuestionByCompleted(false);
        // then
        assertThat(questions).hasSize(4);
    }

    @Test
    void 제목을_수정한다() {
        // given
        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(RuntimeException::new);
        List<String> hashtags = question.getHashtags().stream().map(Hashtag::getContent).collect(
            Collectors.toList());
        QuestionUpdateRequest request = new QuestionUpdateRequest("수정된 제목", question.getContent(), hashtags);

        // when
        questionService.update(question.getId(), question.getWriter().getId(), request);
        List<String> updatedHashtag = question.getHashtags().stream().map(Hashtag::getContent)
            .collect(Collectors.toList());

        // then
        assertThat(question.getTitle()).isEqualTo(request.getTitle());
        assertThat(question.getContent()).isEqualTo(request.getContent());
        assertThat(updatedHashtag).isEqualTo(request.getHashtag());
    }

    @Test
    void 내용을_수정한다() {
        // given
        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(RuntimeException::new);
        List<String> hashtags = question.getHashtags().stream().map(Hashtag::getContent).collect(
            Collectors.toList());
        QuestionUpdateRequest request = new QuestionUpdateRequest(question.getTitle(), "수정된 내용", hashtags);

        // when
        questionService.update(question.getId(), question.getWriter().getId(), request);
        List<String> updatedHashtag = question.getHashtags().stream().map(Hashtag::getContent)
            .collect(Collectors.toList());
        // then
        assertThat(question.getTitle()).isEqualTo(request.getTitle());
        assertThat(question.getContent()).isEqualTo(request.getContent());
        assertThat(updatedHashtag).isEqualTo(request.getHashtag());
    }

    @Test
    void 해시태그를_수정한다() {
        // given
        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(RuntimeException::new);
        List<String> hashtags = List.of("해시태그1", "해시태그2");
        QuestionUpdateRequest request = new QuestionUpdateRequest(question.getTitle(), question.getContent(), hashtags);

        questionService.update(question.getId(), question.getWriter().getId(), request);
        // when
        List<String> updatedHashtag = question.getHashtags().stream().map(Hashtag::getContent)
            .collect(Collectors.toList());
        // then
        assertThat(question.getTitle()).isEqualTo(request.getTitle());
        assertThat(question.getContent()).isEqualTo(request.getContent());
        assertThat(updatedHashtag).isEqualTo(request.getHashtag());
    }

    @Test
    void 전부_수정한다() {
        // given
        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(RuntimeException::new);
        List<String> hashtags = List.of("해시태그1", "해시태그2");
        QuestionUpdateRequest request = new QuestionUpdateRequest("수정된 제목", "수정된 내용", hashtags);

        questionService.update(question.getId(), question.getWriter().getId(), request);

        // when
        List<String> updatedHashtag = question.getHashtags().stream().map(Hashtag::getContent)
            .collect(Collectors.toList());
        // then
        assertThat(question.getTitle()).isEqualTo(request.getTitle());
        assertThat(question.getContent()).isEqualTo(request.getContent());
        assertThat(updatedHashtag).isEqualTo(request.getHashtag());
    }
    @Test
    void 해결_여부를_수정한다() {
        // given
        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(RuntimeException::new);
        boolean completed = question.isCompleted();

        // when
        questionService.updateComplete(question.getId(), question.getWriter().getId(), !completed);

        // then
        assertThat(question.isCompleted()).isEqualTo(!completed);

    }
}
