package com.example.demo.entity;

 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Image")
@Getter
@Setter
@NoArgsConstructor
public class Image {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "im_id")
	private Long id;

	@Column(name = "im_urlImage")
	private String urlImage;
 
//	@JoinColumn(name = "im_post")
//	@ManyToOne(fetch = FetchType.LAZY)
//	private Post post;
	
	@JoinColumn(name = "cm_user")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	 
}
