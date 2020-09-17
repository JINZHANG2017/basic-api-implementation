package com.thoughtworks.rslist.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String getString(Object object) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(object);
        return json;
    }

    public  static <T> T jsonToObject(String json, Class<T> clazz) throws JsonProcessingException {
        Object object = objectMapper.readValue(json, clazz);
        return clazz.cast(object);
    }
}
