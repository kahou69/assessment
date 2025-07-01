package com.example.assessment.etiqa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CacheInspectionService {

    @Autowired
    private CacheManager cacheManager;

    public void printCacheContents(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if(cache != null) {
            System.out.println("Cache Contents : ");
            System.out.println(Objects.requireNonNull(cache.getNativeCache()).toString());
        } else {
            System.out.println("Cache not found");
        }
    }
}
