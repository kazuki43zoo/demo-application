package com.kazuki43zoo.infra.mybatis.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.sql.*;

@MappedTypes(LocalTime.class)
public final class LocalTimeTypeHandler extends BaseTypeHandler<LocalTime> {

    private static final LocalDate BASE_DATE = new LocalDate(0);

    @Override
    public void setNonNullParameter(final PreparedStatement ps, int i, final LocalTime parameter, final JdbcType jdbcType) throws SQLException {
        ps.setTime(i, new Time(BASE_DATE.toDateTime(parameter).getMillis()));
    }

    @Override
    public LocalTime getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return toLocalTime(rs.getTime(columnName));
    }

    @Override
    public LocalTime getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return toLocalTime(rs.getTime(columnIndex));
    }

    @Override
    public LocalTime getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return toLocalTime(cs.getTime(columnIndex));
    }

    protected LocalTime toLocalTime(final Time time) {
        if (time == null) {
            return null;
        } else {
            return newLocalTime(time);
        }
    }

    protected LocalTime newLocalTime(final Time time) {
        return new LocalTime(time.getTime());
    }

}