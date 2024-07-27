package com.out4ider.selleing_backend.domain.novel.entity;

import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import jakarta.persistence.*;
import lombok.*;

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

    public NovelResponseDto toNovelResponseDto() {
        return NovelResponseDto.builder()
                .novelId(this.novelId)
                .title(this.title)
                .startSentence(this.startSentence)
                .build();
    }
}
