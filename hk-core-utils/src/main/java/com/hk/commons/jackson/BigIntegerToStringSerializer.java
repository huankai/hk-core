package com.hk.commons.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

/**
 * 在使用 jdbcSession 查询时，如果指定属性类型为 Serializable （@see  com.hyj.core.commons.ui.tree.BaseTreeNode#value）
 * 使用 主键雪花算法生成id时，查询出来的类型为 BigInteger，返回到前端会丢失精度，这里转换为 String
 *
 * @author kevin
 * @date 2019-9-21 14:02
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BigIntegerToStringSerializer extends JsonSerializer<BigInteger> {

    private static final BigIntegerToStringSerializer instance = new BigIntegerToStringSerializer();

    public static BigIntegerToStringSerializer getInstance() {
        return instance;
    }

    @Override
    public void serialize(BigInteger value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (Objects.nonNull(value)) {
            gen.writeString(value.toString());
        }
    }
}
