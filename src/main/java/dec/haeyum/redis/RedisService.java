package dec.haeyum.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return (String) values.get(key);
    }
    @Transactional(readOnly = true)
    public Boolean isValueInSet(String key, String value){
        Boolean isMember = redisTemplate.opsForSet().isMember(key, value);

        return Boolean.TRUE.equals(isMember);
    }
    public void setValuesInSet(String key, String values){
        redisTemplate.opsForSet().add(key,values);
        redisTemplate.expire(key,Duration.ofDays(1));
    }

    public void setRefreshTokenInString(String key, String values){
        // key = refreshToken::social_sub , Value = "refreshToken"
        String keyValue = "refreshToken::" + key;
        redisTemplate.opsForValue().set(keyValue,values,Duration.ofDays(7));
    }

    public String getRefreshTokenInString(String key){
        String keyValue = "refreshToken::" + key;
        log.info("keyValue ={}", key);
        return  (String) redisTemplate.opsForValue().get(keyValue);
    }



    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public boolean checkExistsValue(String value) {
        return !value.equals("False");
    }

    public void deleteRefreshToken(Long key) {
        String keyValue = "refreshToken::" + key;
        log.info("keyValue ={}", key);
        Boolean isDelete = redisTemplate.delete(keyValue);
        log.info("deleteRefreshToken ={} ",isDelete);

    }

    public void setBlackListOfRefreshToken(String accessToken) {
        String keyValue = "blackList::refreshToken::" + accessToken;
        redisTemplate.opsForValue().set(keyValue,true,Duration.ofDays(7));
    }

    public Boolean getBlackListOfRefreshToken(String accessToken){
        String keyValue = "blackList::refreshToken::" + accessToken;
        Object object = redisTemplate.opsForValue().get(keyValue);

        if (object == null){
            return true;
        }
        return false;
    }
}
