package com.github.kazuki43zoo.infra.mybatis.spring;

import java.beans.Introspector;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.util.ClassUtils;

public class BatchModeRepositoryBeanNameGenerator implements BeanNameGenerator {

    private static final Pattern REPLACEMENT_PATTERN = Pattern.compile("Repository");

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String defaultBeanName = Introspector.decapitalize(ClassUtils.getShortName(definition
                .getBeanClassName()));
        String batchModeRepositoryBeanName = REPLACEMENT_PATTERN.matcher(defaultBeanName)
                .replaceAll("BatchModeRepository");
        return batchModeRepositoryBeanName;
    }
}
