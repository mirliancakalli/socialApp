package com.example.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dao.PostDao;
import com.example.demo.entity.Image;
import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
//import com.example.demo.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/api")
public class PostController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PostDao postDao;

    private PostService postService;

	@GetMapping(value = "/posts")
	public ResponseEntity<?> getAllPost() {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(postDao.findAll());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Posts could not be retreived : " + e.getMessage());
		}
	}

	@GetMapping(value = "/post/{postId}")
	public Post getPostById(@PathVariable(value = "postId") Long postId) {
		logger.info("getting post with id=" + postId);
		return postDao.findByPk(postId);
	}

	@DeleteMapping(value = "/post/{postId}")
	public ResponseEntity<?> deleteUser(@PathVariable(value = "postId") Long postId) {
		logger.error("deleting post =" + postId);
		Post post = postDao.findByPk(postId);
		if (post != null && post.getId() != null) {
			postDao.delete(post);
			return ResponseEntity.status(HttpStatus.OK).body("Post deleted Successfylly : " + post);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("post not deleted Successfylly : " + post.toString());
		}
	}

	@GetMapping(value = "/posts/pagi")
	public ResponseEntity<?> getAllPostPaginated(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			Pageable paging = PageRequest.of(page, size);
			Page<Post> pageTuts = postDao.findAll(paging);

			Map<String, Object> response = new HashMap<>();
			response.put("posts", pageTuts);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/post")
	public Post insertPost(@RequestParam("post") String postS, @RequestParam("image") MultipartFile[] file)
			throws JsonMappingException, JsonProcessingException {
		try {
			Post post = new ObjectMapper().readValue(postS, Post.class);
			List<Image> imageList = post.getImages();
			if (file != null && file.length > 0) {
				for (int i = 0; i < file.length; i++) {
					postService.addPhotoOnServer(post, file[i], imageList, null);
				}
			}
			return postDao.save(post);
		} catch (Exception e) {
			logger.error("error inserting post", e);
			return null;
		}
	}

	@PutMapping(value = "/post")
	public ResponseEntity<?> modifyPost(@RequestBody Post post) {
		Map<String, Object> response = new HashMap<>();
		try {
			Post postExists = postDao.findByPk(post.getId());
			if (postExists != null) {
				post.setUpdated(new Date());
				postDao.save(post);
				logger.info("post updated successfylly");
				return ResponseEntity.status(HttpStatus.OK).body(post);
			} else {
				logger.info("post not exists" + post.getId().toString());
				response.put("response", false);
				response.put("post", " does not exists in database " + post.getId().toString());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("post could not be updated", e);
			response.put("response", false);
			response.put("post", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
