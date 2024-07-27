package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.entity.NovelInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JDBCTemplateNovelInfoRepository{
    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<NovelInfoEntity> novelInfoEntities) {
        String sql = "INSERT INTO novel_info(content, novel_id) VALUES(?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                NovelInfoEntity novelInfoEntity = novelInfoEntities.get(i);
                ps.setString(1,novelInfoEntity.getContent());
                ps.setLong(2, novelInfoEntity.getNovel().getNovelId());
            }

            @Override
            public int getBatchSize() {
                return novelInfoEntities.size();
            }
        });
    }
}