package main.controller;

import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.services.CalendarService;
import main.services.SettingsService;
import main.services.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagService tagService;

    private final CalendarService calendarService;

    public ApiGeneralController(final InitResponse initResponses,
                                final SettingsService settingsServices,
                                final TagService tagServices,
                                final CalendarService calendarServiceParam) {
        this.initResponse = initResponses;
        this.settingsService = settingsServices;
        this.tagService = tagServices;
        this.calendarService = calendarServiceParam;
    }

    @GetMapping("/init")
    public final ResponseEntity<InitResponse> initResponse() {
        return ResponseEntity.ok(initResponse);
    }

    @GetMapping("/settings")
    public final ResponseEntity<SettingsResponse> settingsResponse() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping(value = "/tag")
    public final ResponseEntity<TagResponse> tagResponse(
            final @RequestParam(defaultValue = "") String query) {
        return tagService.getTagWithQueryResponse(query);
    }

    @GetMapping(value = "/calendar", params = {"year"})
    public final ResponseEntity<CalendarResponse> calendarResponse(
            final @RequestParam(value = "year") Integer year) {
        return calendarService.getPostsByYear(year);
    }
}
