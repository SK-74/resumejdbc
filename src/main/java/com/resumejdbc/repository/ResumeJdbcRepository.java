package com.resumejdbc.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
	public List<Resume> findByMemberId(Integer id) throws Exception {
		List<Object> args = new ArrayList<>();
		args.add(id);
		
		// 種別、年、月で並べ替えて検索する
		List<Resume> resumes = template.query(
				"SELECT * FROM resumes WHERE member_id = ? ORDER BY typ, ym",
				new BeanPropertyRowMapper<>(Resume.class),
				args.toArray());

		return resumes;
	}

	/**
	 * IDを検索条件にresumesテーブルを検索する
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Resume findById(Integer id) throws Exception {
		List<Object> args = new ArrayList<>();
		args.add(id);
		
		//一つだけ検索されるはずだが、queryメソッドの仕様によりListで受け、後で１件抽出する
		List<Resume> resumes = template.query(
				"SELECT * FROM resumes WHERE id = ?",
				new BeanPropertyRowMapper<>(Resume.class),
				args.toArray());
		
		Resume resume = new Resume();
		if(resumes.size() > 0) {
			resume = resumes.getFirst();
		}
		return resume;
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
	public void update(Resume resume) throws Exception {
		List<Object> args = new ArrayList<>();
		//更新
		args.add(resume.getTyp());
		args.add(resume.getYm());
		args.add(resume.getContent());
		args.add(resume.getId());
		template.update(
				"UPDATE resumes SET typ = ?, ym = ?, content = ? WHERE id = ?",
				args.toArray());
	}
	
	/**
	 * レコード削除
	 * @param id ID
	 * @throws Exception
	 */
	public void delete(Integer id) throws Exception {
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
	public void deleteByMemberId(Integer memberId) throws Exception {
		List<Object> args = new ArrayList<>();
		//削除
		args.add(memberId);
		template.update(
				"DELETE FROM resumes WHERE member_id = ?",
				args.toArray());
		
	}

}
