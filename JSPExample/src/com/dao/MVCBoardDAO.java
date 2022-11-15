package com.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.connection.H2DBConnPool;
import com.dto.MVCBoardDTO;

public class MVCBoardDAO extends H2DBConnPool
{
	public MVCBoardDAO() { super(); }
	
	public int selectCount(Map<String, Object> map) 
	{
		int totalCount = 0;
		
		String sql = "select count(*) from mvcboard";
		
		if(map.get("searchWord") != null) 
		{
			sql += " where " + map.get("searchField") + " " + " like '%" + map.get("searchWord") + "%'";
		}
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			totalCount = rs.getInt(1);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return totalCount;
	}
	public List<MVCBoardDTO> selectListPage(Map<String, Object> map) 
	{
		List<MVCBoardDTO> board = new Vector<MVCBoardDTO>();
		
		String sql = " "
				+ "select * from ( "
				+ " select tb.*, rownum rNum from ( "
				+ " select * from mvcboard ";
		if(map.get("searchWord") != null) 
		{
			sql += " where " + map.get("searchField") + " " + " like '%" + map.get("searchWord") + "%'";
		}
		sql += " order by idx desc "
				+ " ) tb "
				+ " ) "
				+ " where rNum between ? and ?";
		try
		{
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, map.get("start").toString());
			pstmt.setString(2, map.get("end").toString());
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				MVCBoardDTO dto = new MVCBoardDTO();
				
				dto.setIdx(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostdate(rs.getDate(5));
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8));
				dto.setPass(rs.getString(9));
				dto.setVisitcount(rs.getInt(10));
//				System.out.println(dto);
				board.add(dto);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return board;
	}
	public int insertWrite(MVCBoardDTO dto)
	{
		int result = 0;
		System.out.println(dto);
		try 
		{
			String sql = "insert into mvcboard ( "
					+ " name, title, content, ofile, sfile, pass) "
					+ " values (?, ?, ?, ?, ?, ?)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getOfile());
			pstmt.setString(5, dto.getSfile());
			pstmt.setString(6, dto.getPass());
			result = pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	public MVCBoardDTO selectView(String idx) {
		MVCBoardDTO dto = new MVCBoardDTO();
		
		String sql = "select * from mvcboard where idx = ?";
		try
		{
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, idx);
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				dto.setIdx(rs.getString(1));
                dto.setName(rs.getString(2));
                dto.setTitle(rs.getString(3));
                dto.setContent(rs.getString(4));
                dto.setPostdate(rs.getDate(5));
                dto.setOfile(rs.getString(6));
                dto.setSfile(rs.getString(7));
                dto.setDowncount(rs.getInt(8));
                dto.setPass(rs.getString(9));
                dto.setVisitcount(rs.getInt(10));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return dto;
	}
	public void updateVisitCount(String idx) 
	{
		String sql = "update mvcboard set "
				+ " visitcount = visitcount + 1 "
				+ " where idx = ?";
		try
		{
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, idx);
			pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void downCountPlus(String idx) 
	{
		String sql = "update mvcboard set "
				+ " downcount = downcount + 1 "
				+ " where idx = ?";
		try
		{
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, idx);
			pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public boolean confirmPassword(String pass, String idx)
	{
		boolean isCorr = true;
		try
		{
			String sql = "select count(*) from mvcboard where pass = ? and idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, pass);
			pstmt.setString(2, idx);
			rs = pstmt.executeQuery();
			rs.next();
			if(0 == rs.getInt(1))
			{
				isCorr = false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return isCorr;
	}
	public int deletePost(String idx)
	{
		int result = 0;
		try
		{
			String sql = "delete from mvcboard where idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, idx);
			result = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public int updatePost(MVCBoardDTO dto)
	{
		int result = 0;
		try
		{
			String sql = "update mvcboard "
					+ " set title = ?, name = ?, content = ?, ofile = ?, sfile = ? "
					+ " where idx = ? and pass = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getOfile());
			pstmt.setString(5, dto.getSfile());
			pstmt.setString(6, dto.getIdx());
			pstmt.setString(7, dto.getPass());
			
			result = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
