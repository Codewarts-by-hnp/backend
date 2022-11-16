INSERT INTO members
    (id, email, nickname, profile_image_url, refresh_token, resource_server, resource_server_id)
VALUES
    (1, 'olx.rko.o@gmail.com', 'rkolx', 'https://avatars.githubusercontent.com/u/92699009?v=4',
     'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NjExMzExODEsImF1ZCI6IjkyNjk5MDA5IiwiZXhwIjoxNjY4OTE1MDIwOSwibWVtYmVySWQiOjkyNjk5MDA5LCJpc3MiOiIxc2VwaGlsIiwic3ViIjoiUmVmcmVzaC1Ub2tlbiJ9.WTIn5iPi5k9LIsFPKN4LpmK7nFm69glsJ4xU-vEqmDc',
     'GITHUB', 92699009),
    (2, 'peelhw@gmail.com', 'philsogooood', 'https://avatars.githubusercontent.com/u/87455844?v=4',
     'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NjExMzExODEsImF1ZCI6Ijg3NDU1ODQ0IiwiZXhwIjoxNjY4OTE1MDIwOSwibWVtYmVySWQiOjg3NDU1ODQ0LCJpc3MiOiIxc2VwaGlsIiwic3ViIjoiUmVmcmVzaC1Ub2tlbiJ9.OuH6NJnCcCZBMWu_BvxVFKhxccx3FvkWc6j2EsHa60c',
     'GITHUB', 87455844);


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

INSERT INTO comment
    (id, content, deleted, secret, writer_id, article_id, written_time, edited_time)
VALUES
    (1, '우왕 잘봤어용', false, false, 2, 1, '2022-11-12 16:25:58.991061', '2022-11-12 16:25:58.991061'),
    (2, '우왕 잘봤어용2', false, false, 2, 1, '2022-11-12 17:25:58.991061', '2022-11-12 17:25:58.991061'),
    (3, '우왕 잘봤어3', false, false, 2, 1, '2022-11-12 19:25:58.991061', '2022-11-12 19:25:58.991061'),
    (4, '우왕 잘봤어용4', false, false, 2, 1, '2022-11-13 16:25:58.991061', '2022-11-13 16:25:58.991061');

