package main.services;

import main.api.response.SettingsResponse;
import main.repository.SettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    private final SettingsRepository settingsRepository;

    public SettingsService(final SettingsRepository settingsRepositoryParam) {
        this.settingsRepository = settingsRepositoryParam;
    }

    public final ResponseEntity<SettingsResponse> getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(
                settingsRepository.getMultiUserMode());
        settingsResponse.setStatisticsIsPublic(
                settingsRepository.getStatisticsIsPublic());
        settingsResponse.setPostPremoderation(
                settingsRepository.getPostPremoderation());
        return new ResponseEntity<>(settingsResponse, HttpStatus.OK);
    }
}
