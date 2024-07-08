package com.ite.adminservice.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {
    void set(String key, String value);

    String getValue(String key);

    void setTimeToLive(String key, long timeOutInMinutes);

    void setFieldWithTTL(String key, String field, Object value, long timeOutInMinutes);

    void hashSet(String key, String field, Object value);

    boolean hashExists(String key, String field);

    boolean keyExists(String key);

    Object get(String key);

    public Map<String, Object> getField(String key);

    Object hashGet (String key, String field);

    List<Object> hashGetByFieldPrefix(String key, String fieldPrefix);

    Set<String> getFieldPrefies(String key);

    void delete(String key);

    void delete(String key, String field);

    void delete(String key, List<String> fields);

}
