package com.im.imparty.handler;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FastJsonArrayTypeHandler extends BaseTypeHandler<JSONArray> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONArray jsonArray, JdbcType jdbcType)
            throws SQLException {
        String jsonString = jsonArray.toJSONString();
        ps.setString(i, jsonString);
    }

    @Override
    public JSONArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonString = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return JSONArray.parseArray(jsonString);
    }

    @Override
    public JSONArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonString = rs.getString(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return JSONArray.parseArray(jsonString);
    }

    @Override
    public JSONArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonString = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        return JSONArray.parseArray(jsonString);
    }
}