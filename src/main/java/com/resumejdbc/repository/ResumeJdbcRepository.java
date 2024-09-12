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
	
	public ResumeJdbcRepository(JdbcTemplate template) {
		this.template = template;
	}

	public List<Resume> findByMemberId(Integer id) {
		List<Object> args = new ArrayList<>();
		args.add(id);
		
		// 種別、年、月で並べ替えて検索する
		List<Resume> resumes = template.query(
				"SELECT * FROM resumes WHERE member_id = ? ORDER BY typ, ym",
				new BeanPropertyRowMapper<>(Resume.class),
				args.toArray());

		return resumes;
	}

	public Resume findById(Integer id) {
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
	
	public void insert(Resume resume) {
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
	
	public void update(Resume resume) {
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
	
	public void delete(Integer id) {
		List<Object> args = new ArrayList<>();
		//削除
		args.add(id);
		template.update(
				"DELETE FROM resumes WHERE id = ?",
				args.toArray());
	}
	
	public void deleteByMemberId(Integer memberId) {
		List<Object> args = new ArrayList<>();
		//削除
		args.add(memberId);
		template.update(
				"DELETE FROM resumes WHERE member_id = ?",
				args.toArray());
		
	}

}
