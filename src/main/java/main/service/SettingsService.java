package main.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.request.SettingsRequest;
import main.api.response.BooleanResponse;
import main.model.GlobalSettings;
import main.model.User;
import main.repository.SettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@Service
@RequiredArgsConstructor
public class SettingsService {
    private final SettingsRepository settingsRepository;

    public final ResponseEntity<Map<String, Boolean>> getGlobalSettings() {
        Map<String, Boolean> map =
                StreamSupport
                        .stream(settingsRepository
                                .findAll().spliterator(), false)
                        .collect(Collectors.toMap(GlobalSettings::getCode,
                                globalSettings -> globalSettings.getValue()
                                        .equalsIgnoreCase("YES")));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public final ResponseEntity<?> setGlobalSettings(
            final SettingsRequest settingsRequest,
            final User user) {
        Boolean multiUserMode = settingsRequest.getMultiuserMode();
        Boolean postPremoderation = settingsRequest.getPostPremoderation();
        Boolean statisticsIsPublic = settingsRequest.getStatisticsIsPublic();
        if (Boolean.FALSE.equals(user.getIsModerator())) {
            return new ResponseEntity<>(new BooleanResponse(false),
                    HttpStatus.BAD_REQUEST);
        }
        HashSet<Boolean> result = new HashSet<>();
        setNewSettingsAndAddToMap(settingsRepository.findAll(), result,
                multiUserMode, postPremoderation, statisticsIsPublic);
        return new ResponseEntity<>(getGlobalSettings(), HttpStatus.OK);
    }

    public final Set<GlobalSettings> getAllGlobalSettingsSet() {
        return new HashSet<>((Collection<? extends GlobalSettings>)
                settingsRepository.findAll());
    }

    private void setNewSettingsAndAddToMap(
            final Iterable<GlobalSettings> settings,
            final HashSet<Boolean> result,
            final Boolean multiUserMode,
            final Boolean postPremoderation,
            final Boolean statisticsIsPublic) {
        settings.forEach(globalSettings -> {
            switch (globalSettings.getCode()) {
                case "MULTIUSER_MODE":
                    result.add(multiUserMode != null
                            ? yesOrNoToBoolean(setSetting(
                            globalSettings, multiUserMode).getValue())
                            : yesOrNoToBoolean(globalSettings.getValue()));
                    break;
                case "POST_PREMODERATION":
                    result.add(postPremoderation != null
                            ? yesOrNoToBoolean(setSetting(
                            globalSettings, postPremoderation).getValue())
                            : yesOrNoToBoolean(globalSettings.getValue()));
                    break;
                case "STATISTICS_IS_PUBLIC":
                    result.add(statisticsIsPublic != null
                            ? yesOrNoToBoolean(setSetting(
                            globalSettings, statisticsIsPublic).getValue())
                            : yesOrNoToBoolean(globalSettings.getValue()));
                    break;
                default:
                    throw new IllegalStateException(
                            "Unexpected value: " + globalSettings.getCode()
                                    .toUpperCase());
            }
        });
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
