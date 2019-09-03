package com.hk.commons.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author kevin
 * @date 2019-9-3 17:53
 */
public class Jackson2ArrayOrStringDeserializer extends StdDeserializer<Set<String>> {

    public Jackson2ArrayOrStringDeserializer() {
        super(Set.class);
    }

    @Override
    public JavaType getValueType() {
        return TypeFactory.defaultInstance().constructParametricType(Set.class, String.class);
    }

    @Override
    public Set<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken token = jp.getCurrentToken();
        if (token.isScalarValue()) {
            String list = jp.getText();
            list = list.replaceAll("\\s+", ",");
            return new LinkedHashSet<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(list)));
        }
        return jp.readValueAs(new TypeReference<Set<String>>() {
        });
    }
}
