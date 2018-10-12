package com.hk.core.autoconfigure.data.jpa;

import com.hk.core.data.jpa.BaseJpaRepositoryFactoryBean;
import com.hk.core.data.jpa.repository.JpaBaseRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <p>
 * JpaAutoConfiguration
 * </p>
 * <p>
 * EnableJpaRepositories
 * basePackages 指定扫描包，所有 repository 都必须放在指定的目录下
 * </p>
 *
 * @author: kevin
 * @date: 2018-06-07 13:07
 */
@EnableJpaAuditing
@ConditionalOnClass(JpaBaseRepository.class)
@EnableJpaRepositories(basePackages = {"com.hk.**.repository.jpa"}, repositoryFactoryBeanClass = BaseJpaRepositoryFactoryBean.class)
public class JpaAutoConfiguration {


}
