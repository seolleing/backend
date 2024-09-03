package com.out4ider.selleing_backend.domain.like.service;

import com.out4ider.selleing_backend.domain.comment.entity.CommentEntity;
import com.out4ider.selleing_backend.domain.comment.repository.CommentRepository;
import com.out4ider.selleing_backend.domain.like.entity.LikeCommentEntity;
import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import com.out4ider.selleing_backend.domain.like.repository.LikeCommentRepository;
import com.out4ider.selleing_backend.domain.like.repository.LikeNovelRepository;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.novel.repository.NovelRepository;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import com.out4ider.selleing_backend.domain.user.repository.UserRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.AlreadyDoingException;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeCommentRepository likeCommentRepository;
    private final CommentRepository commentRepository;
    private final LikeNovelRepository likeNovelRepository;
    private final NovelRepository novelRepository;
    private final UserRepository userRepository;

    @Transactional
    public void likeNovel(Long novelId, Long userId) {
        if (likeNovelRepository.findByNovel_NovelIdAndUser_UserId(novelId, userId).isPresent()) {
            throw new AlreadyDoingException(ExceptionEnum.ALREADYDOING.ordinal(), "이미 좋아요를 누르셨습니다.", HttpStatus.FORBIDDEN);
        } else {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
            NovelEntity novelEntity = novelRepository.findById(novelId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
            LikeNovelEntity likeNovel = LikeNovelEntity.builder()
                    .user(userEntity)
                    .novel(novelEntity)
                    .build();
            likeNovelRepository.save(likeNovel);
            novelEntity.incrementLikeCount();
            novelRepository.save(novelEntity);
        }
    }

    @Transactional
    public void likeComment(Long id, Long userId) {
        if (likeCommentRepository.findByComment_IdAndUser_UserId(id, userId).isPresent()) {
            throw new AlreadyDoingException(ExceptionEnum.ALREADYDOING.ordinal(), "이미 좋아요를 누르셨습니다.", HttpStatus.FORBIDDEN);
        } else {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
            CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
            LikeCommentEntity likeComment = LikeCommentEntity.builder()
                    .user(userEntity)
                    .comment(commentEntity)
                    .build();
            likeCommentRepository.save(likeComment);
            commentEntity.incrementLikeCount();
            commentRepository.save(commentEntity);
        }
    }
}
