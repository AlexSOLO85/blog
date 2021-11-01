package main.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.TagResponse;
import main.model.Tag;
import main.repository.TagRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor

@Mapper(componentModel = "spring")
public abstract class TagMapper {
    /**
     * The Tag repository.
     */
    @Autowired
    protected TagRepository tagRepository;

    /**
     * To tag dto tag response . tags.
     *
     * @param tag the tag
     * @return the tag response . tags
     */
    @Mapping(target = "weight",
            expression = "java((double) tagRepository.getTagCountByTagId"
                    + "(Math.toIntExact(tag.getId())) "
                    + "/ (double) tagRepository.getMaxTagCount())")
    public abstract TagResponse.Tags toTagDTO(Tag tag);

    /**
     * To tag tags.
     *
     * @param tagResponseDTO the tag response dto
     * @return the tags
     */
    public abstract Tag toTag(TagResponse.Tags tagResponseDTO);

    /**
     * To tag dt os list.
     *
     * @param tags the tags
     * @return the list
     */
    public abstract List<TagResponse.Tags> toTagDTOs(List<Tag> tags);
}
