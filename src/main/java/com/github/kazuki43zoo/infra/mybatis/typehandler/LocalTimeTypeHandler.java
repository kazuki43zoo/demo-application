package com.github.kazuki43zoo.infra.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

@MappedTypes(LocalTime.class)
public final class LocalTimeTypeHandler extends BaseTypeHandler<LocalTime> {

    private static final LocalDate BASE_DATE = new LocalDate(0);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalTime parameter,
            JdbcType jdbcType) throws SQLException {
        ps.setTime(i, new Time(BASE_DATE.toDateTime(parameter).getMillis()));
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toLocalTime(rs.getTime(columnName));
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toLocalTime(rs.getTime(columnIndex));
    }

    @Override
    public LocalTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toLocalTime(cs.getTime(columnIndex));
    }

    protected LocalTime toLocalTime(Time time) {
        if (time == null) {
            return null;
        } else {
            return newLocalTime(time);
        }
    }

    protected LocalTime newLocalTime(Time time) {
        return new LocalTime(time.getTime());
    }

}