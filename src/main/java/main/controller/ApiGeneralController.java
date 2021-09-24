package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/init")
    private InitResponse initResponse() {
        return initResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settingsResponse() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/tag")
    private TagResponse tagResponse() {
        return tagService.getTagResponse();
    }
}
