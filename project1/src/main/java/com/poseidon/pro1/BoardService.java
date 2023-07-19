package com.poseidon.pro1;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

@Service("boardService")
public class BoardService {
	
	@Inject
	@Named("boardDAO")
	private BoardDAO boardDAO;

	// 보드 리스트 불러오는 메소드
	public List<Map<String, Object>> boardList() {
		return boardDAO.boardList();
	}

	public BoardDTO detail(String bno) {
		BoardDTO dto = boardDAO.detail(bno);
		//여기서 ip뽑아올 수 있죠?
		String ip = dto.getBip();
		//ip 중간에 하트 넣어주실 수 있죠? 172.30.1.19 ---> 172.♡.1.19
		
		//그거 다시 ip에 저장하실 수 있죠?
		dto.setBip(ip);
		//끝.
		return dto;
	}

	//글쓰기입니다.
	public void write(BoardDTO dto) {
		boardDAO.write(dto);//실행만 시키고 결과를 받지 않습니다. 
		//select를 제외한 나머지는 영향받은 행의 수(int)를 받아오기도 합니다.
	}
	
	
	
	
	
}
