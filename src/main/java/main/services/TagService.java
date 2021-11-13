package main.services;

import main.api.response.TagResponse;
import main.mapper.TagDTO;
import main.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final TagResponse tagResponse;
    private final TagDTO tagDTO;

    public TagService(final TagRepository tagRepositoryParam,
                      final TagResponse tagResponseParam,
                      final TagDTO tagDTOParam) {
        this.tagRepository = tagRepositoryParam;
        this.tagResponse = tagResponseParam;
        this.tagDTO = tagDTOParam;
    }

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
}
