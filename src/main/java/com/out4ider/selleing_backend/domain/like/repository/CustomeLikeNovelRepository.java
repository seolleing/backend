package com.out4ider.selleing_backend.domain.like.repository;

import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;

import java.util.List;

public interface CustomeLikeNovelRepository {

    List<LikeNovelEntity> findByNovelIdWithUser(Long novelId);
}
