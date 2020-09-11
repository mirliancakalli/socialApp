package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Post;

@Repository
public interface PostDao extends JpaRepository<Post, Long>{

	@Query("SELECT p FROM Post p WHERE p.id =:postId")
	Post findByPk(Long postId);

}
