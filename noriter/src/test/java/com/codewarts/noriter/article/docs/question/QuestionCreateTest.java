package com.codewarts.noriter.article.docs.question;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.DATE;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
import com.codewarts.noriter.article.domain.dto.question.QuestionUpdateRequest;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.article.service.QuestionService;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import com.codewarts.noriter.common.domain.Member;
import com.codewarts.noriter.common.repository.MemberRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.stream.Collectors;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class QuestionCreateTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private MemberRepository memberRepository;

    @LocalServerPort
    int port;

    @Autowired
    protected JwtProvider jwtProvider;

    protected RequestSpecification documentationSpec;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        documentationSpec = new RequestSpecBuilder()
            .addFilter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        prettyPrint(),
                        removeHeaders(HOST, CONTENT_LENGTH))
                    .withResponseDefaults(
                        prettyPrint(),
                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING,
                            "Keep-Alive",
                            HttpHeaders.VARY))
            )
            .build();
    }

    @Test
    @DisplayName("미해결 질문 글 조회")
    void findIncompleteQuestion() throws Exception {
        // given
        Member member = Member.builder().nickname("phil").build();
        Member savedMember = memberRepository.save(member);
        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "해결되지 못했지", null);
        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "해결했지", null);
        QuestionPostRequest request3 = new QuestionPostRequest("안녕 나는 질문3", "해결되지 못했지", null);
        Long questionId1 = questionService.add(request1, savedMember.getId());
        Long questionId2 = questionService.add(request2, savedMember.getId());
        Long questionId3 = questionService.add(request3, savedMember.getId());
        Question question2 = (Question) articleRepository.findById(questionId2).get();
        question2.changeStatus(true);

        // when
        mockMvc.perform(get("/community/question?completion=false")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", Matchers.is(4)))
            .andExpect(jsonPath("$[2].id").value(questionId1))
            .andExpect(jsonPath("$[3].id").value(questionId3))
            .andDo(print());
    }

    @Test
    @DisplayName("해결된 질문 글 조회")
    void findCompleteQuestion() throws Exception {
        // given
        Member member = Member.builder().nickname("phil").build();
        Member savedMember = memberRepository.save(member);
        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "해결되지 못했지", null);
        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "해결했지", null);
        QuestionPostRequest request3 = new QuestionPostRequest("안녕 나는 질문3", "해결되지 못했지", null);
        questionService.add(request1, savedMember.getId());
        questionService.add(request3, savedMember.getId());
        Long questionId2 = questionService.add(request2, savedMember.getId());
        Question question2 = (Question) articleRepository.findById(questionId2).get();
        question2.changeStatus(true);

        // expected
        mockMvc.perform(get("/community/question?completion=true")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", Matchers.is(2)))
            .andExpect(jsonPath("$[1].id").value(questionId2))
            .andDo(print());
    }

    @Test
    @DisplayName("모든 질문 글 조회")
    void findAllQuestion() throws Exception {
        // given
        Member member = Member.builder().nickname("phil").build();
        Member savedMember = memberRepository.save(member);
        QuestionPostRequest request1 = new QuestionPostRequest("안녕 나는 질문1", "해결되지 못했지", null);
        QuestionPostRequest request2 = new QuestionPostRequest("안녕 나는 질문2", "해결했지", null);
        QuestionPostRequest request3 = new QuestionPostRequest("안녕 나는 질문3", "해결되지 못했지", null);
        Long questionId1 = questionService.add(request1, savedMember.getId());
        Long questionId2 = questionService.add(request2, savedMember.getId());
        Long questionId3 = questionService.add(request3, savedMember.getId());
        Question question2 = (Question) articleRepository.findById(questionId2).get();
        question2.changeStatus(true);

        // expected
        mockMvc.perform(get("/community/question")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", Matchers.is(6)))
            .andExpect(jsonPath("$[3].id").value(questionId1))
            .andExpect(jsonPath("$[4].id").value(questionId2))
            .andExpect(jsonPath("$[5].id").value(questionId3))
            .andDo(print());
    }

    @Test
    void 제목을_수정한다() {

        Question question = articleRepository.findQuestionById(6L)
            .orElseThrow(RuntimeException::new);
        String accessToken = jwtProvider.issueAccessToken(question.getWriter().getId());
        List<String> hashtag = question.getHashtags().stream().map(Hashtag::getContent)
            .collect(Collectors.toList());
        QuestionUpdateRequest updateRequest = new QuestionUpdateRequest("수정된 제목", question.getContent(), hashtag);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(updateRequest)

            .when()
            .put("/community/question/6")

            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
