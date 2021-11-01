package main.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The type Bad request message response.
 */
@Getter
@Setter
public class BadResponse {
    /**
     * The Result.
     */
    private boolean result;
    /**
     * The Message.
     */
    private String message;

    /**
     * Instantiates a new Bad request message response.
     *
     * @param args the args
     */
    public BadResponse(final String... args) {
        result = false;
        message = Arrays.stream(args).filter(s ->
                        (s != null && !s.isBlank()) && !s.equalsIgnoreCase(""))
                .collect(Collectors.joining(". "));
    }
}
