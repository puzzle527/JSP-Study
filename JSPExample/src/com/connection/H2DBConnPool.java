package com.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class H2DBConnPool {
	public Connection con;
	public Statement stmt;
	public PreparedStatement pstmt;
	public ResultSet rs;
	
	public H2DBConnPool() {
		Context initCtx;
		try 
		{
			initCtx = new InitialContext();
			Context ctx = (Context)initCtx.lookup("java:comp/env");
			DataSource source = (DataSource)ctx.lookup("myH2");
			
			con = source.getConnection();
			
//			System.out.println("커넥션 풀 열기 성공");
		} 
		catch (Exception e) 
		{
//			System.out.println("커넥션 풀 열기 실패");
			e.printStackTrace();
		}
	}
	public void close() {
		try 
		{
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(stmt != null) stmt.close();
			if(con != null) con.close();
//			System.out.println("커넥션 풀 닫기 성공");
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
}
