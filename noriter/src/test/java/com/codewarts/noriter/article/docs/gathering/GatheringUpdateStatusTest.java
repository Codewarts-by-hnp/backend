package com.codewarts.noriter.article.docs.gathering;

import static com.codewarts.noriter.exception.type.ArticleExceptionType.ALREADY_CHANGED_STATUS;
import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_FOUND;
import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_MATCHED_WRITER;
import static com.codewarts.noriter.exception.type.CommonExceptionType.INCORRECT_REQUEST_VALUE;
import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static com.codewarts.noriter.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("스터디게시판 상태 수정 기능 통합 테스트")
class GatheringUpdateStatusTest extends InitIntegrationRestDocsTest {

    @Test
    void 게시글의_모집상태를_변경한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)
            .body(Collections.singletonMap("status", "complete"))

        .when()
            .patch("/community/gathering/{id}")

        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void Path_Variable이_없는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", " ")
            .body(Collections.singletonMap("status", "complete"))

        .when()
            .patch("/community/gathering/{id}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("changeStatus.id: ID가 비어있습니다."));
    }

    @Test
    void id가_유효하지_않는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -1)
            .body(Collections.singletonMap("status", "complete"))

        .when()
            .patch("/community/gathering/{id}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("changeStatus.id: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void 필수값이_비어있을_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)
            .body(Collections.singletonMap("status", " "))

        .when()
            .patch("/community/gathering/{id}")

        .then()
            .statusCode(INCORRECT_REQUEST_VALUE.getStatus().value())
            .body("errorCode", equalTo(INCORRECT_REQUEST_VALUE.getErrorCode()))
            .body("message", equalTo(INCORRECT_REQUEST_VALUE.getErrorMessage()));

    }

    @Test
    void 존재하지_않는_회원인_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(99999L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 9999999)
            .body(Collections.singletonMap("status", "complete"))

        .when()
            .patch("/community/gathering/{id}")

        .then()
            .statusCode(MEMBER_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void id가_존재하지_않는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 9999999)
            .body(Collections.singletonMap("status", "complete"))

        .when()
            .patch("/community/gathering/{id}")

        .then()
            .statusCode(ARTICLE_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(ARTICLE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ARTICLE_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void 다른_회원의_상태변경을_요청할_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)
            .body(Collections.singletonMap("status", "incomplete"))

        .when()
            .patch("/community/gathering/{id}")

        .then()
            .statusCode(ARTICLE_NOT_MATCHED_WRITER.getStatus().value())
            .body("errorCode", equalTo(ARTICLE_NOT_MATCHED_WRITER.getErrorCode()))
            .body("message", equalTo(ARTICLE_NOT_MATCHED_WRITER.getErrorMessage()));
    }

    @Test
    void 현재와_같은_상태변경으_요청할_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)
            .body(Collections.singletonMap("status", "incomplete"))

        .when()
            .patch("/community/gathering/{id}")

        .then()
            .statusCode(ALREADY_CHANGED_STATUS.getStatus().value())
            .body("errorCode", equalTo(ALREADY_CHANGED_STATUS.getErrorCode()))
            .body("message", equalTo(ALREADY_CHANGED_STATUS.getErrorMessage()));
    }
}
