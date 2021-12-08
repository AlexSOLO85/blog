package main.services;

import lombok.RequiredArgsConstructor;
import main.api.response.CalendarResponse;
import main.model.Post;
import main.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final PostRepository postRepository;

    public final ResponseEntity<CalendarResponse> countPostsByYear(
            final String queriedYear) {
        String year = queriedYear == null
                ? String.valueOf(LocalDateTime.now().getYear()) : queriedYear;
        List<Post> postsByYear = postRepository.getPostsByYear(year);
        HashMap<Date, Integer> postsCountByDate = new HashMap<>();
        for (Post p : postsByYear) {
            Date postDate = Date.valueOf(p.getTime().toLocalDate());
            Integer postCount = postsCountByDate.getOrDefault(postDate, 0);
            postsCountByDate.put(postDate, postCount + 1);
        }
        List<Integer> allYears = postRepository.getYearsWithAnyPosts();
        return new ResponseEntity<>(
                new CalendarResponse(postsCountByDate, allYears),
                HttpStatus.OK);
    }
}
