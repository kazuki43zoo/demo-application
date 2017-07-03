package com.kazuki43zoo.domain.repository;

import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageParams extends RowBounds implements Pageable {

    private final Pageable pageable;

    public PageParams(Pageable pageable) {
        super(pageable.getOffset(), pageable.getPageSize());
        this.pageable = pageable;
    }

    @Override
    public Pageable first() {
        return pageable.first();
    }

    @Override
    public int getPageNumber() {
        return pageable.getPageNumber();
    }

    @Override
    public int getPageSize() {
        return pageable.getPageSize();
    }

    @Override
    public Sort getSort() {
        return pageable.getSort();
    }

    @Override
    public boolean hasPrevious() {
        return pageable.hasPrevious();
    }

    @Override
    public Pageable next() {
        return pageable.next();
    }

    @Override
    public Pageable previousOrFirst() {
        return pageable.previousOrFirst();
    }

}
