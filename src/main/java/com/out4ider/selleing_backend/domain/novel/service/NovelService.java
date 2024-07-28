package com.out4ider.selleing_backend.domain.novel.service;

import com.out4ider.selleing_backend.domain.comment.entity.CommentEntity;
import com.out4ider.selleing_backend.domain.comment.repository.CommentRepository;
import com.out4ider.selleing_backend.domain.like.repository.LikeNovelRepository;
import com.out4ider.selleing_backend.domain.novel.dto.NovelRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelTotalResponseDto;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.novel.entity.NovelInfoEntity;
import com.out4ider.selleing_backend.domain.novel.repository.JDBCTemplateNovelInfoRepository;
import com.out4ider.selleing_backend.domain.novel.repository.JPANovelInfoRepository;
import com.out4ider.selleing_backend.domain.novel.repository.NovelRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NovelService {
    private final NovelRepository novelRepository;
    private final JDBCTemplateNovelInfoRepository jdbcTemplateNovelInfoRepository;
    private final CommentRepository commentRepository;
    private final JPANovelInfoRepository jpaNovelInfoRepository;
    private final LikeNovelRepository likeNovelRepository;

    @Transactional
    public Long save(NovelRequestDto novelRequestDto) {
        NovelEntity novelEntity = NovelEntity.builder()
                .title(novelRequestDto.getTitle())
                .startSentence(novelRequestDto.getStartSentence())
                .likeNovels(new ArrayList<>())
                .isReported(false)
                .build();
        novelRepository.save(novelEntity);
        jdbcTemplateNovelInfoRepository.batchInsert(novelRequestDto.getNovelInfoRequestDtos(), novelEntity.getNovelId());
        return novelEntity.getNovelId();
    }

    public List<NovelResponseDto> getSome(int page, String orderby) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orderby).descending());
        return novelRepository.findAll(pageable).getContent().stream().map(NovelEntity::toNovelResponseDto).toList();
    }

    public NovelTotalResponseDto get(Long novelId, String email) {
        boolean isLiked = likeNovelRepository.findLikeNovel(novelId, email).isPresent();
        return new NovelTotalResponseDto(isLiked, jpaNovelInfoRepository.findByNovelId(novelId).stream().map(NovelInfoEntity::toNovelInfoResponseDto).toList(),
                commentRepository.findByNovelId(novelId).stream().map(CommentEntity::toCommentResponseDto).toList());
    }

    @Transactional
    public void updateReport(Long novelId) {
        NovelEntity novelEntity = novelRepository.findById(novelId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        novelEntity.setReported(true);
        novelRepository.save(novelEntity);
    }
}
