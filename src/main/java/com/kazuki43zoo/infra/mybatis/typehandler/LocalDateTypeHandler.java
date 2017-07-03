package com.kazuki43zoo.infra.mybatis.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.LocalDate;

import java.sql.*;

@MappedTypes(LocalDate.class)
public final class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

    @Override
    public void setNonNullParameter(final PreparedStatement ps, final int i, final LocalDate parameter, final JdbcType jdbcType) throws SQLException {
        ps.setDate(i, new Date(parameter.toDate().getTime()));
    }

    @Override
    public LocalDate getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return toLocalDate(rs.getDate(columnName));
    }

    @Override
    public LocalDate getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return toLocalDate(rs.getDate(columnIndex));
    }

    @Override
    public LocalDate getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return toLocalDate(cs.getDate(columnIndex));
    }

    protected LocalDate toLocalDate(final Date date) {
        if (date == null) {
            return null;
        } else {
            return newLocalDate(date);
        }
    }

    protected LocalDate newLocalDate(final Date date) {
        return new LocalDate(date.getTime());
    }

}