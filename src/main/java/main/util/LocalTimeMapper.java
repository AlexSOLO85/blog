package main.util;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * The type Local time mapper.
 */
@Service
public class LocalTimeMapper {
    /**
     * Date to long.
     *
     * @param time the time
     * @return the long
     */
    public long dateToLong(final LocalDateTime time) {
        return time.toEpochSecond(ZoneOffset.UTC);
    }
}
