package com.out4ider.selleing_backend.domain.novel.entity;

import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Table(name = "novel")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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


    public NovelResponseDto toNovelResponseDto(int newLikeCount) {
        return NovelResponseDto.builder()
                .novelId(this.novelId)
                .title(this.title)
                .startSentence(this.startSentence)
                .likeCount(this.likeNovels.size()+newLikeCount)
                .build();
    }
}
