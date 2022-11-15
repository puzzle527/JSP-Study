package com.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.MVCBoardDAO;
import com.utils.FileUtil;

@WebServlet("/DownloadController")
public class DownloadController extends HttpServlet 
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		String ofile = request.getParameter("ofile");
		String sfile = request.getParameter("sfile");
		String idx = request.getParameter("idx");
		
		//파일 다운로드
		FileUtil.download(request, response, "/Uploads", sfile, ofile);
		
		//해당 게시물의 다운로드 수 1 증가
		MVCBoardDAO dao = new MVCBoardDAO();
		dao.downCountPlus(idx);
		dao.close();
	}
}
