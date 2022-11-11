package com.codewarts.noriter.article.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.common.domain.Member;
import com.codewarts.noriter.common.repository.MemberRepository;
import java.util.Arrays;
import java.util.List;
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
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("요청 받은 제목과 내용의 글을 저장한다")
    void post_title() {
    	// given
        Member member = Member.builder().nickname("phil").build();
        Member savedMember = memberRepository.save(member);
        List<String> hashtagsRequest = Arrays.asList("Spring", "Test");
        QuestionPostRequest request = new QuestionPostRequest("안녕 나는 질문이야", "헬륨가스 먹고 요렇게 됐지", hashtagsRequest);
        Long questionId = questionService.add(request, savedMember.getId());
    	// when
        Article question = articleRepository.findById(questionId)
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
        QuestionPostRequest request = new QuestionPostRequest("안녕 나는 질문이야", "헬륨가스 먹고 요렇게 됐지", hashtagsRequest);
        Long questionId = questionService.add(request, savedMember.getId());
        Article question = articleRepository.findById(questionId)
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
        Article question = articleRepository.findById(questionId)
            .orElseThrow(RuntimeException::new);
        // when
        List<Hashtag> hashtags = question.getHashtags();
        // then
        assertThat(hashtags).isEmpty();
    }
}
