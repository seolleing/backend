package com.out4ider.selleing_backend.domain.comment.entity;

import com.out4ider.selleing_backend.domain.like.entity.LikeCommentEntity;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Table(name="comment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
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

    @Column(name = "like_count")
    @ColumnDefault("0")
    private int likeCount;

    public void incrementLikeCount(){
        this.likeCount+=1;
    }
}
