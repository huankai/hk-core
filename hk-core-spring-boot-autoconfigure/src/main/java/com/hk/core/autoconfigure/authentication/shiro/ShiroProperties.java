package com.hk.core.autoconfigure.authentication.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangkai
 * @date 2018-12-25 09:20
 */
@Data
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private Map<String, List<String>> filterChainDefinition = new LinkedHashMap<>();

    public Map<String, String> getFilterChainDefinitionMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : filterChainDefinition.entrySet()) {
            List<String> values = entry.getValue();
            String key = entry.getKey();
            for (String item : values) {
                map.put(item, key);
            }
        }
        return map;
    }
}
