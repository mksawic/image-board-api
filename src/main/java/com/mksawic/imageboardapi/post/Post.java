package com.mksawic.imageboardapi.post;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.mksawic.imageboardapi.user.User;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
public class Post {
    private String id;
    private String title;
    private Binary image;
    private Date published;
    private int votes;
    @DBRef
    private User author;

    @DBRef
    private Set<User> votersUp = new HashSet<>();

    @DBRef
    private Set<User> votersDown = new HashSet<>();

    public Post() {
    }

    public Post(String title, Date published) {
        this.title = title;
        this.published = published;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Binary getImage() {
        return image;
    }

    public Date getPublished() {
        return published;
    }

    public int getVotes() {
        return votes;
    }

    public User getAuthor() {
        return author;
    }
    
    public Set<User> getVotersUp() {
        return votersUp;
    }
    public Set<User> getVotersDown() {
        return votersDown;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(Binary image) {
        this.image = image;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
    
    public void setVotersUp(Set<User> votersUp) {
        this.votersUp = votersUp;
    }

    public void setVotersDown(Set<User> votersDown) {
        this.votersDown = votersDown;
    }

    @Override
    public String toString() {
        return 
        "Post [id=" + id 
        + ", title=" + title 
        + ", author=" + author.getUsername()
         + ", published=" + published.toString() 
         + ", votes=" + Integer.toString(votes)
         + ", image=" + image 
         + "]";
    }
}