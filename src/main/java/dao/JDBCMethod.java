package dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class JDBCMethod {
	/*
	connection.setAutoCommit(false);
	connection.commit();
	try { connection.rollback(); } catch (Exception e1) { e1.printStackTrace(); }
	 */
	
	//增删改
	public static int update(Connection connection, String sql, Object... objects) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = creratPreparedStatement(connection, sql, objects);
			return preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (preparedStatement != null) try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//增
	public static int insert(Connection connection, String tableName, Map<String, Object> map) {
		String sql = "INSERT " + tableName;
		String valueName = "(";
		String value = "(";
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			valueName += entry.getKey() + ",";
			value += "?,";
		}
		valueName = valueName.substring(0, valueName.length() - 1) + ")";
		value = value.substring(0, value.length() - 1) + ")";
		sql += " " + valueName + " value" + value;
		int num = -1;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			int i = 1;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				preparedStatement.setObject(i, entry.getValue());
				i++;
			}
			num = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return num;
	}
	
	//删
	public static int delete(Connection connection, String tableName, String name, Object value) {
		String sql = "DELETE FROM " + tableName + " WHERE " + name + "=?";
		int num = -1;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setObject(1, value);
			num = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return num;
	}
	
	//改
	public static int set(Connection connection, String tableName, String name, Object value, Map<String, Object> map) {
		String sql = "UPDATE " + tableName + " SET ";
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sql += entry.getKey() + "=? ,";
		}
		sql = sql.substring(0, sql.length() - 1) + "WHERE " + name + "=?";
		int num = -1;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			int i = 1;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				preparedStatement.setObject(i, entry.getValue());
				i++;
			}
			preparedStatement.setObject(map.size() + 1, value);
			num = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return num;
	}
	
	//查所需的整个表
	public static Map<String, Object>[] selectTable(Connection connection, String sql, Object... objects) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = creratPreparedStatement(connection, sql, objects);
			resultSet = preparedStatement.executeQuery();
			resultSet.last();
			Map<String, Object>[] maps = new Map[resultSet.getRow()];
			resultSet.beforeFirst();
			int num = 0;
			String[] columnNames = selectColumnLabels(resultSet);
			while (resultSet.next()) {
				maps[num] = new HashMap<String, Object>();
				for (String columnName : columnNames) maps[num].put(columnName, resultSet.getObject(columnName));
				num++;
			}
			return maps;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (preparedStatement != null) try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (resultSet != null) try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//查所需行
	public static Map<String, Object> selectRow(Connection connection, String sql, Object... objects) {
		try {
			return selectTable(connection, sql, objects)[0];
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//查所需属性或者情况
	public static Object selectValue(Connection connection, String sql, Object... objects) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = creratPreparedStatement(connection, sql, objects);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getObject(1);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (preparedStatement != null) try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (resultSet != null) try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//查表列别名，当没有指定别名时自动用回原名
	public static String[] selectColumnLabels(ResultSet resultSet) throws SQLException {
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		String[] columnLabels = new String[resultSetMetaData.getColumnCount()];
		for (int i = 0; i < columnLabels.length; i++) {
			columnLabels[i] = resultSetMetaData.getColumnLabel(i + 1);
		}
		return columnLabels;
	}
	
	private static PreparedStatement creratPreparedStatement(Connection connection, String sql, Object... objects) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		for (int i = 0; i < objects.length; i++) preparedStatement.setObject(i + 1, objects[i]);
		return preparedStatement;
	}
}
