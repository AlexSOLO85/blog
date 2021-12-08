package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SettingsRepository extends PagingAndSortingRepository
        <GlobalSettings, Long> {
}
