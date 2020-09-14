package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Image;

@Repository
public interface ImageDao extends JpaRepository<Image, Long> {

	@Query("SELECT p FROM Image p WHERE p.id =:idImage")
	Image findByPk(Long idImage);

}
