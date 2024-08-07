package com.out4ider.selleing_backend.domain.bookmark.entity;

import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmark")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    private NovelEntity novel;
}
