package com.out4ider.selleing_backend.domain.user.entity;

import com.out4ider.selleing_backend.domain.bookmark.entity.BookmarkEntity;
import com.out4ider.selleing_backend.domain.like.entity.LikeCommentEntity;
import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name="password")
    private String encryptedPassword;

    @Column(name = "nickname")
    @Setter
    private String nickname;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<LikeNovelEntity> likeNovels;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<LikeCommentEntity> likeComments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<BookmarkEntity> bookmarks;

}
