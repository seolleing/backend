package com.out4ider.selleing_backend.domain.like.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class CustomLikeNovelRepositoryImpl implements CustomLikeNovelRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(Set<Long> novelIds, Map<Long, Set<String>> novelIdWithEmails) {
        String sql = "INSERT INTO like_novel(novel_id, user_id) VALUES(?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                for(Long novelId : novelIds) {
                    for(String email : novelIdWithEmails.get(novelId)) {
                        ps.setLong(1, novelId);
                        ps.setString(2, email);
                    }
                }
            }

            @Override
            public int getBatchSize() {
                return 100;
            }
        });
    }
}
