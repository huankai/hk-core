package org.hibernate.id;

import com.hk.commons.util.Lazy;
import com.hk.commons.util.SnowflakeIdGenerator;
import com.hk.commons.util.SnowflakeProperties;
import com.hk.commons.util.SpringContextHolder;
import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;

/**
 * 此类必须要有默认的构造方法，能被实例化
 * hibernate 使用 雪花 算法生成id
 *
 * @author kevin
 * @date 2019-7-2 16:25
 */
@NoArgsConstructor
public class SnowflakeIdentifierGenerator implements IdentifierGenerator {

    private static final Lazy<SnowflakeIdGenerator> SNOWFLAKE_ID_GENERATOR = Lazy.of(() -> SpringContextHolder.getBeanIfExist(SnowflakeProperties.class)
            .map(item -> new SnowflakeIdGenerator(item.getWorkerId(), item.getDataCenterId()))
            .orElse(new SnowflakeIdGenerator()));

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return SNOWFLAKE_ID_GENERATOR.get().generate();
    }

}
