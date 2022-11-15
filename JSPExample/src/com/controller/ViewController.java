package com.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.MVCBoardDAO;
import com.dto.MVCBoardDTO;

public class ViewController extends HttpServlet 
{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		MVCBoardDAO dao = new MVCBoardDAO();
		String idx = request.getParameter("idx");
		dao.updateVisitCount(idx);
		MVCBoardDTO dto = dao.selectView(idx);
		dao.close();
		
		dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
		
		request.setAttribute("dto", dto);
		request.getRequestDispatcher("../view.jsp").forward(request, response);
	}
}
