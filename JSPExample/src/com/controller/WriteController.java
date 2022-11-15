package com.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.MVCBoardDAO;
import com.dto.MVCBoardDTO;
import com.oreilly.servlet.MultipartRequest;
import com.utils.FileUtil;
import com.utils.JSFunction;

public class WriteController extends HttpServlet 
{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		request.getRequestDispatcher("../write.jsp").forward(request, response);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//1.파일 업로드 처리
		//업로드 디렉터리의 물리적 경로 확인
		String saveDirectory = request.getServletContext().getRealPath("/Uploads");
		System.out.println(saveDirectory);
		
		//초기화 매개변수로 설정한 첨부 파일 최대 용량 확인
		ServletContext application = getServletContext();
		int maxPostSize = Integer.parseInt(application.getInitParameter("maxPostSize"));
		
		//파일 업로드
		MultipartRequest mr = FileUtil.uploadFile(request, saveDirectory, maxPostSize);
		if(mr == null)
		{
			//파일 업로드 실패
			JSFunction.alertLocation(response, "첨부 파일이 제한 용량을 초괴합니다", "mvcboard/write.do");
			return;
		}
		
		//2.파일 업로드 외 처리
		//폼 값을 DTO에 저장
		MVCBoardDTO dto = new MVCBoardDTO();
//		System.out.println(mr.getParameter("name"));
		dto.setName(mr.getParameter("name"));
		dto.setTitle(mr.getParameter("title"));
		dto.setContent(mr.getParameter("content"));
		dto.setPass(mr.getParameter("pass"));
		
		//원본 파일명과 저장된 파일 이름 설정
		String fileName = mr.getFilesystemName("ofile");
		if(fileName != null)
		{
			//첨부 파일이 있을 경우 파일명 변경
			//새로운 파일명 생성
			String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
			String ext = fileName.substring(fileName.lastIndexOf("."));
			String newFileName = now + ext;
			
			//파일명 변경
			File oldFile = new File(saveDirectory + File.separator + fileName);
			File newFile = new File(saveDirectory + File.separator + newFileName);
			oldFile.renameTo(newFile);
			
			dto.setOfile(fileName);
			dto.setSfile(newFileName);
		}
		
		MVCBoardDAO dao = new MVCBoardDAO();
//		System.out.println(dto);
		int result = dao.insertWrite(dto);
		dao.close();
		
		//성공 or 실패?
		if(result == 1) response.sendRedirect("./list.do");
		else response.sendRedirect("./write.do");
	}

}
