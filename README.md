# 카카오 프렌즈 목업 토이 프로젝트

## 뷰캐시 설계
### 1. 로컬캐시 설계
1. 로컬 저장소 만들기
  * WAS의 static 영역에 저장소 올림 
    * 저장소를 어떤 컬렉션을 extends 하여 정의할까? (내가 생각하고 있는 후보 3가지)
      1. HashMap
        - 속도는 가장 빠르다.
        - multi-thread 환경에서 thread-safe를 보장하지 않아 데이터의 무결성이 깨질 수 있다.
      2. HashTable
        - 내부적으로 syncronized를 이용하여 thread-safe를 보장한다.
        - 속도가 느리다.
      3. ConcurrentHashMap
        - 내부적으로 lock을 이용하여 thread-safe를 보장한다.
        - HashMap과 다르게 key,value에 null을 허용하지 않는다.
    * <U>일단, **HashMap**을 택하고 lock을 직접 명시하는 방식으로 선택 (추후에 동시성 테스트를 진행하고 최선의 방식을 택할 것)</U>
    
2. 캐시로직 설계 (CacheMap)
  * **getValue(String key)**
    * key에 해당하는 값을 반환하는 메소드
    * Object 반환
  * **set(String key, Object value)**
    * 데이터를 put하는 부분이 Critical Section 이기 때문에 이 부분을 write-lock 하여 동시성 문제를 해결하려고 시도
  
3. AOP 구현
  * 커스텀 어노테이션 **@ApiCached** 를 정의
  * 메소드 정의에 @ApiCached가 붙은 메소드를 target 으로 하는 AOP 구현
  * cache miss, cache expired, cache hit의 경우를 분기처리
