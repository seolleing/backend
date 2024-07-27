package com.out4ider.selleing_backend.domain.novel.service;

import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.novel.entity.NovelInfoEntity;
import com.out4ider.selleing_backend.domain.novel.repository.JDBCTemplateNovelInfoRepository;
import com.out4ider.selleing_backend.domain.novel.repository.JPANovelInfoRepository;
import com.out4ider.selleing_backend.domain.novel.repository.NovelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NovelService {
    private final NovelRepository novelRepository;
    private final JDBCTemplateNovelInfoRepository jdbcTemplateNovelInfoRepository;
    private final JPANovelInfoRepository jpaNovelInfoRepository;

    @Transactional
    public void save(NovelRequestDto novelRequestDto) {
        NovelEntity novelEntity = NovelEntity.builder()
                .title(novelRequestDto.getTitle())
                .startSentence(novelRequestDto.getStartSentence())
                .isReported(false)
                .build();
        novelRepository.save(novelEntity);
        List<NovelInfoEntity> novelInfoEntities = novelRequestDto.getNovelInfoRequestDtos().stream()
                .map(novelInfoRequestDto -> NovelInfoEntity.builder()
                .content(novelInfoRequestDto.getContent())
                .novel(novelEntity)
                .build()).toList();
        jdbcTemplateNovelInfoRepository.batchInsert(novelInfoEntities);
    }

    public List<NovelResponseDto> getSome(int page, String orderby) {
        Pageable pageable = PageRequest.of(page,10, Sort.by(orderby).descending());
        return novelRepository.findAll(pageable).getContent().stream().map(NovelEntity::toNovelResponseDto).toList();
    }

    public List<NovelInfoResponseDto> get(Long novelId) {
        return jpaNovelInfoRepository.findByNovelId(novelId).stream().map(NovelInfoEntity::toNovelInfoResponseDto).toList();
    }

    @Transactional
    public void updateReport(Long novelId){
        NovelEntity novelEntity = novelRepository.findById(novelId).orElseThrow(/* 예외 발생하도록 */);
        novelEntity.setReported(true);
        novelRepository.save(novelEntity);
    }
}
