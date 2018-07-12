# hk-core
- hk-core-authentication-api 认证统计接口定义

- hk-core-authentication-security 使用Spring Security认证，包括Security的自动配置.
- hk-core-cache 缓存功能的自动配置
Spring 默认需要在标记有缓存注解的方法上定义缓存名 、或在类上定义@CacheConfig 来指定缓存名，
这里只需要在继承 EnableCacheServiceImpl ，并在子类上添加 @CacheConfig指定缓存名，就可以实现缓存的效果，
而不需要在子类的方法上再定义缓存注解.如果是子类调用父类标记有缓存注解的方法，需要使用 getCurrentProxy()方法来调用.

- hk-core-cache-redis 缓存的Redis实现.包括自动配置

- hk-core-data 包括实体的定义、Spring-data-jpa 的 audit功能，Spring-jdbc的封装、Spring-data-jpa 查询的封装,包括自动配置,连接池使用druid.

- hk-core-i18n 国际化支持,只需要在application.yml(application.properties) 文件中配置 spring.messages.basenames=i18n/messages 即可，如果有多个国际化文件，使用英文逗号分隔.

- hk-core-rocketmq rocketmq,请忽略

- hk-core-web web 的一些工具类
