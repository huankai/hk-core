# hk-core
- hk-core-authentication-api 认证统计接口定义

- hk-core-authentication-security 使用Spring Security认证，包括Security的自动配置.

- hk-core-authentication-security-oauth2 使用Spring Security oauth2实现单点登陆.

- hk-core-cache 缓存功能的自动配置
Spring 默认需要在标记有缓存注解的方法上定义缓存名 、或在类上定义@CacheConfig 来指定缓存名，
这里只需要在继承 EnableCacheServiceImpl ，并在子类上添加 @CacheConfig指定缓存名，就可以实现缓存的效果，
而不需要在子类的方法上再定义缓存注解.如果是子类调用父类标记有缓存注解的方法，需要使用 getCurrentProxy()方法来调用.

- hk-core-data-commons Dao commons 包

- hk-core-data-jdbc  jdbc访问数据库，未实现

- hk-core-data-jpa  jpa访问数据库，已实现

- hk-core-data-mybatis  mybatis访问数据库，已实现

- hk-core-solr solr 增量导入使用 aop 实现 

- hk-core-exception 异常包 

- hk-core-i18n 国际化语言支持包
    国际化支持,只需要在application.yml(application.properties) 文件中配置 spring.messages.basenames=i18n/messages 即可，如果有多个国际化文件，使用英文逗号分隔.
 
- hk-core-page 分页包

- hk-core-service service 访问包，统一事务处理

- hk-core-spring-boot-autoconfigure spring boot 自动配置包

- hk-core-spring-boot-dependencies spring boot 自动依赖包
