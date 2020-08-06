package com.kakaofriends_mockup_project.utils;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class CacheValue {

    private Map<String, Object> map;

    @Builder
    public CacheValue(Map<String, Object> map) {
        this.map = map;
    }

}
