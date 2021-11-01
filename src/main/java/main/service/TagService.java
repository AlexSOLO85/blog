package main.service;

import main.api.response.TagResponse;
import main.mapper.TagMapper;
import main.repository.TagRepository;
import org.springframework.stereotype.Service;

/**
 * The type Tag service.
 */
@Service
public class TagService {
    /**
     * The Tag repository.
     */
    private final TagRepository tagRepository;
    /**
     * The Tag response.
     */
    private final TagResponse tagResponse;
    /**
     * The Tag mapper.
     */
    private final TagMapper tagMapper;

    /**
     * Instantiates a new Tag service.
     *
     * @param tagRepositoryParam the tag repository
     * @param tagResponseParam   the tag response
     * @param tagMapperParam     the tag mapper param
     */
    public TagService(final TagRepository tagRepositoryParam,
                      final TagResponse tagResponseParam,
                      final TagMapper tagMapperParam) {
        this.tagRepository = tagRepositoryParam;
        this.tagResponse = tagResponseParam;
        this.tagMapper = tagMapperParam;
    }

    /**
     * Gets tag response.
     *
     * @return the tag response
     */
    public TagResponse getTagWithoutQueryResponse() {
        tagResponse.setTags(tagMapper.toTagDTOs(tagRepository
                .getAllTagsListSortedByIdDesc()));
        return tagResponse;
    }

    /**
     * Gets tag with query response.
     *
     * @param query the query
     * @return the tag with query response
     */
    public TagResponse getTagWithQueryResponse(final String query) {
        if (query == null || query.equals("") || query.isBlank()) {
            return getTagWithoutQueryResponse();
        } else {
            tagResponse.setTags(tagMapper.toTagDTOs(tagRepository
                    .getAllTagsListByQuerySortedByIdDesc(query)));
            return tagResponse;
        }
    }
}
