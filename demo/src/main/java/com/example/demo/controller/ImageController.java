package com.example.demo.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.ImageDao;
import com.example.demo.dao.PostDao;
import com.example.demo.entity.Image;
import com.example.demo.entity.Post;

@RestController
@RequestMapping(value = "/api")
public class ImageController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PostDao postDao;

	@Autowired
	private ImageDao imageDao;

	@PostMapping(value = "/image/{postId}")
	public ResponseEntity<?> attachImageInExistingPost(@RequestBody Image image,
			@PathVariable(value = "postId") Long postId) {
		Map<String, Object> response = new HashMap<>();
		try {
			logger.info("inserting image for PostId=" + postId);

			Image img = imageDao.save(image);
			Post post = postDao.findByPk(postId);

			List<Image> imageList = post.getImages();
			imageList.add(img);
			post.setImages(imageList);

			postDao.save(post);
			logger.info("image inserted successfully");

			response.put("response", true);
			response.put("post", post);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("error inserting Image ", e);
			response.put("response", false);
			response.put("Image not added", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping(value = "/image/{imageId}/{postId}")
	public ResponseEntity<?> deleteComment(@PathVariable(value = "postId") Long postId,
			@PathVariable(value = "imageId") Long imageId) {
		Map<String, Object> response = new HashMap<>();
		try {
			logger.info("deleting image for postid= " + postId);
			Post post = postDao.findByPk(postId);
			List<Image> listImg = post.getImages();

			for (int i = 0; i < listImg.size(); i++) {
				if (listImg.get(i).getId() == imageId) {
					listImg.remove(i);
					logger.info("image with id=" + imageId + " deleted successfully for postid=" + postId);
				}
			}
			postDao.save(post);

			response.put("response", true);
			response.put("post", post);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image could not be deleted : " + e.getMessage());
		}
	}

}
