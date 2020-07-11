package com.mksawic.imageboardapi.post;

import java.util.List;

import com.mksawic.imageboardapi.user.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllBy(Sort sort);
    List<Post> findByAuthor(User author, Pageable pageable);
}