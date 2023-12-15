create table users
(
    id       uuid primary key default gen_random_uuid(),
    email    text unique,
    username text unique,
    password text not null,
    role     text             default 'STUDENT'
);

create table anonymous_posts
(
    id              bigserial primary key,
    user_id         uuid references users (id),
    url             text not null,
    username_public boolean default false
);

create table comments
(
    id                serial primary key,
    user_id           uuid references users (id),
    post_id           bigint references anonymous_posts (id),
    parent_comment_id int references comments (id) on delete cascade,
    content           text not null
);

create table likes
(
    user_id uuid references users (id),
    post_id bigint references anonymous_posts (id)
);

create table frames
(
    id         bigserial primary key,
    url        text not null,
    times_used integer default 0
);