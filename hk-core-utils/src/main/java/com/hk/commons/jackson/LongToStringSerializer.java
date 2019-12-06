package com.hk.commons.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * 将 long 转换为 前端 String，在使用雪花算法生成 的Long 类型id时，防止前端long类型丢失精度
 *
 * @author huangkai
 * @date 2019-08-29 21:28
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LongToStringSerializer extends JsonSerializer<Long> {

    private static LongToStringSerializer INSTANCE = new LongToStringSerializer();

    public static LongToStringSerializer getInstance() {
        return INSTANCE;
    }

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (null != value) {
            gen.writeString(value.toString());
        }
    }
}
