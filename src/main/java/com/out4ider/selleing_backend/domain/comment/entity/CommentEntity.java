package com.out4ider.selleing_backend.domain.comment.entity;

import com.out4ider.selleing_backend.domain.comment.dto.CommentResponseDto;
import com.out4ider.selleing_backend.domain.like.entity.LikeCommentEntity;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="comment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    private NovelEntity novel;

    @Column(name="comment_content")
    @Setter
    private String content;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<LikeCommentEntity> likeComments;

    public CommentResponseDto toCommentResponseDto(int newLikeCount) {
        return CommentResponseDto.builder()
                .commentId(this.id)
                .content(this.content)
                .nickname(this.user.getNickname())
                .likeCount(this.likeComments.size()+newLikeCount)
                .build();
    }
}
