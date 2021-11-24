package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SettingsRepository extends PagingAndSortingRepository
        <GlobalSettings, Long> {
    @Query(
            value =
                    "SELECT value "
                            + "FROM global_settings "
                            + "WHERE code = 'MULTIUSER_MODE'",
            nativeQuery = true)
    boolean getMultiUserMode();

    @Query(
            value =
                    "SELECT value "
                            + "FROM global_settings "
                            + "WHERE code = 'POST_PREMODERATION'",
            nativeQuery = true)
    boolean getPostPremoderation();

    @Query(
            value =
                    "SELECT value "
                            + "FROM global_settings "
                            + "WHERE code = 'STATISTICS_IS_PUBLIC'",
            nativeQuery = true)
    boolean getStatisticsIsPublic();
}
