package main.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.request.SettingsRequest;
import main.api.response.BooleanResponse;
import main.api.response.SettingsResponse;
import main.model.GlobalSettings;
import main.model.User;
import main.repository.SettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Service
@RequiredArgsConstructor
public class SettingsService {
    public static final String MULTIUSER_MODE =
            "MULTIUSER_MODE";
    public static final String POST_PREMODERATION =
            "POST_PREMODERATION";
    public static final String STATISTICS_IS_PUBLIC =
            "STATISTICS_IS_PUBLIC";
    private final SettingsRepository settingsRepository;

    public final ResponseEntity<SettingsResponse> getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(
                settingsRepository.getMultiUserMode());
        settingsResponse.setStatisticsIsPublic(
                settingsRepository.getStatisticsIsPublic());
        settingsResponse.setPostPremoderation(
                settingsRepository.getPostPremoderation());
        return new ResponseEntity<>(settingsResponse,
                HttpStatus.OK);
    }

    public final ResponseEntity<?> setGlobalSettings(
            final SettingsRequest settingsRequest,
            final User user) {
        Boolean multiUserModeSetting =
                settingsRequest.getMultiuserMode();
        Boolean postPremoderationSetting =
                settingsRequest.getPostPremoderation();
        Boolean statisticsIsPublicSetting =
                settingsRequest.getStatisticsIsPublic();
        if (Boolean.FALSE.equals(user.getIsModerator())) {
            return new ResponseEntity<>(new BooleanResponse(false),
                    HttpStatus.BAD_REQUEST);
        }
        HashSet<GlobalSettings> settings =
                (HashSet<GlobalSettings>) getAllGlobalSettingsSet();
        Map<String, Boolean> resultMap = new HashMap<>();
        setNewSettingsAndAddToMap(settings, resultMap, multiUserModeSetting,
                postPremoderationSetting, statisticsIsPublicSetting);
        return new ResponseEntity<>(getGlobalSettings(), HttpStatus.OK);
    }

    public final Set<GlobalSettings> getAllGlobalSettingsSet() {
        return new HashSet<>((Collection<? extends GlobalSettings>)
                settingsRepository.findAll());
    }

    private void setNewSettingsAndAddToMap(
            final HashSet<GlobalSettings> settings,
            final Map<String, Boolean> resultMap,
            final Boolean multiUserModeSetting,
            final Boolean postPremoderationSetting,
            final Boolean statisticsIsPublicSetting) {
        for (GlobalSettings globalSettings : settings) {
            switch (globalSettings.getCode().toUpperCase()) {
                case MULTIUSER_MODE:
                    resultMap.put(MULTIUSER_MODE,
                            multiUserModeSetting != null
                                    ? yesOrNoToBoolean(
                                    setSetting(
                                            globalSettings,
                                            multiUserModeSetting)
                                            .getValue())
                                    : yesOrNoToBoolean(globalSettings
                                    .getValue()));
                    break;
                case POST_PREMODERATION:
                    resultMap.put(POST_PREMODERATION,
                            postPremoderationSetting != null
                                    ? yesOrNoToBoolean(
                                    setSetting(globalSettings,
                                            postPremoderationSetting)
                                            .getValue())
                                    : yesOrNoToBoolean(globalSettings
                                    .getValue()));
                    break;
                case STATISTICS_IS_PUBLIC:
                    resultMap.put(STATISTICS_IS_PUBLIC,
                            statisticsIsPublicSetting != null
                                    ? yesOrNoToBoolean(
                                    setSetting(globalSettings,
                                            statisticsIsPublicSetting)
                                            .getValue())
                                    : yesOrNoToBoolean(globalSettings
                                    .getValue()));
                    break;
                default:
                    throw new IllegalStateException(
                            "Unexpected value: " + globalSettings.getCode()
                                    .toUpperCase());
            }
        }
    }
    private GlobalSettings setSetting(
            final GlobalSettings globalSettings,
            final boolean setting) {
        globalSettings.setValue(convertBooleanToYesOrNo(setting));
        settingsRepository.save(globalSettings);
        return globalSettings;
    }
    private boolean yesOrNoToBoolean(final String yesOrNo) {
        if (yesOrNo.equals("YES")) {
            return true;
        } else if (yesOrNo.equals("NO")) {
            return false;
        }
        return false;
    }
    private String convertBooleanToYesOrNo(
            final boolean bool) {
        if (bool) {
            return "YES";
        } else {
            return "NO";
        }
    }
}
