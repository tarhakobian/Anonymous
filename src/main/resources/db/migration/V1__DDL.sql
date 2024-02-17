create table users
(
    id               uuid primary key default gen_random_uuid(),
    email            text unique,
    username         text unique,
    password         text not null,
    role             text             default 'STUDENT',
    active           boolean          default true,
    created_at       timestamp,
    last_modified_at timestamp
);

create table anonymous_posts
(
    id               serial primary key,
    user_id          uuid references users (id),
    url              text not null,
    is_deleted       boolean default false,
    username_public  boolean default false,
    created_at       timestamp,
    last_modified_at timestamp
);

create table comments
(
    id                serial primary key,
    user_id           uuid references users (id),
    post_id           bigint references anonymous_posts (id),
    parent_comment_id int references comments (id) on delete cascade,
    content           text not null,
    username_public   boolean default false,
    created_at        timestamp,
    last_modified_at  timestamp
);

create table posts_likes
(
    user_id uuid references users (id),
    post_id bigint references anonymous_posts (id) on delete cascade
);

create table comments_likes
(
    user_id    uuid references users (id),
    comment_id bigint references comments (id) on delete cascade
);

create table frames
(
    id         bigserial primary key,
    url        text not null,
    times_used integer default 0
);

