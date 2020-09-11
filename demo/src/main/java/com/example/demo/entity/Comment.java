package com.example.demo.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cm_id")
	private Long id;

	@Column(name = "cm_text")
	private String text;

	@Column(name = "cm_inserted")
	private Date inserted;

	@Column(name = "cm_updated")
	private Date updated;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cm_user")
	private User user;

	@Override
	public String toString() {
		return "Comment [id=" + id + ", text=" + text + ", inserted=" + inserted + ", updated=" + updated + ", user="
				+ user + "]";
	}

}
