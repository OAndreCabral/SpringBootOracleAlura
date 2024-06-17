package br.com.alura.ForumApp.repository;

import br.com.alura.ForumApp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
