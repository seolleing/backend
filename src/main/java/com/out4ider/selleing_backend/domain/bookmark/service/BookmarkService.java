package com.out4ider.selleing_backend.domain.bookmark.service;

import com.out4ider.selleing_backend.domain.bookmark.entity.BookmarkEntity;
import com.out4ider.selleing_backend.domain.bookmark.repository.BookmarkRepository;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.novel.repository.NovelRepository;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import com.out4ider.selleing_backend.domain.user.repository.UserRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final NovelRepository novelRepository;

    @Transactional
    public void bookmark(Long novelId, Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        NovelEntity novelEntity = novelRepository.findById(novelId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
                .user(userEntity)
                .novel(novelEntity)
                .build();
        bookmarkRepository.save(bookmarkEntity);
    }

    @Transactional
    public void unBookmark(Long novelId, Long userId) {
        bookmarkRepository.deleteByNovel_NovelIdAndUser_UserId(novelId, userId);
    }
}
