package com.example.demo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Post")
@Getter
@Setter
@NoArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pt_id")
	private Long id;

	@Column(name = "pt_text")
	private String text;

	@Column(name = "pt_inserted")
	private Date inserted;

	@Column(name = "pt_updated")
	private Date updated;

	@OneToMany(orphanRemoval = true)
	private List<User> likes = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Image> images = new ArrayList<>();

	@JoinColumn(name = "pt_user")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@Override
	public String toString() {
		return "Post [id=" + id + ", text=" + text + ", inserted=" + inserted + ", updated=" + updated + ", likes="
				+ likes + ", comments=" + comments + ", images=" + images + ", user=" + user + "]";
	}

}
