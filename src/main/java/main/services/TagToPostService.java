package main.services;

import main.model.TagToPost;
import main.repository.TagToPostRepository;
import org.springframework.stereotype.Service;

@Service
public class TagToPostService {
    private final TagToPostRepository tagToPostRepository;

    public TagToPostService(
            final TagToPostRepository tagToPostRepositoryParam) {
        this.tagToPostRepository = tagToPostRepositoryParam;
    }

    public final TagToPost addTagToPost(final TagToPost tagToPost) {
        if (tagToPost == null) {
            return null;
        } else {
            return tagToPostRepository.save(tagToPost);
        }
    }
}
