package com.out4ider.selleing_backend.domain.like.entity;

import com.out4ider.selleing_backend.domain.comment.entity.CommentEntity;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "like_comment")
public class LikeCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;
}
