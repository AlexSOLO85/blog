package main.api.mapper;

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
public abstract class TagDTO {
    @Autowired
    private TagRepository tagRepository;

    @Mapping(target = "weight",
            expression = "java((double) getTagRepository().getTagCountByTagId"
                    + "(Math.toIntExact(tag.getId())) "
                    + "/ (double) getTagRepository()"
                    + ".getMaxTagCount(org.springframework.data.domain"
                    + ".PageRequest.of(0, 1)))")
    public abstract TagResponse.Tags toTagDTO(Tag tag);

    public abstract Tag toTag(TagResponse.Tags tagResponseDTO);

    public abstract List<TagResponse.Tags> toTagDTOs(List<Tag> tags);
}
