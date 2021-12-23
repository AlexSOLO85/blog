package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostAddRequest {
    private LocalDateTime time;
    private byte active;
    private String title;
    private String text;
    private List<String> tags;
}
