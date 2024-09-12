package com.resumejdbc.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.resumejdbc.entity.Member;

@Repository
public class MemberJdbcRepository {

	private JdbcTemplate template;
	
	public MemberJdbcRepository(JdbcTemplate template) {
		this.template = template;
	}

	public List<Member> findAll(){
		List<Member> members =  template.query(
				"SELECT * FROM members ORDER BY id",
				new BeanPropertyRowMapper<>(Member.class));
		return members;
	}
	
	public List<Member> findByBirth(LocalDate from, LocalDate to) {
		List<Object> args = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM members");
		if(Objects.nonNull(from) && Objects.nonNull(to)) {
			sql.append(" WHERE birth >= ?");
			sql.append(" AND birth <= ?");
			//プレースホルダの設定
			args.add(from);
			args.add(to);
		}else if(Objects.nonNull(from)) {
			sql.append(" WHERE birth >= ?");
			//プレースホルダの設定
			args.add(from);
		}else if(Objects.nonNull(to)) {
			sql.append(" WHERE birth <= ?");
			//プレースホルダの設定
			args.add(to);
		}
		sql.append(" ORDER BY id");
		
		List<Member> members = template.query(
				sql.toString(),
				new BeanPropertyRowMapper<>(Member.class),
				args.toArray());
		
		return members;
	}
	
	public Member findById(Integer id) {
		List<Object> args = new ArrayList<>();
		//プレースホルダの設定
		args.add(id);
		List<Member> members = template.query(
				"SELECT * FROM members WHERE id = ?",
				new BeanPropertyRowMapper<>(Member.class),
				args.toArray());
		// id検索なので1件しかないはず
		Member member = members.get(0);

		return member;
	}
	
	public void insert(Member member) {
		List<Object> args = new ArrayList<>();
		//新規登録
		args.add(member.getName());
		args.add(member.getBirth());
		template.update(
				"INSERT INTO members(name, birth) VALUES(?, ?)",
				args.toArray());
	}

	public void update(Member member) {
		List<Object> args = new ArrayList<>();
		//更新
		args.add(member.getName());
		args.add(member.getBirth());
		args.add(member.getId());
		template.update(
				"UPDATE members SET name = ?, birth = ? WHERE id = ?",
				args.toArray());
	}
	
	public void delete(Integer id) {
		List<Object> args = new ArrayList<>();
		//削除
		args.add(id);
		template.update(
				"DELETE FROM members WHERE id = ?",
				args.toArray());
		
	}

}
