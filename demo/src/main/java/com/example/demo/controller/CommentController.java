package com.example.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.CommentDao;
import com.example.demo.dao.PostDao;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;

@RestController
@RequestMapping(value = "/api")
public class CommentController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private PostDao postDao;
	
	@PostMapping(value = "/comment/{postId}")
	public ResponseEntity<?> insertComment(@RequestBody Comment comment,@PathVariable(value = "postId")  Long postId){
		Map<String, Object> response = new HashMap<>();
		try {
			logger.info("inserting comment for fostId="+postId);
			comment.setInserted(new Date());
			Comment com = commentDao.save(comment);
			Post post = postDao.findByPk(postId);
			
			List<Comment> commentList= post.getComments();
			commentList.add(com);
			post.setComments(commentList);
			
			postDao.save(post);
			logger.info("comment inserted successfully");

			response.put("response", true);
			response.put("comment added", post);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("error inserting Comment ",e);
 			response.put("response", false);
			response.put("comment not added", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value = "/comment/{commentId}/{postId}")
	public ResponseEntity<?> deleteComment(@PathVariable(value = "postId")  Long postId,@PathVariable(value = "commentId")  Long commentId){
		Map<String, String> response = new HashMap<>();
		try {
			logger.info("deleting comment for postid= "+postId);
			Post post = postDao.findByPk(postId);
			List<Comment> listCom= post.getComments();
			
			for (int i = 0; i < listCom.size(); i++) {
				if(listCom.get(i).getId() == commentId) {
					listCom.remove(i);
					logger.info("comment with id="+commentId+" deleted successfully for postid="+postId);
				}
			}
			postDao.save(post);
			
			response.put("response", "true");
			response.put("comment deleted", post.toString());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Comment could not be deleted : " + e.getMessage());
		}
	}
	
	@PutMapping(value = "/comment")
	public ResponseEntity<?> updateComment(@RequestBody Comment updatedComment){
		Map<String, Object> response = new HashMap<>();
		try {
			if(updatedComment.getId() != null) {
				logger.info("updating comment text for commentId="+updatedComment.getId());
				 
				Comment comToBeUpdated = commentDao.findByPK(updatedComment.getId());
				if(comToBeUpdated!=null) {
					comToBeUpdated.setText(updatedComment.getText());
					comToBeUpdated.setUpdated(new Date());
					
					commentDao.save(comToBeUpdated);
					response.put("response", "true");
					response.put("commentUpdated", comToBeUpdated);
					
					return new ResponseEntity<>(response, HttpStatus.OK);
				}else {
					response.put("response", "false");
					response.put("comment", "no comment found for id="+updatedComment.getId());
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			}else {
				response.put("response", "false");
				response.put("comment", "comment could not be updated for request="+updatedComment.toString());
				return new ResponseEntity<>(response, HttpStatus.OK);			
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Comment could not be updated : " + e.getMessage());
		}
	}
	
}
