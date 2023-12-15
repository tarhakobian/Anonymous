INSERT INTO users (id, email, username, password)
VALUES ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 'user1@student.glendale.edu', 'user1', '123456'),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 'user2@student.glendale.edu', 'user2', '123456');

INSERT INTO anonymous_posts (id, user_id, url, username_public)
VALUES (1, 'bb26548e-fe18-40b0-8a99-303bb2d6714a', 'https://example.com/post1', true),
       (2, 'bb26548e-fe18-40b0-8a99-303bb2d6714a', 'https://example.com/post2', false),
       (3, 'bb26548e-fe18-40b0-8a99-303bb2d6714a', 'https://example.com/post3', true),
       (4, 'bb26548e-fe18-40b0-8a99-303bb2d6714a', 'https://example.com/post3', false),
       (5, 'bb26548e-fe18-40b0-8a99-303bb2d6714a', 'https://example.com/post3', false),
       (6, '9faed82c-c4f0-43e4-9184-7ff83e2009e3', 'https://example.com/post3', true),
       (7, '9faed82c-c4f0-43e4-9184-7ff83e2009e3', 'https://example.com/post3', true),
       (8, '9faed82c-c4f0-43e4-9184-7ff83e2009e3', 'https://example.com/post3', false),
       (9, '9faed82c-c4f0-43e4-9184-7ff83e2009e3', 'https://example.com/post3', true),
       (10, '9faed82c-c4f0-43e4-9184-7ff83e2009e3', 'https://example.com/post3', true);

INSERT INTO comments (user_id, post_id, content)
VALUES ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 1, 'Comment 1 on post 1'),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 2, 'Comment 2 on post 2'),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 3, 'Comment 3 on post 3'),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 4, 'Comment 4 on post 3'),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 5, 'Comment 5 on post 3'),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 6, 'Comment 6 on post 3'),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 7, 'Comment 7 on post 3'),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 8, 'Comment 8 on post 3'),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 9, 'Comment 9 on post 3'),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 10, 'Comment 10 on post 3');


insert into comments (user_id, post_id, parent_comment_id, content)
values ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 1, 1, 'nice comment'),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 1, 1, 'nice comment'),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 1, 1, 'nice comment'),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 1, 1, 'nice comment'),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 1, 1, 'nice comment'),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 1, 1, 'nice comment'),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 1, 1, 'nice comment');

INSERT INTO likes (user_id, post_id)
VALUES ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 1),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 2),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 3),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 4),
       ('bb26548e-fe18-40b0-8a99-303bb2d6714a', 5),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 6),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 7),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 8),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 9),
       ('9faed82c-c4f0-43e4-9184-7ff83e2009e3', 10);

INSERT INTO frames (url, times_used)
VALUES ('https://example.com/frame1', 0),
       ('https://example.com/frame2', 0),
       ('https://example.com/frame3', 0);
