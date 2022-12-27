package com.codewarts.noriter.article.docs.question;

import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_MATCHED_WRITER;
import static com.codewarts.noriter.exception.type.AuthExceptionType.EMPTY_ACCESS_TOKEN;
import static com.codewarts.noriter.exception.type.AuthExceptionType.TAMPERED_ACCESS_TOKEN;
import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static com.codewarts.noriter.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.DATE;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.dto.question.QuestionUpdateRequest;
import com.codewarts.noriter.article.repository.QuestionRepository;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionEditTest {

    @LocalServerPort
    int port;
    @Autowired
    private QuestionRepository questionRepository;
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
    void 제목을_수정한다() {

        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        String accessToken = jwtProvider.issueAccessToken(question.getWriter().getId());
        List<String> hashtag = question.getHashtags().stream().map(Hashtag::getContent)
            .collect(Collectors.toList());
        QuestionUpdateRequest updateRequest = new QuestionUpdateRequest("수정된 제목", question.getContent(), hashtag);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(updateRequest)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }
    @Test
    void 내용을_수정한다() {

        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        String accessToken = jwtProvider.issueAccessToken(question.getWriter().getId());
        List<String> hashtag = question.getHashtags().stream().map(Hashtag::getContent)
            .collect(Collectors.toList());

        QuestionUpdateRequest updateRequest = new QuestionUpdateRequest(question.getTitle(), "수정된 내용", hashtag);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(updateRequest)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 해시태그를_수정한다() {

        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(RuntimeException::new);
        String accessToken = jwtProvider.issueAccessToken(question.getWriter().getId());

        QuestionUpdateRequest updateRequest = new QuestionUpdateRequest(question.getTitle(), question.getContent(), List.of("수정 태그1", "수정 태그2"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(updateRequest)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 전부_수정한다() {

        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(RuntimeException::new);
        String accessToken = jwtProvider.issueAccessToken(question.getWriter().getId());

        QuestionUpdateRequest updateRequest = new QuestionUpdateRequest("수정된 제목", "수정된 내용", List.of("수정 태그1", "수정 태그2"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(updateRequest)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void Access_Token이_비어있는_경우_예외_발생() {
        String accessToken = " ";

        Map<String, Object> requestBody = Map.of("title", "질문있어요",
            "content", "스프링 어려워요", "hashtags",
            List.of("질문게시판", "고수가 되고싶어요", "코린이"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(requestBody)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(EMPTY_ACCESS_TOKEN.getStatus().value())
            .body("errorCode", equalTo(EMPTY_ACCESS_TOKEN.getErrorCode()))
            .body("message", equalTo(EMPTY_ACCESS_TOKEN.getErrorMessage()));
    }

    @Test
    void Access_Token이_변조된_경우_예외_발생() {
        String accessToken = jwtProvider.issueAccessToken(1L) + "123";

        Map<String, Object> requestBody = Map.of("title", "질문있어요",
            "content", "스프링 어려워요", "hashtags",
            List.of("질문게시판", "고수가 되고싶어요", "코린이"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)
            .body(requestBody)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(TAMPERED_ACCESS_TOKEN.getStatus().value())
            .body("errorCode", equalTo(TAMPERED_ACCESS_TOKEN.getErrorCode()))
            .body("message", equalTo(TAMPERED_ACCESS_TOKEN.getErrorMessage()));
    }

    @Test
    void Path_Variable이_없는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("title", "내가 글을 수정해볼게",
            "content", "하나둘셋 얍", "hashtags", List.of("ㄱㄴㄱㄴ", "얍", "모여라"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", " ")
            .body(requestBody)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("questionEdit.id: ID가 비어있습니다."));
    }

    @Test
    void id가_유효하지_않는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("title", "내가 글을 수정해볼게",
            "content", "하나둘셋 얍", "hashtags", List.of("ㄱㄴㄱㄴ", "얍", "모여라"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -1)
            .body(requestBody)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("questionEdit.id: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void id가_존재하지_않는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("title", "내가 글을 수정해볼게",
            "content", "하나둘셋 얍", "hashtags", List.of("ㄱㄴㄱㄴ", "얍", "모여라"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 999999999)
            .body(requestBody)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(ArticleExceptionType.ARTICLE_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void 필수값이_비어있을_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "하나둘셋 얍",
            "hashtags", List.of("ㄱㄴㄱㄴ", "얍", "모여라"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(requestBody)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo(INVALID_REQUEST.getErrorMessage()))
            .body("detail.title", equalTo("제목은 필수입니다."));
    }

    @Test
    void 존재하지_않는_회원인_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(99999999L);

        Map<String, Object> requestBody = Map.of("title", "안녕하세용",
            "content", "헬륨가스를모곳지", "hashtags",
            List.of("자유게시판", "개발자좋아효", "코린이"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(requestBody)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void 작성자가_일치하지_않는_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("title", "안녕하세용",
            "content", "헬륨가스를모곳지", "hashtags",
            List.of("자유게시판", "개발자좋아효", "코린이"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(requestBody)

            .when()
            .put("/community/question/{id}")

            .then()
            .statusCode(ARTICLE_NOT_MATCHED_WRITER.getStatus().value())
            .body("errorCode", equalTo(ARTICLE_NOT_MATCHED_WRITER.getErrorCode()));
    }
}
