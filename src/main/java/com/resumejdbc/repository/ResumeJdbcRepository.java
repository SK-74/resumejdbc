package com.resumejdbc.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.resumejdbc.entity.MemberResume;
import com.resumejdbc.entity.Resume;

@Repository
public class ResumeJdbcRepository {

	private JdbcTemplate template;
	
	/**
	 * コンストラクタ
	 * @param template JdbcTemplate
	 */
	public ResumeJdbcRepository(JdbcTemplate template) {
		this.template = template;
	}

	/**
	 * 会員IDを検索条件にresumesテーブルを検索する
	 * @param id ID
	 * @return
	 * @throws Exception
	 */
	public List<Resume> findByMemberId(Long id) throws Exception {
		List<Object> args = new ArrayList<>();
		args.add(id);
		
		// 種別、年、月で並べ替えて検索する
		List<Resume> resumes = template.query(
				"SELECT id, typ, member_id, ym, content FROM resumes WHERE member_id = ? ORDER BY typ, ym",
				new BeanPropertyRowMapper<>(Resume.class),
				args.toArray());

		return resumes;
	}

	/**
	 * IDを検索条件にresumesテーブルを検索する
	 * @param id ID
	 * @return
	 * @throws Exception
	 */
	public Resume findById(Long id) throws Exception {
		List<Object> args = new ArrayList<>();
		args.add(id);
		
		//一つだけ検索されるはずだが、queryメソッドの仕様によりListで受け、後で１件抽出する
		List<Resume> resumes = template.query(
				"SELECT id, typ, member_id, ym, content FROM resumes WHERE id = ?",
				new BeanPropertyRowMapper<>(Resume.class),
				args.toArray());
		
		Resume resume = new Resume();
		if(resumes.size() > 0) {
			resume = resumes.getFirst();
		}
		return resume;
	}

	/**
	 * 経歴と紐づく会員を検索する
	 * @param id ID
	 * @return
	 * @throws Exception
	 */
	public MemberResume findWithMemberById(Long id) throws Exception {
		List<Object> args = new ArrayList<>();
		//プレースホルダの設定
		args.add(id);
		//経歴と紐づく会員の取得
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append("   mem.name");
		sb.append("   , mem.id AS member_id");
		sb.append("   , res.id AS resume_id");
		sb.append("   , res.typ");
		sb.append("   , res.ym");
		sb.append("   , res.content");
		sb.append(" FROM resumes res");
		sb.append("   INNER JOIN members mem ON");
		sb.append("   res.member_id = mem.id");
		sb.append(" WHERE");
		sb.append("   res.id = ?");
		List<MemberResume> memberResumes = template.query(
				sb.toString(),
				new BeanPropertyRowMapper<>(MemberResume.class), //戻り値の型
				args.toArray());
		
		MemberResume memberResume = new MemberResume();
		if(memberResumes.size() > 0) {
			// id検索なので1件しかないはず
			memberResume = memberResumes.getFirst();
		}

		return memberResume;
	}
	
	/**
	 * レコード挿入
	 * @param resume 経歴エンティティ
	 * @throws Exception
	 */
	public void insert(Resume resume) throws Exception {
		List<Object> args = new ArrayList<>();
		//新規登録
		args.add(resume.getTyp());
		args.add(resume.getYm());
		args.add(resume.getContent());
		args.add(resume.getMemberId());
		template.update(
				"INSERT INTO resumes(typ, ym, content, member_id) VALUES(?, ?, ?, ?)",
				args.toArray());
	}
	
	/**
	 * レコード更新
	 * @param resume 経歴エンティティ
	 * @throws Exception
	 */
	public void update(MemberResume resume) throws Exception {
		List<Object> args = new ArrayList<>();
		//更新
		args.add(resume.getTyp());
		args.add(resume.getYm());
		args.add(resume.getContent());
		args.add(resume.getResumeId());
		template.update(
				"UPDATE resumes SET typ = ?, ym = ?, content = ? WHERE id = ?",
				args.toArray());
	}
	
	/**
	 * レコード削除
	 * @param id ID
	 * @throws Exception
	 */
	public void delete(Long id) throws Exception {
		List<Object> args = new ArrayList<>();
		//削除
		args.add(id);
		template.update(
				"DELETE FROM resumes WHERE id = ?",
				args.toArray());
	}
	
	/**
	 * 会員IDを条件にレコードを削除する
	 * @param memberId 会員ID
	 * @throws Exception
	 */
	public void deleteByMemberId(Long memberId) throws Exception {
		List<Object> args = new ArrayList<>();
		//削除
		args.add(memberId);
		template.update(
				"DELETE FROM resumes WHERE member_id = ?",
				args.toArray());
		
	}

}
