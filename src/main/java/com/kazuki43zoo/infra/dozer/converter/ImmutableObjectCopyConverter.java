package com.kazuki43zoo.infra.dozer.converter;

import org.dozer.DozerConverter;

public class ImmutableObjectCopyConverter<A, B> extends DozerConverter<A, B> {

    @SuppressWarnings("unchecked")
    public ImmutableObjectCopyConverter(final Class<A> target) {
        super(target, (Class<B>) target);
    }

    @Override
    @SuppressWarnings("unchecked")
    public B convertTo(final A source, final B destination) {
        return (B) source;
    }

    @Override
    @SuppressWarnings("unchecked")
    public A convertFrom(final B source, final A destination) {
        return (A) source;
    }

}
