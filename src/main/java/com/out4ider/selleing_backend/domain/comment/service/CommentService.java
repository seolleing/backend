package com.out4ider.selleing_backend.domain.comment.service;

import com.out4ider.selleing_backend.domain.comment.dto.CommentRequestDto;
import com.out4ider.selleing_backend.domain.comment.entity.CommentEntity;
import com.out4ider.selleing_backend.domain.comment.repository.CommentRepository;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.novel.repository.NovelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final NovelRepository novelRepository;

    @Transactional
    public Long save(CommentRequestDto commentRequestDto) {
        log.info("{}",commentRequestDto.getNovelId());
        NovelEntity novelEntity = novelRepository.findById(commentRequestDto.getNovelId()).orElseThrow(/* 예외 발생 */);
        CommentEntity commentEntity = CommentEntity.builder()
                .novel(novelEntity)
                .content(commentRequestDto.getContent())
                .build();
        commentRepository.save(commentEntity);
        return commentEntity.getId();
    }
}
