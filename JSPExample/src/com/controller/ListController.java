package com.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.MVCBoardDAO;
import com.dto.MVCBoardDTO;
import com.utils.BoardPage;

public class ListController extends HttpServlet 
{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		MVCBoardDAO dao = new MVCBoardDAO();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String searchField = request.getParameter("searchField");
		String searchWord = request.getParameter("searchWord");
		if(searchWord != null)
		{
			map.put("searchField", searchField);
			map.put("seachWord", searchWord);
		}
		int totalCount = dao.selectCount(map);
		int pageSize = 5;
		int blockPage = 5;
		
		int pageNum = 1;
		String pageTemp = request.getParameter("pageNum");
		if(pageTemp != null && !"".equals(pageTemp))
		{
			pageNum = Integer.parseInt(pageTemp);
		}
		
		int start = (pageNum - 1) * pageSize + 1;
		int end = pageNum * pageSize;
		map.put("start", start);
		map.put("end", end);
		
		List<MVCBoardDTO> boardList = dao.selectListPage(map);
		dao.close();
		
		String pagingImg = BoardPage.pagingStr(totalCount, pageSize, blockPage, pageNum, "mvcboard/list.do");
		map.put("pagingImg", pagingImg);
		map.put("totalCount", totalCount);
		map.put("pageSize", pageSize);
		map.put("blockPage", blockPage);
		
		request.setAttribute("boardList", boardList);
		request.setAttribute("map", map);
		
		request.getRequestDispatcher("../list.jsp").forward(request, response);
	}
}
