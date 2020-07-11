package com.mksawic.imageboardapi.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.mksawic.imageboardapi.user.User;
import com.mksawic.imageboardapi.user.UserRepository;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    public List<Post> getAllPosts(@RequestParam(required = false) String username, @RequestParam int page) {
        List<Post> posts = new ArrayList<Post>();
        Pageable pageableRequest = PageRequest.of(page, 5, Sort.by(Direction.DESC, "published"));
        if (username == null) {
            postRepository.findAll(pageableRequest).forEach(posts::add);
        } else {
            postRepository
                    .findByAuthor(userRepository.findByUsername(username).orElseThrow(
                            () -> new UsernameNotFoundException("No user found with username " + username)), pageableRequest)
                    .forEach(posts::add);
        }
        
        posts.sort(Comparator.comparing(Post::getPublished).reversed());
        return posts;
    }

    public Optional<Post> getPost(String id) {
        return postRepository.findById(id);
    }

    public void voteUp(String id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username" + username));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No post found with id " + id));

        if (post.getVotersUp().remove(user)) {
            post.setVotes(post.getVotes() - 1);
        } else {
            if (post.getVotersDown().remove(user)) {
                post.setVotes(post.getVotes() + 1);
            }
            post.getVotersUp().add(user);
            post.setVotes(post.getVotes() + 1);
        }
        postRepository.save(post);
    }

    public void voteDown(String id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No post found with id " + id));

        if (post.getVotersDown().remove(user)) {
            post.setVotes(post.getVotes() + 1);
        } else {
            if (post.getVotersUp().remove(user)) {
                post.setVotes(post.getVotes() - 1);
            }
            post.getVotersDown().add(user);
            post.setVotes(post.getVotes() - 1);
        }
        postRepository.save(post);
    }

    public Post addPost(@RequestParam String title, @RequestParam Date published, @RequestParam String username,
            @RequestParam MultipartFile image) throws IOException {
        Post post = new Post(title, published);
        post.setImage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
        post.setAuthor(author);
        post.setVotes(0);
        return postRepository.save(post);
    }

    public void deletePost(@RequestParam String id, @RequestParam String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No post found with id " + id));

        if (author.equals(post.getAuthor())) {
            postRepository.deleteById(id);
        } else {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized access.");
        }

    }

}