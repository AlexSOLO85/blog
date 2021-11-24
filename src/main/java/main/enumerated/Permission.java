package main.enumerated;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    USER("user:write"),
    MODERATOR("moderator:write");

    private final String permissions;

    public String getPermission() {
        return permissions;
    }
}
