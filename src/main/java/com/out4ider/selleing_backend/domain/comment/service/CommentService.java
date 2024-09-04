package com.out4ider.selleing_backend.domain.comment.service;

import com.out4ider.selleing_backend.domain.comment.dto.CommentRequestDto;
import com.out4ider.selleing_backend.domain.comment.dto.CommentResponseDto;
import com.out4ider.selleing_backend.domain.comment.entity.CommentEntity;
import com.out4ider.selleing_backend.domain.comment.repository.CommentRepository;
import com.out4ider.selleing_backend.domain.like.repository.LikeCommentRepository;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.novel.repository.NovelRepository;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import com.out4ider.selleing_backend.domain.user.repository.UserRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.NotAuthorizedException;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import com.out4ider.selleing_backend.global.common.service.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final NovelRepository novelRepository;
    private final UserRepository userRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final RedisService redisService;

    @Transactional
    public Long save(CommentRequestDto commentRequestDto, Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        NovelEntity novelEntity = novelRepository.findById(commentRequestDto.getNovelId()).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        CommentEntity commentEntity = CommentEntity.builder()
                .novel(novelEntity)
                .user(userEntity)
                .content(commentRequestDto.getContent())
                .build();
        commentRepository.save(commentEntity);
        return commentEntity.getId();
    }

    @Transactional
    public void update(Long commentId, String content, Long userId) {
        CommentEntity commentEntity = commentRepository.findByIdWithUser(commentId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        if (commentEntity.getUser().getUserId().equals(userId)) {
            commentEntity.setContent(content);
            commentRepository.save(commentEntity);
        } else {
            throw new NotAuthorizedException(ExceptionEnum.NOTAUTHORIZED.ordinal(), "you dont have authorization", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        CommentEntity commentEntity = commentRepository.findByIdWithUser(commentId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        if (commentEntity.getUser().getUserId().equals(userId)) {
            likeCommentRepository.deleteByComment_id(commentEntity.getId());
            commentRepository.delete(commentEntity);
        } else {
            throw new NotAuthorizedException(ExceptionEnum.NOTAUTHORIZED.ordinal(), "you dont have authorization", HttpStatus.FORBIDDEN);
        }
    }

    public List<CommentResponseDto> getMore(Long novelId, Long lastCommentId) {
        return commentRepository.findByNovelId(novelId, lastCommentId);
    }
}