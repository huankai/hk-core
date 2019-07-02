package org.hibernate.id;

import com.hk.commons.util.SnowflakeIdGenerator;
import com.hk.commons.util.SpringContextHolder;
import com.hk.core.data.commons.properties.SnowflakeProperties;
import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;

/**
 * hibernate 使用 雪花 算法生成id
 *
 * @author kevin
 * @date 2019-7-2 16:25
 */
@NoArgsConstructor
public class SnowflakeIdentifierGenerator implements IdentifierGenerator {

    private static SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return getSnowflakeIdGenerator().generate();
    }

    private static synchronized SnowflakeIdGenerator getSnowflakeIdGenerator() {
        if (null == snowflakeIdGenerator) {
            SnowflakeProperties snowflakeProperties = SpringContextHolder.getBean(SnowflakeProperties.class);
            snowflakeIdGenerator = new SnowflakeIdGenerator(snowflakeProperties.getWorkerId(),
                    snowflakeProperties.getDataCenterId());
        }
        return snowflakeIdGenerator;
    }

}
