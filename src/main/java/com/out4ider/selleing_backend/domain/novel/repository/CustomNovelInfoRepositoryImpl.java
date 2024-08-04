package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomNovelInfoRepositoryImpl implements CustomNovelInfoRepository {
    private final JdbcTemplate jdbcTemplate;

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
                return 8;
            }
        });
    }
}