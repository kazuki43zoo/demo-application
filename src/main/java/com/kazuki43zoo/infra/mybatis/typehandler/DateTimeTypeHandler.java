package com.kazuki43zoo.infra.mybatis.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.DateTime;

import java.sql.*;

@MappedTypes(DateTime.class)
public final class DateTimeTypeHandler extends BaseTypeHandler<DateTime> {

    @Override
    public void setNonNullParameter(final PreparedStatement ps, final int i, final DateTime parameter, final JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, new Timestamp(parameter.getMillis()));
    }

    @Override
    public DateTime getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return toDateTime(rs.getTimestamp(columnName));
    }

    @Override
    public DateTime getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return toDateTime(rs.getTimestamp(columnIndex));
    }

    @Override
    public DateTime getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return toDateTime(cs.getTimestamp(columnIndex));
    }

    protected DateTime toDateTime(final Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return newDateTime(timestamp);
        }
    }

    protected DateTime newDateTime(final Timestamp timestamp) {
        return new DateTime(timestamp.getTime());
    }

}