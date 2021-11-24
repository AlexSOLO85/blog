package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Component
public class CalendarResponse {
    private List<Integer> years;
    private HashMap<String, Integer> posts;

    public CalendarResponse(
            final Map<Date, Integer> postsCountByDate,
            final List<Integer> allYears) {
        years = new LinkedList<>(allYears);
        years.sort(Comparator.comparingInt(o -> o));
        posts = new HashMap<>();
        postsCountByDate.keySet()
                .forEach(d -> posts
                        .put(String.valueOf(d), postsCountByDate.get(d)));
    }
}
