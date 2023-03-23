package com.codewarts.noriter.article.docs.gathering;

import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_FOUND;
import static com.codewarts.noriter.exception.type.ArticleExceptionType.DELETED_ARTICLE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("스터디게시판 상세 조회 기능 통합 테스트")
public class GatheringDetailTest extends InitIntegrationRestDocsTest {

    @Test
    void 상세조회를_한다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", 1)

        .when()
            .get("/community/gathering/{id}")

        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(1))
            .body("title", equalTo("테스트를 해볼것이당"))
            .body("content", equalTo("안녕하냐고오옹"))
            .body("writer.id", equalTo(1))
            .body("writer.nickname", equalTo("admin1"))
            .body("writer.profileImage",
                equalTo("https://avatars.githubusercontent.com/u/111111?v=4"))
            .body("sameWriter", equalTo(false))
            .body("hashtags[0]", equalTo("SPRING"))
            .body("hashtags[1]", equalTo("JPA"))
            .body("hashtags[2]", equalTo("난자유야"))
            .body("wish", equalTo(false))
            .body("wishCount", equalTo(1))
            .body("status", equalTo("INCOMPLETE"))
            .body("comment[0].id", equalTo(1))
            .body("comment[0].content", equalTo("우왕 잘봤어용"))
            .body("comment[0].writer.id", equalTo(1))
            .body("comment[0].writer.nickname", equalTo("admin1"))
            .body("comment[0].writer.profileImage",
                equalTo("https://avatars.githubusercontent.com/u/111111?v=4"));
    }

    @Test
    void 로그인_후_상세조회를_한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

        .when()
            .get("/community/gathering/{id}")

        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(1))
            .body("title", equalTo("테스트를 해볼것이당"))
            .body("content", equalTo("안녕하냐고오옹"))
            .body("writer.id", equalTo(1))
            .body("writer.nickname", equalTo("admin1"))
            .body("writer.profileImage",
                equalTo("https://avatars.githubusercontent.com/u/111111?v=4"))
            .body("sameWriter", equalTo(false))
            .body("hashtags[0]", equalTo("SPRING"))
            .body("hashtags[1]", equalTo("JPA"))
            .body("hashtags[2]", equalTo("난자유야"))
            .body("wish", equalTo(true))
            .body("wishCount", equalTo(1))
            .body("status", equalTo("INCOMPLETE"))
            .body("comment[0].id", equalTo(1))
            .body("comment[0].content", equalTo("우왕 잘봤어용"))
            .body("comment[0].writer.id", equalTo(1))
            .body("comment[0].writer.nickname", equalTo("admin1"))
            .body("comment[0].writer.profileImage",
                equalTo("https://avatars.githubusercontent.com/u/111111?v=4"));
    }

    @Test
    void Path_Variable이_없는_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", " ")

        .when()
            .get("/community/gathering/{id}")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("getDetail.id: ID가 비어있습니다."));
    }

    @Test
    void id가_유효하지_않는_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", -1)

        .when()
            .get("/community/gathering/{id}")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("getDetail.id: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void id가_존재하지_않는_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", 999999999)

        .when()
            .get("/community/gathering/{id}")

        .then()
            .statusCode(ARTICLE_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(ARTICLE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ARTICLE_NOT_FOUND.getErrorMessage()));

    }

    @Test
    void 삭제된_게시물을_조회할_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", 14)

        .when()
            .get("/community/gathering/{id}")

        .then()
            .statusCode(DELETED_ARTICLE.getStatus().value())
            .body("errorCode", equalTo(DELETED_ARTICLE.getErrorCode()))
            .body("message", equalTo(DELETED_ARTICLE.getErrorMessage()));
    }
}
