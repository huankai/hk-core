package com.hk.commons.cglib;

import com.hk.commons.util.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;

import java.util.Optional;

/**
 * @author huangkai
 * @date 2019-10-13 22:19
 */
public class BeanConverter implements Converter {

    /**
     * 目标对象 beanMap
     */
    private final BeanMap targetBeanMap;

    public BeanConverter(Object target) {
        this.targetBeanMap = BeanMap.create(target);
    }

    /**
     * @param value       value
     * @param targetClass 目标类型
     * @param context     set 方法名，如 set属性名
     */
    @Override
    public Object convert(Object value, Class targetClass, Object context) {
        if (value == null
                || value instanceof Optional && ((Optional<?>) value).isEmpty()) {
            return targetBeanMap.get(convertFieldName(context.toString()));
        }
        return value;
    }

    /**
     * setXxx -> xxx
     */
    private String convertFieldName(String setMethod) {
        return StringUtils.uncapitalize(StringUtils.substring(setMethod, 3));
    }


//    public static void main(String[] args) {
//        User user = new User();
//        user.setId(new SecureRandom().nextInt(20));
////        user.setName("admin");
////        BeanCopier beanCopier = BeanCopier.create(User.class, UserVo.class, true);
//        UserVo userVo = new UserVo();
//        userVo.setName("admin");
////        beanCopier.copy(user, userVo, new BeanConverter(userVo));
//        BeanUtils.copicerCopyNotNullProperties(user, userVo);
//        System.out.println(JsonUtils.serialize(user));
//        System.out.println(JsonUtils.serialize(userVo));
//    }
//
//    @Data
//    public static class User {
//        private Integer id;
//
//        private String name;
//
//        public String getNameText() {
//            return name;
//        }
//    }
//
//    @Data
//    public static class UserVo {
//
//        private Integer id;
//
//        private String name;
//
//        private String txt;
//    }
}
