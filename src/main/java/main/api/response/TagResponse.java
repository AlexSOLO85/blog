package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * The type Tag response.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TagResponse {
    /**
     * The Tags.
     */
    private List<Tag> tags;

    /**
     * The type Tag.
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Tag {
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
