package com.out4ider.selleing_backend.domain.novel.entity;

import com.out4ider.selleing_backend.domain.bookmark.entity.BookmarkEntity;
import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Table(name = "novel")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class NovelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_id")
    private Long novelId;

    @Column(name = "title")
    private String title;

    @Column(name = "start_sentence")
    private String startSentence;

    @Column(name="is_reported")
    @Setter
    private boolean isReported;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "novel", fetch = FetchType.LAZY)
    private List<LikeNovelEntity> likeNovels;

    @OneToMany(mappedBy = "novel", fetch = FetchType.LAZY)
    private List<BookmarkEntity> bookmarks;

    @Column(name = "like_count")
    @ColumnDefault("0")
    private int likeCount;

    public void incrementLikeCount() {
        this.likeCount+=1;
    }
}
