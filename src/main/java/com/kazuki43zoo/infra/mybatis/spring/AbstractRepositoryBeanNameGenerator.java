package com.kazuki43zoo.infra.mybatis.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

public abstract class AbstractRepositoryBeanNameGenerator implements BeanNameGenerator {

    private final String suffix = getClass().getSimpleName().replaceAll("BeanNameGenerator", "");

    @Override
    public String generateBeanName(final BeanDefinition definition, final BeanDefinitionRegistry registry) {
        String defaultBeanName = Introspector.decapitalize(ClassUtils.getShortName(definition.getBeanClassName()));
        return defaultBeanName.replaceAll("Repository", suffix);
    }

}
