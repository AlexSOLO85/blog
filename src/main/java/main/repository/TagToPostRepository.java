package main.repository;

import main.model.TagToPost;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagToPostRepository
        extends PagingAndSortingRepository<TagToPost, Long> {
}
