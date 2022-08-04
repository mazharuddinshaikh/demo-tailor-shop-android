package com.example.demotailorshop.utils;

import com.example.demotailorshop.entity.DressType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class DtsJsonUtils {
    public static <T> T getObject(String jsonString, Class<T> t) {
        T object = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            object = mapper.readValue(jsonString, t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static <T> String getJsonFromObject(T t) {
        String jsonObject = null;
        if (t != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                jsonObject = mapper.writeValueAsString(t);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    public static List<DressType> getDressTypeListFromJson(String jsonString) {
        List<DressType> dressTypeList = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            dressTypeList = mapper.readValue(jsonString, new TypeReference<List<DressType>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return dressTypeList;
    }

}
