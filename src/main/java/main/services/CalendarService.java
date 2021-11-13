package main.services;

import main.api.response.CalendarResponse;
import main.mapper.CalendarDTO;
import main.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {
    private final PostRepository postRepository;
    private final CalendarResponse calendarResponse;
    private final CalendarDTO calendarDTO;

    public CalendarService(final PostRepository postRepositoryParam,
                           final CalendarResponse calendarResponseParam,
                           final CalendarDTO calendarDTOParam) {
        this.postRepository = postRepositoryParam;
        this.calendarResponse = calendarResponseParam;
        this.calendarDTO = calendarDTOParam;
    }

    public final ResponseEntity<CalendarResponse> getPostsByYear(
            final int year) {
        calendarResponse.setYears(postRepository.getYearsWithAnyPosts());
        calendarResponse.setPosts(calendarResponse
                .convertArrayToMap(calendarDTO.toCalendarDTOs(postRepository
                        .getPostsByYear(year))));
        return new ResponseEntity<>(calendarResponse, HttpStatus.OK);
    }
}
