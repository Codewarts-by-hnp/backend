INSERT INTO members
    (id, email, nickname, profile_image_url, refresh_token, resource_server, resource_server_id)
VALUES (1, 'olx.rko.o@gmail.com', 'rkolx', 'https://avatars.githubusercontent.com/u/92699009?v=4',
        'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2Njg5MTUwMjA5fQ.ljyiSCdelyGB_DDXNBC-yQh0A7uQ38IhzQ4OuBOboY0',
        'GITHUB', 92699009);


INSERT INTO article
    (id, article_type, title, content, deleted, edited_time, written_time, writer_id)
VALUES
    (1, 'STUDY', '테스트를 해볼것이당','안녕하냐고오옹', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (2, 'STUDY', '테스트를 해볼것이당1','안녕하냐고오옹1', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (3, 'STUDY', '테스트를 해볼것이당2','안녕하냐고오옹2', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (4, 'STUDY', '테스트를 해볼것이당3','안녕하냐고오옹3', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1),
    (5, 'STUDY', '테스트를 해볼것이당4','안녕하냐고오옹4', false, '2022-11-11 16:25:58.991061', '2022-11-11 16:25:58.991061', 1);

INSERT INTO study
    (id, completed)
VALUES
    (1, false),
    (2, true),
    (3, false),
    (4, true),
    (5, false);

