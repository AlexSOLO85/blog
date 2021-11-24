package main.api.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class StatisticsResponse {
    private final Map<String, Object> map;

    public StatisticsResponse(final int postsCount,
                              final int allLikesCount,
                              final int allDislikeCount,
                              final int viewsCount,
                              final long firstPublicationDate) {
        this.map = new HashMap<>();
        map.put("postsCount", postsCount);
        map.put("likesCount", allLikesCount);
        map.put("dislikesCount", allDislikeCount);
        map.put("viewsCount", viewsCount);
        map.put("firstPublication", firstPublicationDate);
    }
}
