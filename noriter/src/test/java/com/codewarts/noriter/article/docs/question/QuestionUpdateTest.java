package com.codewarts.noriter.article.docs.question;

import static io.restassured.RestAssured.given;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
class QuestionUpdateTest {

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
    @Test
    void 내용을_수정한다() {

        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(RuntimeException::new);
        String accessToken = jwtProvider.issueAccessToken(question.getWriter().getId());
        List<String> hashtag = question.getHashtags().stream().map(Hashtag::getContent)
            .collect(Collectors.toList());

        QuestionUpdateRequest updateRequest = new QuestionUpdateRequest(question.getTitle(), "수정된 내용", hashtag);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(updateRequest)

            .when()
            .put("/community/question/6")

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
            .body(updateRequest)

            .when()
            .put("/community/question/6")

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
            .body(updateRequest)

            .when()
            .put("/community/question/6")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    void 해결_여부를_변경한다(String completed) {

        Question question = questionRepository.findQuestionById(6L)
            .orElseThrow(RuntimeException::new);
        String accessToken = jwtProvider.issueAccessToken(question.getWriter().getId());
        Map<String, String> map = Map.of("completed", completed, "Authorization", accessToken);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(map)

            .when()
            .patch("/community/question/6")

            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
