package main.services;

import lombok.RequiredArgsConstructor;
import main.model.TagToPost;
import main.repository.TagToPostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagToPostService {
    private final TagToPostRepository tagToPostRepository;

    public final TagToPost addTagToPost(final TagToPost tagToPost) {
        if (tagToPost == null) {
            return null;
        } else {
            return tagToPostRepository.save(tagToPost);
        }
    }

    public final void deleteTagToPost(final TagToPost tagToPost) {
        tagToPostRepository.delete(tagToPost);
    }
}
