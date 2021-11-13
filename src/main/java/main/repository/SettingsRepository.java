package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SettingsRepository extends PagingAndSortingRepository
        <GlobalSettings, Long> {
    @Query(
            value = "SELECT value FROM global_settings where id = 1",
            nativeQuery = true)
    boolean getMultiUserMode();

    @Query(
            value = "SELECT value FROM global_settings where id = 2",
            nativeQuery = true)
    boolean getPostPremoderation();

    @Query(
            value = "SELECT value FROM global_settings where id = 3",
            nativeQuery = true)
    boolean getStatisticsIsPublic();
}
