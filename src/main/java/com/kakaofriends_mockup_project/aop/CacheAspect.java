package com.kakaofriends_mockup_project.aop;

import com.kakaofriends_mockup_project.api.ApiCached;
import com.kakaofriends_mockup_project.utils.CacheUtils;
import com.kakaofriends_mockup_project.utils.CacheValue;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
@Aspect
public class CacheAspect {

    @Around("@annotation(com.kakaofriends_mockup_project.api.ApiCached)")
    public Object cacheTest(ProceedingJoinPoint joinPoint) throws Throwable {

        String key = getKey(joinPoint);

        CacheValue cachedValue = (CacheValue) CacheUtils.cacheMap.getValue(key);

        if(isCacheMiss(cachedValue)) {                                                      // cache miss
            return joinPoint.proceed();
        }

        Map<String, Object> cachedValueMap = cachedValue.getMap();
        LocalDateTime createdDateTime = (LocalDateTime) cachedValueMap.get("createdTime");

        int expiringMinute = getExpiringMinute(joinPoint);

        if(isExpiring(createdDateTime, expiringMinute)) {                                   // cache expire
            CacheUtils.cacheMap.remove(key);
            return joinPoint.proceed();
        }

        return ResponseEntity.ok((String) cachedValueMap.get("data"));                      // cache hit
    }

    private String getKey(ProceedingJoinPoint joinPoint) {

        ApiCached anno = getAnnotation(joinPoint);

        return anno.key();
    }

    private int getExpiringMinute(ProceedingJoinPoint joinPoint) {

        ApiCached anno = getAnnotation(joinPoint);

        return anno.ttl();
    }

    private ApiCached getAnnotation(ProceedingJoinPoint joinPoint) {

        Method method = getCurrentMethod(joinPoint);

        return method.getAnnotation(ApiCached.class);
    }

    private Method getCurrentMethod(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        return signature.getMethod();
    }

    private boolean isCacheMiss(CacheValue cachedValue) {

        return isNull(cachedValue);
    }

    private boolean isExpiring(LocalDateTime createdDateTime, int expiringMinute) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        return currentDateTime.isAfter(createdDateTime.plusMinutes(expiringMinute));
    }

}
