package com.hk.commons.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Set;

/**
 * @author kevin
 * @date 2019-9-3 17:53
 */
public class Jackson2ArrayOrStringDeserializer extends JsonDeserializer<Set<String>> {
    @Override
    public Set<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String valueAsString = p.getValueAsString();
        String text = p.getText();
        return null;
    }

//    public Jackson2ArrayOrStringDeserializer() {
//        super(Set.class);
//    }
//
//    @Override
//    public JavaType getValueType() {
//        return SimpleType.constructUnsafe(String.class);
//    }
//
//    @Override
//    public Set<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
//        jp.getValueAsString();
//        JsonToken token = jp.getCurrentToken();
//        if (token.isScalarValue()) {
//            String list = jp.getText();
//            list = list.replaceAll("\\s+", ",");
//            return new LinkedHashSet<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(list)));
//        }
//        return jp.readValueAs(new TypeReference<Set<String>>() {
//        });
//    }

}
