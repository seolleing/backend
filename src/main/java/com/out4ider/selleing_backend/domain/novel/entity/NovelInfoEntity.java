package com.out4ider.selleing_backend.domain.novel.entity;

import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "novel_info")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NovelInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_info_id")
    private Long novelInfoId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    private NovelEntity novel;

    @Column(name = "content")
    private String content;

}
