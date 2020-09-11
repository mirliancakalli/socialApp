package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
 
	@Query("SELECT p FROM User p WHERE p.id =:id")
    User findByPk(@Param("id") Long parseLong);

}
