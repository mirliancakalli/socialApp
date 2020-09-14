package com.example.demo.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.entity.Image;
import com.example.demo.entity.Post;

public interface PostService {

	 void addPhotoOnServer(Post post, MultipartFile file, List<Image> imageList,Long postId);
}
