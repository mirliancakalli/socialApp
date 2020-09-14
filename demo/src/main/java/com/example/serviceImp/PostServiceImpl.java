package com.example.serviceImp;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dao.ImageDao;
import com.example.demo.entity.Image;
import com.example.demo.entity.Post;
import com.example.demo.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	private final Path root = Paths.get("C:\\Users\\mcakalli1\\Desktop\\images");

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ImageDao imageDao;

	@Override
	public void addPhotoOnServer(Post post, MultipartFile file, List<Image> imageList, Long postId) {
		String insPath = null;
		String postIdValue = (postId == null) ? "new_Post" : postId.toString();
		try {
			String filename = new Date().getTime() + "_" + postIdValue + "_" + file.getOriginalFilename().trim();
			byte[] bytes = file.getBytes();
			insPath = this.root + "\\" + filename;
			Files.write(Paths.get(insPath), bytes);
		} catch (IOException e) {
			logger.error("error editting post to add images", e);
		}
		Image img = new Image();
		img.setUrlImage(insPath);
		img.setUser(post.getUser());
		imageDao.save(img);
		imageList.add(img);
	}
}
