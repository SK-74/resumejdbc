package com.resumejdbc.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resumejdbc.entity.Member;
import com.resumejdbc.repository.MemberJdbcRepository;
import com.resumejdbc.repository.ResumeJdbcRepository;

@Service
public class MemberService {

	private MemberJdbcRepository memberRepo;
	
	private ResumeJdbcRepository resumeRepo;
	
	public MemberService(MemberJdbcRepository memberRepo, ResumeJdbcRepository resumeRepo) {
		this.memberRepo = memberRepo;
		this.resumeRepo = resumeRepo;
	}
	
	public List<Member> findAll(){
		return this.memberRepo.findAll();
	}

	public List<Member> findByBirth(LocalDate from, LocalDate to) {
		return this.memberRepo.findByBirth(from, to);
	}

	public Member findById(Integer id) {
		return this.memberRepo.findById(id);
	}
	
	@Transactional
	public void insertMember(Member member) {
		this.memberRepo.insert(member);
	}

	@Transactional
	public void updateMember(Member member) {
		this.memberRepo.update(member);
	}
	
	@Transactional
	public void deleteMember(Integer id) {
		this.resumeRepo.deleteByMemberId(id);
		this.memberRepo.delete(id);
	}

}
