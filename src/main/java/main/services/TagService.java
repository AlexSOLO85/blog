package main.services;

import lombok.RequiredArgsConstructor;
import main.api.response.TagResponse;
import main.mapper.TagDTO;
import main.model.Tag;
import main.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagResponse tagResponse;
    private final TagDTO tagDTO;

    public final ResponseEntity<TagResponse> getTagWithoutQueryResponse() {
        tagResponse.setTags(tagDTO.toTagDTOs(tagRepository
                .getAllTagsListSortedByIdDesc()));
        return new ResponseEntity<>(tagResponse, HttpStatus.OK);
    }

    public final ResponseEntity<TagResponse> getTagWithQueryResponse(
            final String query) {
        if (query == null || query.equals("") || query.isBlank()) {
            return getTagWithoutQueryResponse();
        } else {
            tagResponse.setTags(tagDTO.toTagDTOs(tagRepository
                    .getAllTagsListByQuerySortedByIdDesc(query)));
            return new ResponseEntity<>(tagResponse, HttpStatus.OK);
        }
    }

    public final Tag addTag(final Tag tag) {
        if (tag == null) {
            return null;
        } else {
            return tagRepository.save(tag);
        }
    }
}
