package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Comment;

@Repository
public interface CommentDao extends JpaRepository<Comment, Long> {

	@Query("SELECT p FROM Comment p WHERE p.id =:commentId")
	Comment findByPK(Long commentId);

}
