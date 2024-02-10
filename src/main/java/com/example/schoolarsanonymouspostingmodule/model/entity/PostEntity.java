package com.example.schoolarsanonymouspostingmodule.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "anonymous_posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url")
    private String url;

    @Column(name = "username_public")
    private Boolean usernamePublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity publisher;

    @ManyToMany(mappedBy = "likedPosts", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<UserEntity> likedBy;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Set<CommentEntity> comments;
}
