package com.sttproject.app.project;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sttproject.action.Action;
import com.sttproject.action.ActionTo;
import com.sttproject.dao.FileDAO;
import com.sttproject.dao.ProjectDAO;
import com.sttproject.dto.FileDTO;
import com.sttproject.dto.ProjectDTO;
import com.sttproject.dto.ServiceDTO;

public class ProjectListAction implements Action {
	@Override
	public ActionTo execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ProjectDAO pdao = new ProjectDAO();
		String temp = req.getParameter("page");
		String keyword = req.getParameter("keyword");
		FileDAO fdao = new FileDAO();
		
		int page = temp == null ? 1 : Integer.parseInt(req.getParameter("page"));		
		
		//전체 게시글의 개수 ( 총 페이지를 알기 위해서 필요 )
		int totalCnt =	pdao.getProjectCnt(keyword);
		
		//한페이지에 보여줄 게시글 수
		int pageSize = 5;
		
		//페이징처리 		
		int startPage = (page-1)/10*10+1;
		int endPage = startPage + 9;
		int totalPage = (totalCnt-1)/pageSize + 1;
		//가장 마지막 페이지 번호(totalPage)보다 연산으로 구해진 endPage가 더 큰 경우도 있다.(허구의 페이지 번호가 존재할 수 있다)
		//그때는 endPage를 가장 마지막 페이지 번호로 바꿔주어야 한다.
		endPage = endPage>totalPage ? totalPage : endPage;
		
		int startRow = (page - 1)*pageSize;
		
		List<ProjectDTO> list = pdao.getprojectlist(startRow,pageSize,keyword);
		List<FileDTO> flist = fdao.getfilelistwithproject(startRow,pageSize,keyword);
		req.setAttribute("list", list);
		req.setAttribute("flist", flist);
		req.setAttribute("totalPage", totalPage);
		req.setAttribute("totalCnt", totalCnt);
		req.setAttribute("startPage", startPage);
		req.setAttribute("endPage", endPage);
		req.setAttribute("page", page);
		req.setAttribute("keyword", keyword);
				
		ActionTo transfer = new ActionTo();
		transfer.setRedirect(false);
		transfer.setPath("/app/project/projectlist.jsp");
		return transfer;
	}

}
