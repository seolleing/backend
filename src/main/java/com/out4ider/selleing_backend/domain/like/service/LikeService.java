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
    private final LikeNovelRepository likeNovelRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final NovelRepository novelRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public int likeNovel(Long id, String email) {
        int size = 0;
        NovelEntity novelEntity = novelRepository.findByIdWithLikeNovel(id).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        size = novelEntity.getLikeNovels().size();
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        LikeNovelEntity likeNovelEntity = LikeNovelEntity.builder()
                .novel(novelEntity)
                .user(userEntity)
                .build();
        likeNovelRepository.save(likeNovelEntity);
        novelEntity.addLikeNovel(likeNovelEntity);
        userEntity.addLikeNovel(likeNovelEntity);
        return size + 1;
    }

    @Transactional
    public int unlikeNovel(Long id, String email) {
        int size = 0;
        NovelEntity novelEntity = novelRepository.findByIdWithLikeNovel(id).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        size = novelEntity.getLikeNovels().size();
        likeNovelRepository.deleteByNovelIdAndEmail(id, email);
        return size - 1;
    }

    @Transactional
    public int likeComment(Long id, String email) {
        if (likeCommentRepository.findLikeComment(id, email).isPresent()) {
            throw new AlreadyDoingException(ExceptionEnum.ALREADYDOING.ordinal(), "이미 좋아요를 누르셨습니다.", HttpStatus.FORBIDDEN);
        } else {
            int size = 0;
            CommentEntity commentEntity = commentRepository.findByIdWithLikeComment(id).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
            size = commentEntity.getLikeComments().size();
            UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
            LikeCommentEntity likeCommentEntity = LikeCommentEntity.builder()
                    .comment(commentEntity)
                    .user(userEntity)
                    .build();
            likeCommentRepository.save(likeCommentEntity);
            commentEntity.addLikeComment(likeCommentEntity);
            userEntity.addLikeComment(likeCommentEntity);
            return size + 1;
        }
    }
}
