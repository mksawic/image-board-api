package com.mksawic.imageboardapi.post;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mksawic.imageboardapi.config.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam int page, @RequestParam(required = false) String username) {
        try {
            if (page < 0 ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if(username == null) {
                return new ResponseEntity<List<Post>>(postService.getAllPosts(null, page), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<List<Post>>(postService.getAllPosts(username, page), HttpStatus.OK);   
            }

        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.valueOf(exception.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") String id) {
        Optional<Post> post = postService.getPost(id);

        if (post.isPresent()) {
            return new ResponseEntity<Post>(post.get(), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/voteUp")
    public ResponseEntity<?> voteUp(@RequestParam String id, @RequestParam String authToken) {
        try {
            jwtUtils.validateJwtToken(authToken);
            postService.voteUp(id, jwtUtils.getUserNameFromJwtToken(authToken));
            return ResponseEntity.ok("");
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.valueOf(exception.getMessage()));
        }
    }

    @GetMapping("/voteDown")
    public ResponseEntity<?> voteDown(@RequestParam String id, @RequestParam String authToken) {
        try {
            jwtUtils.validateJwtToken(authToken);
            postService.voteDown(id, jwtUtils.getUserNameFromJwtToken(authToken));
            return ResponseEntity.ok("");
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.valueOf(exception.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<Post> addPost(@RequestParam("title") String title, @RequestParam("image") MultipartFile image,
            @RequestParam("authToken") String authToken) {
        try {
            jwtUtils.validateJwtToken(authToken);
            return new ResponseEntity<Post>(
                    postService.addPost(title, new Date(), 
                    jwtUtils.getUserNameFromJwtToken(authToken), image),
                    HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.valueOf(exception.getMessage()));
        }
    }

    @DeleteMapping()
    public ResponseEntity<String> removePost(@RequestParam("id") String id, @RequestParam("authToken") String authToken) {
        try {
            jwtUtils.validateJwtToken(authToken);
            postService.deletePost(id, jwtUtils.getUserNameFromJwtToken(authToken));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.valueOf(exception.getMessage()));
        }
    }
}