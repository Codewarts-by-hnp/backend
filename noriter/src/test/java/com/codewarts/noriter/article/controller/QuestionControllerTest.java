//package com.codewarts.noriter.article.controller;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.codewarts.noriter.article.domain.Question;
//import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
//import com.codewarts.noriter.article.repository.ArticleRepository;
//import com.codewarts.noriter.article.service.QuestionService;
//import com.codewarts.noriter.common.domain.Member;
//import com.codewarts.noriter.common.repository.MemberRepository;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//@Transactional
//@SpringBootTest
//@AutoConfigureMockMvc
//class QuestionControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ArticleRepository articleRepository;
//    @Autowired
//    private QuestionService questionService;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    @DisplayName("미해결 질문 글 조회")
//    void findIncompleteQuestion() throws Exception {
//        // given
//        Member member = Member.builder().nickname("phil").build();
//        Member savedMember = memberRepository.save(member);
//        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "해결되지 못했지", null);
//        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "해결했지", null);
//        QuestionPostRequest request3 = new QuestionPostRequest("안녕 나는 질문3", "해결되지 못했지", null);
//        Long questionId1 = questionService.add(request1, savedMember.getId());
//        Long questionId2 = questionService.add(request2, savedMember.getId());
//        Long questionId3 = questionService.add(request3, savedMember.getId());
//        Question question2 = (Question) articleRepository.findById(questionId2).get();
//        question2.changeStatus(true);
//
//        // when
//        mockMvc.perform(get("/community/question?completion=false")
//                .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.size()", Matchers.is(2)))
//            .andExpect(jsonPath("$[0].id").value(questionId1))
//            .andExpect(jsonPath("$[1].id").value(questionId3))
//            .andDo(print());
//    }
//
//    @Test
//    @DisplayName("해결된 질문 글 조회")
//    void findCompleteQuestion() throws Exception {
//        // given
//        Member member = Member.builder().nickname("phil").build();
//        Member savedMember = memberRepository.save(member);
//        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "해결되지 못했지", null);
//        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "해결했지", null);
//        QuestionPostRequest request3 = new QuestionPostRequest("안녕 나는 질문3", "해결되지 못했지", null);
//        questionService.add(request1, savedMember.getId());
//        questionService.add(request3, savedMember.getId());
//        Long questionId2 = questionService.add(request2, savedMember.getId());
//        Question question2 = (Question) articleRepository.findById(questionId2).get();
//        question2.changeStatus(true);
//
//        // expected
//        mockMvc.perform(get("/community/question?completion=true")
//                .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.size()", Matchers.is(1)))
//            .andExpect(jsonPath("$[0].id").value(questionId2))
//            .andDo(print());
//    }
//
//    @Test
//    @DisplayName("모든 질문 글 조회")
//    void findAllQuestion() throws Exception {
//        // given
//        Member member = Member.builder().nickname("phil").build();
//        Member savedMember = memberRepository.save(member);
//        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "해결되지 못했지", null);
//        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "해결했지", null);
//        QuestionPostRequest request3 = new QuestionPostRequest("안녕 나는 질문3", "해결되지 못했지", null);
//        Long questionId1 = questionService.add(request1, savedMember.getId());
//        Long questionId2 = questionService.add(request2, savedMember.getId());
//        Long questionId3 = questionService.add(request3, savedMember.getId());
//        Question question2 = (Question) articleRepository.findById(questionId2).get();
//        question2.changeStatus(true);
//
//        // expected
//        mockMvc.perform(get("/community/question")
//                .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.size()", Matchers.is(3)))
//            .andExpect(jsonPath("$[0].id").value(questionId1))
//            .andExpect(jsonPath("$[1].id").value(questionId2))
//            .andExpect(jsonPath("$[2].id").value(questionId3))
//            .andDo(print());
//    }
//
//}
