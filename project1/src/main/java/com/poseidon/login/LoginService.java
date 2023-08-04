package com.poseidon.login;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	
	@Autowired
	private LoginDAO loginDAO;

	public LoginDTO login(LoginDTO dto) {
		return loginDAO.login(dto);
	}

	public int join(JoinDTO joinDTO) {
		return loginDAO.join(joinDTO);
	}

	public List<JoinDTO> members() {
		return loginDAO.members();
	}

	public int checkID(String id) {
		return loginDAO.checkID(id);
	}

	public List<Map<String, Object>> boardList2(int i) {
		return loginDAO.boardList2(i);
	}

	public int totalCount() {
		return loginDAO.totalCount();
	}

}
