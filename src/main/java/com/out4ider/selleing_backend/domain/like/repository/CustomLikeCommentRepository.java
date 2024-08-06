package com.out4ider.selleing_backend.domain.like.repository;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface CustomLikeCommentRepository {
    void batchInsert(List<Pair<Long,Long>> pairList);
}
