package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Settings repository.
 */
public interface SettingsRepository extends JpaRepository
        <GlobalSettings, Long> {
    /**
     * Gets multi user mode.
     *
     * @return the multi user mode
     */
    @Query(
            value = "SELECT value FROM global_settings where id = 1",
            nativeQuery = true)
    boolean getMultiUserMode();

    /**
     * Gets post premoderation.
     *
     * @return the post premoderation
     */
    @Query(
            value = "SELECT value FROM global_settings where id = 2",
            nativeQuery = true)
    boolean getPostPremoderation();

    /**
     * Gets statistics is public.
     *
     * @return the statistics is public
     */
    @Query(
            value = "SELECT value FROM global_settings where id = 3",
            nativeQuery = true)
    boolean getStatisticsIsPublic();
}
