package main.enumerated;

public enum Permission {
    USER("user:write"),
    MODERATOR("moderator:write");

    private final String permissions;

    Permission(final String permissionParam) {
        this.permissions = permissionParam;
    }

    public String getPermission() {
        return permissions;
    }
}
