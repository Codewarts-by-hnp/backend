//package com.codewarts.noriter.article.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.codewarts.noriter.article.domain.Article;
//import com.codewarts.noriter.article.domain.Hashtag;
//import com.codewarts.noriter.article.domain.Question;
//import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
//import com.codewarts.noriter.article.repository.ArticleRepository;
//import com.codewarts.noriter.common.domain.Member;
//import com.codewarts.noriter.common.repository.MemberRepository;
//import java.util.Arrays;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@Transactional
//@SpringBootTest
//class QuestionServiceTest {
//
//    @Autowired
//    private QuestionService questionService;
//    @Autowired
//    private ArticleRepository articleRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    @DisplayName("요청 받은 제목과 내용의 글을 저장한다")
//    void post_title() {
//        // given
//        Member member = Member.builder().nickname("phil").build();
//        Member savedMember = memberRepository.save(member);
//        List<String> hashtagsRequest = Arrays.asList("Spring", "Test");
//        QuestionPostRequest request = new QuestionPostRequest("안녕 나는 질문이야", "헬륨가스 먹고 요렇게 됐지",
//            hashtagsRequest);
//        Long questionId = questionService.add(request, savedMember.getId());
//        // when
//        Article question = articleRepository.findById(questionId)
//            .orElseThrow(RuntimeException::new);
//        // then
//        assertThat(question.getTitle()).isEqualTo(request.getTitle());
//        assertThat(question.getContent()).isEqualTo(request.getContent());
//        assertThat(question.isDeleted()).isFalse();
//    }
//
//    @Test
//    @DisplayName("요청 받은 해시태그와 함께 글을 저장한다")
//    void post_hashtags() {
//        // given
//        Member member = Member.builder().nickname("phil").build();
//        Member savedMember = memberRepository.save(member);
//        List<String> hashtagsRequest = Arrays.asList("Spring", "Test");
//        QuestionPostRequest request = new QuestionPostRequest("안녕 나는 질문이야", "헬륨가스 먹고 요렇게 됐지",
//            hashtagsRequest);
//        Long questionId = questionService.add(request, savedMember.getId());
//        Article question = articleRepository.findById(questionId)
//            .orElseThrow(RuntimeException::new);
//        // when
//        List<Hashtag> hashtags = question.getHashtags();
//        // then
//        assertThat(hashtags).hasSize(2);
//        assertThat(hashtags.get(0).getContent()).isEqualTo("Spring");
//        assertThat(hashtags.get(0).getArticle()).isEqualTo(question);
//        assertThat(hashtags.get(0).isDeleted()).isFalse();
//        assertThat(hashtags.get(1).getContent()).isEqualTo("Test");
//        assertThat(hashtags.get(1).getArticle()).isEqualTo(question);
//        assertThat(hashtags.get(1).isDeleted()).isFalse();
//    }
//
//    @Test
//    @DisplayName("요청 받은 해시태그가 NULL 인 경우")
//    void post_hashtag_null() {
//        // given
//        Member member = Member.builder().nickname("phil").build();
//        Member savedMember = memberRepository.save(member);
//        QuestionPostRequest request = new QuestionPostRequest("안녕 나는 질문이야", "헬륨가스 먹고 요렇게 됐지", null);
//        Long questionId = questionService.add(request, savedMember.getId());
//        Article question = articleRepository.findById(questionId)
//            .orElseThrow(RuntimeException::new);
//        // when
//        List<Hashtag> hashtags = question.getHashtags();
//        // then
//        assertThat(hashtags).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Question 글을 전부 조회 한다")
//    void findAll() {
//        // given
//        Member member = Member.builder().nickname("phil").build();
//        Member savedMember = memberRepository.save(member);
//        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "헬륨가스 먹고 요렇게 됐지", null);
//        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "헬륨가스 먹고 요렇게 됐지", null);
//        Long questionId1 = questionService.add(request1, savedMember.getId());
//        Long questionId2 = questionService.add(request2, savedMember.getId());
//        // when
//        List<Question> questions = articleRepository.findAllQuestion();
//        // then
//        assertThat(questions).hasSize(2);
//        assertThat(questions.get(0).getId()).isEqualTo(questionId1);
//        assertThat(questions.get(1).getId()).isEqualTo(questionId2);
//    }
//
//    @Test
//    @DisplayName("해결된 질문 글만 조회 한다")
//    void findCompleteQuestion() {
//        // given
//        Member member = Member.builder().nickname("phil").build();
//        Member savedMember = memberRepository.save(member);
//        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "해결했지", null);
//        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "해결했지", null);
//        QuestionPostRequest request3 = new QuestionPostRequest("안녕 나는 질문3", "해결되지 못했지", null);
//        Long questionId1 = questionService.add(request1, savedMember.getId());
//        Long questionId2 = questionService.add(request2, savedMember.getId());
//        Long questionId3 = questionService.add(request3, savedMember.getId());
//        Question question1 = (Question)articleRepository.findById(questionId1).get();
//        Question question2 = (Question)articleRepository.findById(questionId2).get();
//        question1.changeStatus(true);
//        question2.changeStatus(true);
//        // when
//        List<Question> questions = articleRepository.findQuestionByCompleted(true);
//        // then
//        assertThat(questions).hasSize(2);
//        assertThat(questions.get(0).getId()).isEqualTo(questionId1);
//        assertThat(questions.get(1).getId()).isEqualTo(questionId2);
//    }
//
//    @Test
//    @DisplayName("미해결 질문 글만 조회 한다")
//    void findIncompleteQuestion() {
//        // given
//        Member member = Member.builder().nickname("phil").build();
//        Member savedMember = memberRepository.save(member);
//        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "해결되지 못했지", null);
//        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "해결했지", null);
//        QuestionPostRequest request3 = new QuestionPostRequest("안녕 나는 질문3", "해결되지 못했지", null);
//        Long questionId1 = questionService.add(request1, savedMember.getId());
//        Long questionId2 = questionService.add(request2, savedMember.getId());
//        Long questionId3 = questionService.add(request3, savedMember.getId());
//        Question question2 = (Question)articleRepository.findById(questionId2).get();
//        question2.changeStatus(true);
//        // when
//        List<Question> questions = articleRepository.findQuestionByCompleted(false);
//        // then
//        assertThat(questions).hasSize(2);
//        assertThat(questions.get(0).getId()).isEqualTo(questionId1);
//        assertThat(questions.get(1).getId()).isEqualTo(questionId3);
//    }
//}
