package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.FileTypeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.ImageDao;
import com.example.demo.dao.PostDao;
import com.example.demo.entity.Image;
import com.example.demo.entity.Post;
import com.example.demo.service.PostService;

@RestController
@RequestMapping(value = "/api")
public class ImageController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PostDao postDao;

	@Autowired
	private ImageDao imageDao;
 
	private PostService postService;
	

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
			post.setUpdated(new Date());
			postDao.save(post);

			response.put("response", true);
			response.put("post", post);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Image could not be deleted : " + e.getMessage());
		}
	}

	@PostMapping(value = "/image/upload/{postId}")
	public ResponseEntity<?> uploadPhoto(@RequestParam("image") MultipartFile[] file,
			@PathVariable(value = "postId") Long postId) {
		Map<String, Object> response = new HashMap<>();
		Post post = postDao.findByPk(postId);
		List<Image> imageList = post.getImages();
		try {
			if (file != null && file.length > 0) {
				for (int i = 0; i < file.length; i++) {
					postService.addPhotoOnServer(post, file[i], imageList, postId);
				}
			}
			post.setImages(imageList);
			post.setUpdated(new Date());
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

	@GetMapping(value = "/download/image/{idImage}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable(value = "idImage") Long idImage) throws IOException {
		Image img = imageDao.findByPk(idImage);
		File file = new File(img.getUrlImage());
		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
				.contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(file)))
				.body(Files.readAllBytes(file.toPath()));
	}
}
