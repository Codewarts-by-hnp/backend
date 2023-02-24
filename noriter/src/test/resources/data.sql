DELETE from image;
DELETE from hashtag;
DELETE from wish;
DELETE from re_comment;
DELETE from comment;
DELETE from question;
DELETE from study;
DELETE from article;
DELETE from members;

INSERT INTO members
    (id, email, nickname, profile_image_url, refresh_token, resource_server, resource_server_id)
VALUES
    (1, 'admin1@gmail.com', 'admin1', 'https://avatars.githubusercontent.com/u/111111?v=4',
     'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NjExMzExODEsImF1ZCI6IjkyNjk5MDA5IiwiZXhwIjoxNjY4OTE1MDIwOSwibWVtYmVySWQiOjkyNjk5MDA5LCJpc3MiOiIxc2VwaGlsIiwic3ViIjoiUmVmcmVzaC1Ub2tlbiJ9.WTIn5iPi5k9LIsFPKN4LpmK7nFm69glsJ4xU-vEqmDc',
     'GITHUB', 111111),
    (2, 'admin2@gmail.com', 'admin2', 'https://avatars.githubusercontent.com/u/222222?v=4',
     'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NjExMzExODEsImF1ZCI6Ijg3NDU1ODQ0IiwiZXhwIjoxNjY4OTE1MDIwOSwibWVtYmVySWQiOjg3NDU1ODQ0LCJpc3MiOiIxc2VwaGlsIiwic3ViIjoiUmVmcmVzaC1Ub2tlbiJ9.OuH6NJnCcCZBMWu_BvxVFKhxccx3FvkWc6j2EsHa60c',
     'GITHUB', 222222);


INSERT INTO article
    (id, article_type, title, content, deleted, edited_time, written_time, writer_id)
VALUES
    (1, 'STUDY', '테스트를 해볼것이당','안녕하냐고오옹', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (2, 'STUDY', '테스트를 해볼것이당1','안녕하냐고오옹1', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (3, 'STUDY', '테스트를 해볼것이당2','안녕하냐고오옹2', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (4, 'STUDY', '테스트를 해볼것이당3','안녕하냐고오옹3', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (5, 'STUDY', '테스트를 해볼것이당4','안녕하냐고오옹4', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (6, 'QUESTION', '질문1','궁금1', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (7, 'QUESTION', '질문2','궁금2', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (8, 'QUESTION', '질문3','궁금3', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (9, 'QUESTION', '질문3','궁금3', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 2),
    (10, 'FREE', '붕어빵 먹고싶어요','강남 붕어빵 맛잇는 집', false, '2022-11-25 16:25:58.991061', '2022-11-25 16:25:58.991061', 2),
    (11, 'FREE', '취업하고싶어효','어떻게하면되나요', false, '2022-11-23 16:25:58.991061', '2022-11-23 16:25:58.991061', 2),
    (12, 'FREE', '띵댕동댕','도레미파', false, '2022-11-22 16:25:58.991061', '2022-11-22 16:25:58.991061', 2),
    (13, 'FREE', '띵댕동댕','도레미파', true, '2022-11-22 16:25:58.991061', '2022-11-22 16:25:58.991061', 2);


INSERT INTO study
    (id, status)
VALUES
    (1, 'INCOMPLETE'),
    (2, 'COMPLETE'),
    (3, 'INCOMPLETE'),
    (4, 'COMPLETE'),
    (5, 'INCOMPLETE');


INSERT INTO question
    (id, status)
VALUES
    (6, 'INCOMPLETE'),
    (7, 'COMPLETE'),
    (8, 'INCOMPLETE'),
    (9, 'COMPLETE');


INSERT INTO hashtag
    (id, content, deleted, article_id)
VALUES
    (1, 'SPRING', false, 1),
    (2, 'JPA', false, 1),
    (3, '난자유야', false, 1),
    (4, '강남역', false, 10),
    (5, '붕어팥', false, 10),
    (6, '팥', false, 11),
    (7, '슈크림', false, 11),
    (8, '겨울', false, 11),
    (9, '스프링', false, 6),
    (10, '코린이', false, 6),
    (11, '도와줘요', false, 6),
    (12, '코드스쿼드', false, 7),
    (13, '마스터즈', false, 7),
    (14, '백엔드', false, 7),
    (15, '필 최고', false, 8),
    (16, '장난아냐', false, 8),
    (17, '진짜루', false, 8),
    (18, '한세 바보', false, 9),
    (19, '거짓말이죠?', false, 9),
    (20, '아마도?', false, 9);


INSERT INTO comment
    (id, content, deleted, secret, writer_id, article_id, written_time, edited_time)
VALUES
    (1, '우왕 잘봤어용', false, false, 1, 1, '2022-11-12 16:25:58.991061', '2022-11-12 16:25:58.991061'),
    (2, '우왕 잘봤어용2', false, false, 1, 1, '2022-11-12 17:25:58.991061', '2022-11-12 17:25:58.991061'),
    (3, '우왕 잘봤어3', false, false, 1, 1, '2022-11-12 19:25:58.991061', '2022-11-12 19:25:58.991061'),
    (4, '우왕 잘봤어용4', false, false, 1, 1, '2022-11-13 16:25:58.991061', '2022-11-13 16:25:58.991061'),
    (5, '강남역 11번출구에서 팔아요', false, false, 2, 10, '2022-11-26 16:25:58.991061', '2022-11-26 16:25:58.991061'),
    (6, '강남역 11번출구에서 팔아요', true, false, 2, 12, '2022-11-26 16:25:58.991061', '2022-11-26 16:25:58.991061'),
    (7, '강남역 11번출구에서 팔아요', false, false, 2, 13, '2022-11-26 16:25:58.991061', '2022-11-26 16:25:58.991061');

INSERT INTO wish
    (id, article_id, member_id)
VALUES
    (1, 3, 2),
    (2, 10, 2),
    (3, 6, 2),
    (4, 1, 2);

INSERT INTO re_comment
    (id, comment_id, writer_id, content, secret, deleted, written_time, edited_time)
VALUES
    (1, 1, 2, '잘 봤다니 다행입니당', false, false, '2022-11-26 16:25:58.991061', '2022-11-26 16:25:58.991061'),
    (2, 6, 2, '잘 봤다니 다행입니당', false, false, '2022-11-26 16:25:58.991061', '2022-11-26 16:25:58.991061'),
    (3, 6, 2, '잘 봤다니 다행입니당', false, true, '2022-11-26 16:25:58.991061', '2022-11-26 16:25:58.991061'),
    (4, 7, 2, '잘 봤다니 다행입니당', false, false, '2022-11-26 16:25:58.991061', '2022-11-26 16:25:58.991061'),
    (5, 1, 2, '잘 봤다니 다행입니당', false, true, '2022-11-26 16:25:58.991061', '2022-11-26 16:25:58.991061'),
    (6, 5, 2, '잘 봤다니 다행입니당', false, false, '2022-11-26 16:25:58.991061', '2022-11-26 16:25:58.991061');

