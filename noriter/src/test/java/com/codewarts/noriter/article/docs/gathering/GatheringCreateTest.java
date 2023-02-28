package com.codewarts.noriter.article.docs.gathering;

import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static com.codewarts.noriter.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("스터디게시판 등록 기능 통합 테스트")
class GatheringCreateTest extends InitIntegrationRestDocsTest {

    @Test
    void 글을_등록한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("title", "안녕하세용",
            "content", "헬륨가스를모곳지", "hashtags", List.of("SPRING", "JPA", "코린이"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

        .when()
            .post("/community/gathering")

        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 필수값이_비어있을_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("title", "안녕하세용",
            "hashtags", List.of("SPRING", "JPA", "코린이"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

        .when()
            .post("/community/gathering")

        .then()
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo(INVALID_REQUEST.getErrorMessage()))
            .body("detail.content", equalTo("내용은 필수입니다."));
    }

    @Test
    void 존재하지_않는_회원인_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(222222L);

        Map<String, Object> requestBody = Map.of("title", "안녕하세용",
            "content", "헬륨가스를모곳지", "hashtags", List.of("SPRING", "JPA", "코린이"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

        .when()
            .post("/community/gathering")

        .then()
            .statusCode(MEMBER_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getErrorMessage()));
    }

}
