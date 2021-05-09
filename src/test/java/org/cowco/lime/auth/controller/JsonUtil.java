package org.cowco.lime.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;

public class JsonUtil {
    public static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
