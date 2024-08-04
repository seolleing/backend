package com.out4ider.selleing_backend.domain.like.repository;

import java.util.Map;
import java.util.Set;

public interface CustomLikeNovelRepository {
    void batchInsert(Set<Long> novelIds, Map<Long, Set<String>> novelIdWithEmails);
}
