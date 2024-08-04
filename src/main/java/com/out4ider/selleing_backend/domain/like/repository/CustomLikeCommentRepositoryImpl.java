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
public class CustomLikeCommentRepositoryImpl implements CustomLikeCommentRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(Set<Long> commentIds, Map<Long, Set<String>> commentIdWithEmails) {
        String sql = "INSERT INTO like_comment(comment_id, user_id) VALUES(?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                for (Long commentId : commentIds) {
                    for (String email : commentIdWithEmails.get(commentId)) {
                        ps.setLong(1, commentId);
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
