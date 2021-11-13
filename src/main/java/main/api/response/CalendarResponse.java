package main.api.response;

import lombok.Setter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Component
public class CalendarResponse {
    private List<Integer> years;
    @Setter(value = AccessLevel.NONE)
    private Map<String, Integer> posts;

    public final Map<String, Integer> convertArrayToMap(
            final List<Posts> list) {
        return list.stream()
                .collect(Collectors
                        .toMap(Posts::getDate, Posts::getCount, (a, b) -> b));
    }

    public final void setPosts(final Map<String, Integer> post) {
        this.posts = post;
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Posts {
        private static final String PARSE_DATE_DTO_PATTERN =
                "yyyy-MM-dd";
        public static final String PARSE_DATE_BASE_PATTERN =
                "yyyy-MM-dd'T'HH:mm:ss";
        @Setter(value = AccessLevel.NONE)
        private String date;
        private int count;

        public final void setDate(final String dateParam) {
            try {
                SimpleDateFormat dfBase =
                        new SimpleDateFormat(PARSE_DATE_BASE_PATTERN);
                dfBase.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date dateOfBase = dfBase.parse(dateParam);
                SimpleDateFormat dfDTO =
                        new SimpleDateFormat(PARSE_DATE_DTO_PATTERN);
                this.date = dfDTO.format(dateOfBase);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
