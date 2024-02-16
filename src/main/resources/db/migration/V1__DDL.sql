create table users
(
    id                 uuid primary key default gen_random_uuid(),
    email              text unique,
    username           text unique,
    password           text not null,
    role               text             default 'STUDENT',
    created_date       timestamp        default now(),
    last_modified_date timestamp
);

create table anonymous_posts
(
    id              serial primary key,
    user_id         uuid references users (id),
    url             text not null,
    username_public boolean   default false,
    created_date    timestamp default now()
);

create table comments
(
    id                serial primary key,
    user_id           uuid references users (id),
    post_id           bigint references anonymous_posts (id),
    parent_comment_id int references comments (id) on delete cascade,
    content           text not null,
    username_public   boolean   default false,
    created_date      timestamp default now()
);

create table posts_likes
(
    user_id uuid references users (id),
    post_id bigint references anonymous_posts (id)
);

create table comments_likes
(
    user_id    uuid references users (id),
    comment_id bigint references comments (id)
);

create table frames
(
    id         bigserial primary key,
    url        text not null,
    times_used integer default 0
);

