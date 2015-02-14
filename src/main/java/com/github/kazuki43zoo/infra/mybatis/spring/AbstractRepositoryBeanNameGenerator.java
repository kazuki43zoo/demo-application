package com.github.kazuki43zoo.infra.mybatis.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

public abstract class AbstractRepositoryBeanNameGenerator implements BeanNameGenerator {

    private final String suffix;

    protected AbstractRepositoryBeanNameGenerator() {
        this.suffix = getClass().getSimpleName().replaceAll("BeanNameGenerator", "");
    }

    @Override
    public String generateBeanName(
            BeanDefinition definition,
            BeanDefinitionRegistry registry) {
        String defaultBeanName = Introspector.decapitalize(ClassUtils.getShortName(definition
                .getBeanClassName()));
        String repositoryBeanName = defaultBeanName.replaceAll("Repository", suffix);
        return repositoryBeanName;
    }

}
