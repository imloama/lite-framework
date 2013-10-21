package com.itwarcraft.lite.plugin;

import java.sql.Connection;

/**
 * 具体的JDBC实现类，继承该接口
 * @author itwarcraft@gmail.com
 *
 */
public interface JDBCPlugin extends IPlugin{

	public Connection getConnection();

	public void beginTransaction();
	
	public void commitTransaction();
	
	public void rollback();
	
}
