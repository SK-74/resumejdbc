package com.resumejdbc.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resumejdbc.entity.Member;
import com.resumejdbc.repository.MemberJdbcRepository;
import com.resumejdbc.repository.ResumeJdbcRepository;
import com.resumejdbc.request.MemberSearchCriteria;
import com.resumejdbc.result.Result;

@Service
public class MemberService {

	//会員リポジトリ
	private MemberJdbcRepository memberRepo;
	
	//経歴リポジトリ
	private ResumeJdbcRepository resumeRepo;
	
	//メッセージ
	private MessageSource messageSrc;
	
	/**
	 * コンストラクタ
	 * @param memberRepo 会員リポジトリ
	 * @param resumeRepo 経歴リポジトリ
	 * @param messageSrc メッセージ
	 */
	public MemberService(MemberJdbcRepository memberRepo, ResumeJdbcRepository resumeRepo, MessageSource messageSrc) {
		this.memberRepo = memberRepo;
		this.resumeRepo = resumeRepo;
		this.messageSrc = messageSrc;
	}
	
	/**
	 * 年齢取得
	 * @param id ID
	 * @return
	 * @throws Exception
	 */
	public String getAge(Long id) throws Exception {
		LocalDate today = LocalDate.now();
		Member member = this.findById(id);
		
		long age = ChronoUnit.YEARS.between(member.getBirth(), today);
		
		String ageStr = "-";
		if(age >= 0) {
			ageStr = String.valueOf(age);
		}
		return ageStr;
	}

	/**
	 * 誕生日を条件に会員を検索する
	 * @param criteria 検索条件
	 * @return
	 * @throws Exception
	 */
	public List<Member> findByBirth(MemberSearchCriteria criteria) throws Exception {
		return this.memberRepo.findByBirth(criteria.getDateFrom(), criteria.getDateTo());
	}

	/**
	 * IDを条件に会員を検索する
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Member findById(Long id) throws Exception {
		return this.memberRepo.findById(id);
	}
	
	/**
	 * 会員追加
	 * @param member 会員エンティティ
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Result insertMember(Member member) throws Exception {
		Result result = new Result();
		//メアドの重複登録をガードする
		long count = this.memberRepo.countByEmail(member.getEmail(), null);
		if(count > 0) {
			result.setOk(false);
			//message.propertiesから取得する
			result.setErrMsg(messageSrc.getMessage("err.msg.duplicateemail", null, Locale.getDefault()));
		}else {
			this.memberRepo.insert(member);

			result.setOk(true);
		}
		return result;
	}

	/**
	 * 会員更新
	 * @param member 会員エンティティ
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Result updateMember(Member member) throws Exception {
		Result result = new Result();
		//メアドの重複登録をガードする
		long count = this.memberRepo.countByEmail(member.getEmail(), member.getId());
		if(count > 0) {
			result.setOk(false);
			//message.propertiesから取得する
			result.setErrMsg(messageSrc.getMessage("err.msg.duplicateemail", null, Locale.getDefault()));
		}else {
			this.memberRepo.update(member);

			result.setOk(true);
		}
		return result;
	}
	
	/**
	 * 会員削除（経歴も同時に削除する）
	 * @param id ID
	 * @throws Exception
	 */
	@Transactional
	public void deleteMember(Long id) throws Exception {
		this.resumeRepo.deleteByMemberId(id);
		this.memberRepo.delete(id);
	}

}
