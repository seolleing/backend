package com.out4ider.selleing_backend.domain.like.repository;

import java.util.Map;
import java.util.Set;

public interface CustomLikeCommentRepository {
    void batchInsert(Set<Long> commentIds, Map<Long, Set<String>> commentIdWithEmails);
}
