package main.service;

import main.api.response.SettingsResponse;
import main.repository.SettingsRepository;
import org.springframework.stereotype.Service;

/**
 * The type Settings service.
 */
@Service
public class SettingsService {
    /**
     * The Settings repository.
     */
    private final SettingsRepository settingsRepository;

    /**
     * Instantiates a new Settings service.
     *
     * @param settingsRepositoryParam the settings repository
     */
    public SettingsService(final SettingsRepository settingsRepositoryParam) {
        this.settingsRepository = settingsRepositoryParam;
    }

    /**
     * Gets global settings.
     *
     * @return the global settings
     */
    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(
                settingsRepository.getMultiUserMode());
        settingsResponse.setStatisticsIsPublic(
                settingsRepository.getStatisticsIsPublic());
        settingsResponse.setPostPremoderation(
                settingsRepository.getPostPremoderation());
        return settingsResponse;
    }
}
