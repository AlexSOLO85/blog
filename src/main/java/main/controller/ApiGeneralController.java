package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Api general controller.
 */
@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    /**
     * The Init response.
     */
    private final InitResponse initResponse;
    /**
     * The Settings service.
     */
    private final SettingsService settingsService;
    /**
     * The Tag service.
     */
    private final TagService tagService;

    /**
     * Instantiates a new Api general controller.
     *
     * @param initResponses    the init response
     * @param settingsServices the settings service
     * @param tagServices      the tag service
     */
    public ApiGeneralController(final InitResponse initResponses,
                                final SettingsService settingsServices,
                                final TagService tagServices) {
        this.initResponse = initResponses;
        this.settingsService = settingsServices;
        this.tagService = tagServices;
    }

    /**
     * Init response init response.
     *
     * @return the init response
     */
    @GetMapping("/init")
    private ResponseEntity<InitResponse> initResponse() {
        return ResponseEntity.ok(initResponse);
    }

    /**
     * Sets response.
     *
     * @return the response
     */
    @GetMapping("/settings")
    private ResponseEntity<SettingsResponse> settingsResponse() {
        return ResponseEntity.ok(settingsService.getGlobalSettings());
    }

    /**
     * Tag response response entity.
     *
     * @param query the query
     * @return the response entity
     */
    @GetMapping("/tag")
    private ResponseEntity<TagResponse> tagResponse(
            final @RequestParam(defaultValue = "") String query) {
        return ResponseEntity.ok((tagService.getTagWithQueryResponse(query)));
    }
}
