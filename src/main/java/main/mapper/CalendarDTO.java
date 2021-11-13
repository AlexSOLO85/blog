package main.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.CalendarResponse;
import main.model.Post;
import main.repository.PostRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor

@Mapper(componentModel = "spring")
public abstract class CalendarDTO {
    @Autowired
    private PostRepository postRepository;

    @Mapping(target = "date",
            expression = "java(String.valueOf(post.getTime()))")
    @Mapping(target = "count",
            expression = "java(getPostRepository()."
                    + "getYearsWithAnyPosts().size())")
    public abstract CalendarResponse.Posts toCalendarDTO(Post post);

    public abstract Post toPost(CalendarResponse calendarResponseDTO);

    public abstract List<CalendarResponse
            .Posts> toCalendarDTOs(List<Post> posts);
}
