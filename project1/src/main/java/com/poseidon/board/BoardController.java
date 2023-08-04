package com.poseidon.board;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.poseidon.util.Util;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class BoardController {
	// user -> Controller -> Service -> DAO -> mybatis -> DB

	// Autowired말고 Resource로 연결
	@Resource(name = "boardService")
	private BoardService boardService;

	@Autowired
	private Util util; // 우리가 만든 숫자변환을 사용하기 위해서 객체 연결했어요.

	// localhost/board?pageNo=0
	@GetMapping("/board")
	public String board(@RequestParam(value="pageNo", required=false, defaultValue="1") int pageNo, Model model) {
		// 서비스에서 값 가져오기
		// 페이지네이션인포 -> 값 넣고 -> DB -> jsp
		//PaginationInfo에 필수 정보를 넣어 준다.
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(pageNo); //현재 페이지 번호
		paginationInfo.setRecordCountPerPage(10); //한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(10); //페이징 리스트의 사이즈
		//전체 글 수 가져오는 명령문장
		int totalCount = boardService.totalCount();
		paginationInfo.setTotalRecordCount(totalCount);  //전체 게시물 건 수
		
		int firstRecordIndex = paginationInfo.getFirstRecordIndex();//시작위치
		int recordCountPerPage = paginationInfo.getRecordCountPerPage();//페이지당 몇 개?
		
		//System.out.println(firstRecordIndex);
		//System.out.println(recordCountPerPage);
		//System.out.println(pageNo);
		//System.out.println(totalCount);
		
		PageDTO page = new PageDTO();
		page.setFirstRecordIndex(firstRecordIndex);
		page.setRecordCountPerPage(recordCountPerPage);
		
		//보드서비스 수정합니다.
		List<BoardDTO> list = boardService.boardList(page); 
		
		model.addAttribute("list", list);
		//페이징 관련 정보가 있는 PaginationInfo 객체를 모델에 반드시 넣어준다.
		model.addAttribute("paginationInfo", paginationInfo);
		return "board";
	}

	// http://localhost:8080/pro1/detail?bno=121
	// 파라미터로 들어오는 값 잡기
	@GetMapping("/detail") // Model은 jsp에 값을 붙이기 위해서 넣었습니다.
	public String detail(HttpServletRequest request, Model model) {
		// String bno = request.getParameter("bno");
		int bno = util.strToInt(request.getParameter("bno"));

		// bno에 요청하는 값이 있습니다. 이 값을 db까지 보내겠습니다.
		// System.out.println("bno : " + bno);

		// DTO로 변경합니다.
		BoardDTO dto = new BoardDTO();
		dto.setBno(bno);
		// dto.setM_id(null); 글 상세보기에서는 mid가 없어도 됩니다.

		BoardDTO result = boardService.detail(dto);
		model.addAttribute("dto", result);

		return "detail";
	}

	@GetMapping("/write")
	public String write(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("mname") != null) {
			return "write";
		} else {
			return "redirect:/login"; // 슬러시 넣어주세요.
		}
	}

	@PostMapping("/write")
	public String write2(HttpServletRequest request) {// 메소드명에 2를 넣어주세요.

		HttpSession session = request.getSession();
		if (session.getAttribute("mid") != null) {
			// 로그인 했습니다. = 아래 로직을 여기로 가져오세요.
			BoardDTO dto = new BoardDTO();
			dto.setBtitle(request.getParameter("title"));
			dto.setBcontent(request.getParameter("content"));
			// 세션에서 불러오겠습니다.
			dto.setM_id((String) session.getAttribute("mid"));// 세션에서 가져옴
			// dto.setM_name((String) session.getAttribute("mname"));//세션에서 가져옴
			dto.setUuid(UUID.randomUUID().toString());
						
			// Service -> DAO -> mybatis-> DB로 보내서 저장하기
			boardService.write(dto);

			return "redirect:board";// 다시 컨트롤러 지나가기 GET방식으로 갑니다
		} else {
			// 로그인 안 했어요. = 로그인 하세요.
			return "redirect:/login";
		}

	}

	// 삭제가 들어온다면 http://172.30.1.19/delete?bno=150
	// HttpServletRequest의 getParameter();
	@GetMapping("/delete")
	public String delete(@RequestParam(value = "bno", required = true, defaultValue = "0") int bno,
			HttpSession session) {
		// 로그인 여부 확인해주세요.
		// System.out.println("mid : " + session.getAttribute("mid"));

		BoardDTO dto = new BoardDTO();
		dto.setBno(bno);
		dto.setM_id((String) session.getAttribute("mid"));
		// dto.setBwrite(null) 사용자 정보
		// 추후 로그인을 하면 사용자의 정보도 담아서 보냅니다.

		boardService.delete(dto);// 이거 임시로 막았어요. id확인 하시고 풀어주셔야 합니다.

		return "redirect:board";// 삭제를 완료한 후에 다시 보드로 갑니다.
	}

	// 내일은 수정하기, 로그인하기 만들겠습니다. 내일은 시험도 있습니다.
	@GetMapping("/edit")
	public ModelAndView edit(HttpServletRequest request) {
		// 로그인 하지 않으면 로그인 화면으로 던져주세요.
		HttpSession session = request.getSession();
		ModelAndView mv = new ModelAndView();// jsp 값을 비웁니다.

		if (session.getAttribute("mid") != null) {
			// dto를 하나 만들어서 거기에 담겠습니다. = bno, mid
			BoardDTO dto = new BoardDTO();
			dto.setBno(util.strToInt(request.getParameter("bno")));
			// 내글만 수정할 수 있도록 세션에 있는 mid도 보냅니다.
			dto.setM_id((String) session.getAttribute("mid"));

			// 데이터베이스에 bno를 보내서 dto를 얻어옵니다.
			BoardDTO result = boardService.detail(dto);
			// System.out.println("result : " + result);
			if (result != null) { // 내 글을 수정했습니다.
				mv.addObject("dto", result);// mv에 실어보냅니다.
				mv.setViewName("edit");// 이동할 jsp명을 적어줍니다.
			} else {// 다른 사람 글이라면 null입니다. 경고창으로 이동합니다.
				mv.setViewName("warning");
			}

		} else {
			// 로그인 안 했다. = login컨트롤러
			mv.setViewName("redirect:/login");
		}
		return mv;
	}

	@PostMapping("/edit")
	public String edit(BoardDTO dto) {
		// System.out.println("map : " + map);
		// System.out.println(dto.getBtitle());
		// System.out.println(dto.getBcontent());
		// System.out.println(dto.getBno());

		boardService.edit(dto);

		return "redirect:detail?bno=" + dto.getBno(); // 보드로 이동하게 해주세요
	}

	// 내일은 로그인, 보드+사용자정보 게시판 연동
	// CRUD 수정을 합니다.

}
