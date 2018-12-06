# hk-core
- hk-core-authentication-api 
 - 统一认证接口定义

- hk-core-authentication-security 
  - 使用Spring Security认证，包括Security的自动配置.
  - 如手机号短信登陆、邮箱号登陆等

- hk-core-authentication-security-oauth2 
	- 使用Spring Security oauth2实现单点登陆.

- hk-core-authentication-weixin-mp 
	- 使用Spring Security oauth2实现微信扫码登陆.

- hk-core-cache 
	- 缓存功能的自动配置
Spring 默认需要在标记有缓存注解的方法上定义缓存名 、或在类上定义@CacheConfig 来指定缓存名，
这里只需要在继承 EnableCacheServiceImpl ，并在子类上添加 @CacheConfig指定缓存名，就可以实现缓存的效果，
而不需要在子类的方法上再定义缓存注解.如果是子类调用父类标记有缓存注解的方法，需要使用 getCurrentProxy()方法来调用.

- hk-core-data-commons 
	- Dao commons 包
	- 统一接口定义，已实现 jdbc 、 jpa 、solr 操作数据源
	
- hk-core-data-jdbc  
	- spring-data-jdbc 访问数据库
	- 在 jdbc层再次封装，方便查询

- hk-core-data-jpa  
	- spring-data-jpa 访问数据库
	- 在 jpa 层再次封装，方便查询，项目可根据需要选择使用 jdbc/jpa/mybatis、也可以同时使用三者
	- 应用程序可以很方便的在 jdbc 与 jpa作切换

- hk-core-data-mybatis  
	- mybatis 访问数据库

- hk-core-data-solr 
	- 支持基于 AOP增量导入
	- 使用 spring-data-solr 再次封装，方便查询  

- hk-core-exception 
	- 异常包
	 
- hk-core-redis
	- redis 依赖包
	
- hk-core-stream-kafka
	- kafka 依赖包
	
- hk-core-message-api 
	- 消息统一接口定义
	
- hk-core-message-websocket
	- 支持websocket 推送消息
	
- hk-core-message-weixin
	- 支持发送微信模板消息
	
- hk-core-message-redis
	- redis 发送消息

- hk-core-i18n 
	- 国际化语言支持包
   国际化支持,只需要在application.yml(application.properties) 文件中配置 spring.messages.basenames=i18n/messages 即可，如果有多个国际化文件，使用英文逗号分隔.
 
- hk-core-page
	-  分页包

- hk-core-service
	-  service 访问包，统一事务处理

- hk-core-spring-boot-autoconfigure
	-  spring boot 自动配置包

- hk-core-spring-boot-dependencies
	-  spring boot 自动依赖包

- hk-swagger-ui
	-  swagger接口管理文档模块

- hk-core-test
	-  单元测试块块
