package com.out4ider.user.redis;

import lombok.Getter;

@Getter
public enum RedisKeyPrefix {
    REFRESH("Refresh:");

    private final String description;

    RedisKeyPrefix(String description) {
        this.description = description;
    }

}
