package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Tag response.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Component
public class TagResponse {
    /**
     * The Tag.
     */
    private List<Tags> tags;

    /**
     * The type Tag.
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Tags {
        /**
         * The Name.
         */
        private String name;
        /**
         * The Weight.
         */
        private double weight;
    }
}
