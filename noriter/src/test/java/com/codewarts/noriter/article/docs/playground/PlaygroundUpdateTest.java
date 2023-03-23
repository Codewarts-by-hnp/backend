package com.codewarts.noriter.article.docs.playground;

import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_MATCHED_WRITER;
import static com.codewarts.noriter.exception.type.ArticleExceptionType.DELETED_ARTICLE;
import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static com.codewarts.noriter.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("자유게시판 수정 기능 통합 테스트")
class PlaygroundUpdateTest extends InitIntegrationRestDocsTest {

    @Test
    void 글을_수정한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("title", "내가 글을 수정해볼게",
            "content", "하나둘셋 얍", "hashtags", List.of("ㄱㄴㄱㄴ", "얍", "모여라"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 2)
            .body(requestBody)

        .when()
            .put("/community/playground/{id}")

        .then()
            .statusCode(HttpStatus.OK.value());
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
            .put("/community/playground/{id}")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("edit.id: ID가 비어있습니다."));
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
            .put("/community/playground/{id}")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("edit.id: 게시글 ID는 양수이어야 합니다."));
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
            .put("/community/playground/{id}")

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
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/community/playground/{id}")

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
            .pathParam("id", 10)
            .body(requestBody)

        .when()
            .put("/community/playground/{id}")

        .then()
            .statusCode(MEMBER_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void 작성자가_일치하지_않는_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("title", "안녕하세용",
            "content", "헬륨가스를모곳지", "hashtags",
            List.of("자유게시판", "개발자좋아효", "코린이"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 10)
            .body(requestBody)

        .when()
            .put("/community/playground/{id}")

        .then()
            .statusCode(ARTICLE_NOT_MATCHED_WRITER.getStatus().value())
            .body("errorCode", equalTo(ARTICLE_NOT_MATCHED_WRITER.getErrorCode()));
    }

    @Test
    void 삭제된_게시물에_수정을_요청할_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("title", "안녕하세용",
            "content", "헬륨가스를모곳지", "hashtags",
            List.of("자유게시판", "개발자좋아효", "코린이"));

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 13)
            .body(requestBody)

        .when()
            .put("/community/playground/{id}")

        .then()
            .statusCode(DELETED_ARTICLE.getStatus().value())
            .body("errorCode", equalTo(DELETED_ARTICLE.getErrorCode()));
    }
}
