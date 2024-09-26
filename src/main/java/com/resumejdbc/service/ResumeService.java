package com.resumejdbc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resumejdbc.convertor.LocalDate2YearMonthConvertor;
import com.resumejdbc.convertor.YearMonth2DateConvertor;
import com.resumejdbc.entity.MemberResume;
import com.resumejdbc.entity.Resume;
import com.resumejdbc.repository.ResumeJdbcRepository;

@Service
public class ResumeService {

	//経歴リポジトリ
	private ResumeJdbcRepository resumeRepo;
	
	/**
	 * コンストラクタ
	 * @param resumeRepo 経歴リポジトリ
	 */
	public ResumeService(ResumeJdbcRepository resumeRepo) {
		this.resumeRepo = resumeRepo;
	}
	
	/**
	 * 経歴検索
	 * @param id ID
	 * @return
	 * @throws Exception
	 */
	public List<Resume> findByMemberId(Long id) throws Exception {
		return this.resumeRepo.findByMemberId(id);
	}

	/**
	 * 経歴検索
	 * @param id ID
	 * @return
	 * @throws Exception
	 */
	public Resume findById(Long id) throws Exception {
		Resume resume = this.resumeRepo.findById(id);
		//DBから取得した年月を画面用に型変換する
		LocalDate2YearMonthConvertor convertor = new LocalDate2YearMonthConvertor();
		resume.setRequestYm(convertor.convert(resume.getYm()));
		
		return resume;
	}

	/**
	 * 経歴＋会員検索
	 * @param id ID
	 * @return
	 * @throws Exception
	 */
	public MemberResume findWithMemberById(Long id) throws Exception {
		MemberResume memberResume = this.resumeRepo.findWithMemberById(id);
		//DBから取得した年月を画面用に型変換する
		LocalDate2YearMonthConvertor convertor = new LocalDate2YearMonthConvertor();
		memberResume.setRequestYm(convertor.convert(memberResume.getYm()));
		
		return memberResume;
	}
	
	/**
	 * 経歴追加
	 * @param resume 経歴エンティティ
	 * @throws Exception
	 */
	@Transactional
	public void insertResume(Resume resume) throws Exception {
		//画面からもらった年月をDB用に型変換する
		YearMonth2DateConvertor convertor = new YearMonth2DateConvertor();
		resume.setYm(convertor.convert(resume.getRequestYm()));
		//INSERT文の実行
		this.resumeRepo.insert(resume);
	}

	/**
	 * 経歴更新
	 * @param resume 経歴エンティティ
	 * @throws Exception
	 */
	@Transactional
	public void updateResume(MemberResume resume) throws Exception {
		//画面からもらった年月をDB用に型変換する
		YearMonth2DateConvertor convertor = new YearMonth2DateConvertor();
		resume.setYm(convertor.convert(resume.getRequestYm()));
		//UPDATE文の実行
		this.resumeRepo.update(resume);
	}
	
	/**
	 * 経歴削除
	 * @param id ID
	 * @throws Exception
	 */
	@Transactional
	public void deleteResume(Long id) throws Exception {
		this.resumeRepo.delete(id);
	}

}
