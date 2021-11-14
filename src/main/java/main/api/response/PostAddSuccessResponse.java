package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Component
public class PostAddSuccessResponse {
    private boolean result;
}

