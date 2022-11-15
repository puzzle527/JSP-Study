package com.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.MVCBoardDAO;
import com.dto.MVCBoardDTO;
import com.utils.FileUtil;
import com.utils.JSFunction;

@WebServlet("/PassController")
public class PassController extends HttpServlet 
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		request.setAttribute("mode", request.getParameter("mode"));
		request.getRequestDispatcher("../pass.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		String idx = request.getParameter("idx");
		String mode = request.getParameter("mode");
		String pass = request.getParameter("pass");
		
		//비밀번호 확인
		MVCBoardDAO dao = new MVCBoardDAO();
		boolean confirmed = dao.confirmPassword(pass, idx);
		dao.close();
		
		if(confirmed)
		{
			if("edit".equals(mode))
			{
				HttpSession session = request.getSession();
				session.setAttribute("pass", pass);
				response.sendRedirect("./edit.do?idx=" + idx);
			}
			else if("delete".equals(mode))
			{
				dao = new MVCBoardDAO();
				MVCBoardDTO dto = new MVCBoardDTO();
				int result = dao.deletePost(idx);
				dao.close();
				if(1 == result)
				{
					String saveFileName = dto.getSfile();
					FileUtil.deleteFile(request, "/Uploads", saveFileName);
				}
				JSFunction.alertLocation(response, "삭제되었습니다.", "./list.do");
			}
		}
		else
		{
			JSFunction.alertBack(response, "비밀번호 검증에 실패했습니다.");
		}
	}
}
