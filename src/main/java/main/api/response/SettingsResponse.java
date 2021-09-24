package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * The type Settings response.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SettingsResponse {
    /**
     * The Multiuser mode.
     */
    @JsonProperty("MULTIUSER_MODE")
    private boolean multiuserMode;
    /**
     * The Post premoderation.
     */
    @JsonProperty("POST_PREMODERATION")
    private boolean postPremoderation;
    /**
     * The Statistics is public.
     */
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;
}
