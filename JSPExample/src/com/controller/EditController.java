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
import javax.servlet.http.HttpSession;

import com.dao.MVCBoardDAO;
import com.dto.MVCBoardDTO;
import com.oreilly.servlet.MultipartRequest;
import com.utils.FileUtil;
import com.utils.JSFunction;

public class EditController extends HttpServlet 
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		String idx = request.getParameter("idx");
		MVCBoardDAO dao = new MVCBoardDAO();
		MVCBoardDTO dto = dao.selectView(idx);
		request.setAttribute("dto", dto);
		request.getRequestDispatcher("../edit.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//1.파일 업로드 처리
		//업로드 디렉터리의 물리적 경로 확인
		String saveDirectory = request.getServletContext().getRealPath("/Uploads");
		
		//초기화 매개변수로 설정한 첨부 파일 최대한 용량 확인
		ServletContext application = getServletContext();
		int maxPostSize = Integer.parseInt(application.getInitParameter("maxPostSize"));
		
		//파일 업로드
		MultipartRequest mr = FileUtil.uploadFile(request, saveDirectory, maxPostSize);
		
		if(mr == null)
		{
			JSFunction.alertBack(response, "첨부 파일이 제한 용량을 초과합니다.");
			return;
		}
		
		//2.파일 업로드 외 처리
		String idx = mr.getParameter("idx");
		String prevOfile = mr.getParameter("prevOfile");
		String prevSfile = mr.getParameter("prevSfile");
		
		String name = mr.getParameter("name");
		String title = mr.getParameter("title");
		String content = mr.getParameter("content");
		
		//비밀번호는 세션에서 가져옴
		HttpSession session = request.getSession();
		String pass = (String)session.getAttribute("pass");
		
		//DTO에 저장
		MVCBoardDTO dto = new MVCBoardDTO();
		dto.setIdx(idx);
		dto.setName(name);
		dto.setTitle(title);
		dto.setContent(content);
		dto.setPass(pass);
		
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
			
			//DTO에 저장
			dto.setOfile(fileName);
			dto.setSfile(newFileName);
			
			//기존 파일 삭제
			FileUtil.deleteFile(request, "/Uploads", prevSfile);
		}
		else 
		{
			dto.setOfile(prevOfile);
			dto.setSfile(prevSfile);
		}
		
		//수정 내용 반영
		MVCBoardDAO dao = new MVCBoardDAO();
		int result = dao.updatePost(dto);
		dao.close();
		
		if (1 == result)
		{
			session.removeAttribute("pass");
			response.sendRedirect("./view.do?idx=" + idx);
		}
		else
		{
			JSFunction.alertLocation(response, "비밀번호 검증을 다시 진행해주세요.", "./view.do?idx=" + idx);
		}
	}

}
