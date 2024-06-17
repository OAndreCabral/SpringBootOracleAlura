package br.com.alura.ForumApp.controller;

import br.com.alura.ForumApp.model.Post;
import br.com.alura.ForumApp.repository.PostRepository;
import br.com.alura.ForumApp.model.User;
import br.com.alura.ForumApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Post post, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username).orElseThrow();
        post.setUser(user);
        postRepository.save(post);
        return ResponseEntity.ok("Post created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody Post postDetails, Authentication authentication) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (!postOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Post post = postOptional.get();
        if (!post.getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(403).body("You can only update your own posts");
        }
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        postRepository.save(post);
        return ResponseEntity.ok("Post updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (!postOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Post post = postOptional.get();
        if (!post.getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(403).body("You can only delete your own posts");
        }
        postRepository.delete(post);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
