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
    public int like(Long id, String type, String email) {
        int size = 0;
        if (type.equals("novel")) {
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
        } else if (type.equals("comment")) {
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
        } else {
            throw new IllegalArgumentException();
        }
        return size + 1;
    }

    @Transactional
    public int unlike(Long id, String type, String email) {
        int size = 0;
        if (type.equals("novel")) {
            NovelEntity novelEntity = novelRepository.findByIdWithLikeNovel(id).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
            size = novelEntity.getLikeNovels().size();
            likeNovelRepository.deleteByNovelIdAndEmail(id,email);
        } else if (type.equals("comment")) {
            CommentEntity commentEntity = commentRepository.findByIdWithLikeComment(id).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
            size = commentEntity.getLikeComments().size();
            likeCommentRepository.deleteByCommentIdAndEmail(id, email);
        } else {
            throw new IllegalArgumentException();
        }
        return size - 1;
    }
}
