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

    // TODO 캐시를 탔는지 안 탔는지를 체크하여, 탔을 때는 원래 메소드로 들어오지 않도록 Request 레벨 로직 추가 (인터셉터)
    @Around("@annotation(com.kakaofriends_mockup_project.api.ApiCached)")
    public Object cacheTest(ProceedingJoinPoint joinPoint) throws Throwable {

        // TODO 실제 파라미터 값에 접근하여, 캐시키를 가져오도록 추가
        // TODO 리플렉션 - 패키지명,클래스명,메소드명 네임스페이스 추가 -> ex) /category/items_1596428228035 -> 분기되는 비즈니스 로직의 키를 계속 추가
        // TODO 그 키가 공통이라면, 정의되지 않아도 추가
        String key = getKey(joinPoint);

        CacheValue cachedValue = (CacheValue) CacheUtils.cacheMap.getValue(key);

        if(isCacheMiss(cachedValue)) {                                                      // cache miss
            return joinPoint.proceed();
        }

        Map<String, Object> cachedValueMap = cachedValue.getMap();
        LocalDateTime createdDateTime = (LocalDateTime) cachedValueMap.get("createdTime");

        int expiringMinute = getExpiringMinute(joinPoint);
        // TODO 캐시를 강제로 만드는 방안 고려 (답이 아닐 수도 있음) - 비즈니스 요건을 보고 판단해야 함 - 스프링부트 스케줄링 어노테이션 사용 (1시간? 15분?)

        if(isExpiring(createdDateTime, expiringMinute)) {                                   // cache expire
            // TODO (기존값이 있을 때) remove를 하기 보다는, 만료시점에 있는 value는 그대로 계속 read하고, put된 딱 그 시점에만 락 걸고 업데이트 후 바로 락을 푼다
            // TODO (기존값이 없을 때) 최초요청이 put을 하는 동안, 동일키로 들어온 get들은 대기(혹은 큐잉)
            CacheUtils.cacheMap.remove(key);
            return joinPoint.proceed();
        }

        // FIXME 캐시가 히트됐을 경우 원래 메소드로 가지 않도록 고도화
        return ResponseEntity.ok((String) cachedValueMap.get("data"));                      // cache hit
        // FIXME 캐시가 히트되지 않았을 경우, 실제 메소드를 실행하고, 결과를 받아와서 여기에서 캐시를 써주도록 고도화
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

    // TODO plusMinutes는 set 하는 시점으로 이동하고, createdDateTime가 아니라 expireDateTime
    private boolean isExpiring(LocalDateTime createdDateTime, int expiringMinute) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        return currentDateTime.isAfter(createdDateTime.plusMinutes(expiringMinute));
    }

}
