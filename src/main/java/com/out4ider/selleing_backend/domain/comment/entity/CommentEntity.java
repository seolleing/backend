package com.out4ider.selleing_backend.domain.comment.entity;

import com.out4ider.selleing_backend.domain.comment.dto.CommentResponseDto;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    //user랑 이어주기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    private NovelEntity novel;

    @Column(name="comment_content")
    private String content;

    public CommentResponseDto toCommentResponseDto() {
        return CommentResponseDto.builder()
                .commentId(this.id)
                .content(this.content)
                .build();
    }
}
