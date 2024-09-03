package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.QNovelInfoResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.out4ider.selleing_backend.domain.novel.entity.QNovelInfoEntity.novelInfoEntity;
import static com.out4ider.selleing_backend.domain.user.entity.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class CustomNovelInfoRepositoryImpl implements CustomNovelInfoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void batchInsert(List<NovelInfoRequestDto> novelInfoRequestDtos, Long novelId) {
        String sql = "INSERT INTO novel_info(content, novel_id, user_id) VALUES(?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                NovelInfoRequestDto novelInfoRequestDto = novelInfoRequestDtos.get(i);
                ps.setString(1,novelInfoRequestDto.getContent());
                ps.setLong(2, novelId);
                ps.setLong(3, novelInfoRequestDto.getUserId());
            }

            @Override
            public int getBatchSize() {
                return novelInfoRequestDtos.size();
            }
        });
    }

    @Override
    public List<NovelInfoResponseDto> findByNovelId(Long novelId) {
        return jpaQueryFactory
                .select(new QNovelInfoResponseDto(
                        userEntity.nickname,
                        novelInfoEntity.content
                ))
                .from(novelInfoEntity)
                .join(novelInfoEntity.user, userEntity)
                .where(novelInfoEntity.novel.novelId.eq(novelId))
                .fetch();
    }
}