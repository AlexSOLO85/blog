package main.util;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class LocalTimeMapper {
    public final long dateToLong(final LocalDateTime time) {
        return time.toEpochSecond(ZoneOffset.UTC);
    }
}
