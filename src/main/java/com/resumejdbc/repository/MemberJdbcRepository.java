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
	
	/**
	 * コンストラクタ
	 * @param template JdbcTemplate
	 */
	public MemberJdbcRepository(JdbcTemplate template) {
		this.template = template;
	}

	/**
	 * membersテーブルの全件検索
	 * @return　検索結果
	 * @throws Exception
	 */
	public List<Member> findAll() throws Exception{
		List<Member> members =  template.query(
				"SELECT * FROM members ORDER BY id",
				new BeanPropertyRowMapper<>(Member.class) //戻り値の型
				);
		return members;
	}
	
	/**
	 * 誕生日を条件にmembersテーブルを検索
	 * @param from 検索開始日
	 * @param to 検索終了日
	 * @return　検索結果
	 * @throws Exception
	 */
	public List<Member> findByBirth(LocalDate from, LocalDate to) throws Exception {
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
				new BeanPropertyRowMapper<>(Member.class), //戻り値の型
				args.toArray());
		
		return members;
	}
	
	/**
	 * IDを検索条件にmembersテーブルを検索する
	 * @param id ID
	 * @return　検索結果
	 * @throws Exception
	 */
	public Member findById(Integer id) throws Exception {
		List<Object> args = new ArrayList<>();
		//プレースホルダの設定
		args.add(id);
		List<Member> members = template.query(
				"SELECT * FROM members WHERE id = ?",
				new BeanPropertyRowMapper<>(Member.class), //戻り値の型
				args.toArray());
		// id検索なので1件しかないはず
		Member member = members.get(0);

		return member;
	}
	
	/**
	 * メアドを条件に一致したレコード数を返す
	 * @param email メアド
	 * @param excludeId 検索時に除外する会員ID
	 * @return 件数
	 * @throws Exception
	 */
	public long countByEmail(String email, Integer excludeId) throws Exception {
		List<Object> args = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) FROM members WHERE email = ?");
		//プレースホルダの設定
		args.add(email);
		
		if(Objects.nonNull(excludeId)) {
			//検索対象外のIDが指定されている
			sql.append(" AND id != ?");
			args.add(excludeId);
		}
		
		Long count = template.queryForObject(
				sql.toString(),
				Long.class, //戻り値の型
				args.toArray());
		return count;
	}
	
	/**
	 * レコード挿入
	 * @param member 会員エンティティ
	 * @throws Exception
	 */
	public void insert(Member member) throws Exception {
		List<Object> args = new ArrayList<>();
		//新規登録 プレースホルダの順番にaddすること
		args.add(member.getName());
		args.add(member.getBirth());
		args.add(member.getEmail());
		template.update(
				"INSERT INTO members(name, birth, email) VALUES(?, ?, ?)",
				args.toArray());
	}

	/**
	 * レコード更新
	 * @param member 会員エンティティ
	 * @throws Exception
	 */
	public void update(Member member) throws Exception {
		List<Object> args = new ArrayList<>();
		//更新 プレースホルダの順番にaddすること
		args.add(member.getName());
		args.add(member.getBirth());
		args.add(member.getEmail());
		args.add(member.getId());
		template.update(
				"UPDATE members SET name = ?, birth = ?, email = ? WHERE id = ?",
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
				"DELETE FROM members WHERE id = ?",
				args.toArray());
		
	}

}
