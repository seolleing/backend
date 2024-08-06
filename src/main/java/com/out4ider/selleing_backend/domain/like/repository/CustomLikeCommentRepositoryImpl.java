package com.out4ider.selleing_backend.domain.like.repository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomLikeCommentRepositoryImpl implements CustomLikeCommentRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(List<Pair<Long,Long>> pairList) {
        String sql = "INSERT INTO like_comment(comment_id, user_id) VALUES(?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Pair<Long,Long> commentIdAndUserId = pairList.get(i);
                ps.setLong(1, commentIdAndUserId.getLeft());
                ps.setLong(2, commentIdAndUserId.getRight());
            }
            @Override
            public int getBatchSize() {
                return pairList.size();
            }
        });
        pairList.clear();
    }
}
