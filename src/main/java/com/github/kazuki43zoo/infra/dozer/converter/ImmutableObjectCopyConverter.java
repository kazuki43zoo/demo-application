package com.github.kazuki43zoo.infra.dozer.converter;

import org.dozer.DozerConverter;

public class ImmutableObjectCopyConverter<A, B> extends DozerConverter<A, B> {

    @SuppressWarnings("unchecked")
    public ImmutableObjectCopyConverter(Class<A> target) {
        super(target, (Class<B>) target);
    }

    @Override
    @SuppressWarnings("unchecked")
    public B convertTo(A source, B destination) {
        return (B) source;
    }

    @Override
    @SuppressWarnings("unchecked")
    public A convertFrom(B source, A destination) {
        return (A) source;
    }

}
