package com.out4ider.selleing_backend.domain.novel.service;

import com.out4ider.selleing_backend.domain.bookmark.repository.BookmarkRepository;
import com.out4ider.selleing_backend.domain.comment.dto.CommentResponseDto;
import com.out4ider.selleing_backend.domain.comment.repository.CommentRepository;
import com.out4ider.selleing_backend.domain.like.repository.likenovel.LikeNovelRepository;
import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelTotalResponseDto;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.novel.repository.novel.NovelRepository;
import com.out4ider.selleing_backend.domain.novel.repository.novelinfo.NovelInfoRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NovelService {
    private final NovelRepository novelRepository;
    private final CommentRepository commentRepository;
    private final NovelInfoRepository novelInfoRepository;
    private final LikeNovelRepository likeNovelRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public Long saveNovel(NovelRequestDto novelRequestDto) {
        NovelEntity novelEntity = NovelEntity.builder()
                .title(novelRequestDto.getTitle())
                .startSentence(novelRequestDto.getStartSentence())
                .likeNovels(new ArrayList<>()).isReported(false)
                .build();
        novelRepository.save(novelEntity);
        novelInfoRepository.batchInsert(novelEntity.getNovelId(),
                novelRequestDto.getNovelInfoRequestDtos());
        return novelEntity.getNovelId();
    }

    public List<NovelResponseDto> findNovelsByLatest(Long lastId) {
        return novelRepository.findOrderByNovelId(lastId);
    }

    public List<NovelResponseDto> findNovelsByPopular(Long lastId, Integer likeCount) {
        return novelRepository.findOrderByLikeCount(lastId, likeCount);
    }

    public NovelTotalResponseDto findNovel(Long novelId, Long userId) {
        boolean isLiked = likeNovelRepository.existsByNovel_NovelIdAndUser_UserId(novelId, userId);
        boolean isBookmarked = bookmarkRepository.existsByNovel_NovelIdAndUser_UserId(novelId, userId);
        int likeCount = novelRepository.findLikeCountByNovelId(novelId);
        List<NovelInfoResponseDto> novelInfoResponseDtos = novelInfoRepository.findByNovelIdWithUser(novelId);
        List<CommentResponseDto> commentResponseDtos = commentRepository.findOrderByCommentId(novelId);
        return new NovelTotalResponseDto(isLiked, isBookmarked, likeCount,
                novelInfoResponseDtos, commentResponseDtos);
    }

    @Transactional
    public void updateReport(Long novelId) {
        NovelEntity novelEntity = novelRepository.findById(novelId)
                .orElseThrow(() -> new NotFoundElementException(
                        ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        novelEntity.setReported(true);
        novelRepository.save(novelEntity);
    }

    public List<NovelResponseDto> findBookmarksByLatest(Long lastId, Long userId) {
        return novelRepository.findOrderByNovelIdInBookmark(lastId, userId);
    }
}
