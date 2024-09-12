package com.resumejdbc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resumejdbc.convertor.LocalDate2YearMonthConvertor;
import com.resumejdbc.convertor.YearMonth2DateConvertor;
import com.resumejdbc.entity.Resume;
import com.resumejdbc.repository.ResumeJdbcRepository;

@Service
public class ResumeService {

	private ResumeJdbcRepository resumeRepo;
	
	public ResumeService(ResumeJdbcRepository resumeRepo) {
		this.resumeRepo = resumeRepo;
	}
	
	public List<Resume> findByMemberId(Integer id) {
		return this.resumeRepo.findByMemberId(id);
	}

	public Resume findById(Integer id) {
		Resume resume = this.resumeRepo.findById(id);
		//DBから取得した年月を画面用に型変換する
		LocalDate2YearMonthConvertor convertor = new LocalDate2YearMonthConvertor();
		resume.setRequestYm(convertor.convert(resume.getYm()));
		
		return resume;
	}

	@Transactional
	public void insertResume(Resume resume) {
		//画面からもらった年月をDB用に型変換する
		YearMonth2DateConvertor convertor = new YearMonth2DateConvertor();
		resume.setYm(convertor.convert(resume.getRequestYm()));
		//INSERT文の実行
		this.resumeRepo.insert(resume);
	}

	@Transactional
	public void updateResume(Resume resume) {
		//画面からもらった年月をDB用に型変換する
		YearMonth2DateConvertor convertor = new YearMonth2DateConvertor();
		resume.setYm(convertor.convert(resume.getRequestYm()));
		//UPDATE文の実行
		this.resumeRepo.update(resume);
	}
	
	@Transactional
	public void deleteResume(Integer id) {
		this.resumeRepo.delete(id);
	}

}
