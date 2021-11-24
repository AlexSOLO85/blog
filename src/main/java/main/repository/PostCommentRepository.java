package main.repository;

import main.model.PostComment;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostCommentRepository
        extends PagingAndSortingRepository<PostComment, Long> {
}
