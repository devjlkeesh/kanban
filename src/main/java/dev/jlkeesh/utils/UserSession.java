package dev.jlkeesh.utils;

import dev.jlkeesh.dto.auth.UserSessionDto;
import dev.jlkeesh.enums.AuthRole;

public class UserSession {

    private static final ThreadLocal<UserSessionDto> users = new ThreadLocal<>();

    public static void set(UserSessionDto userSessionDto) {
        users.set(userSessionDto);
    }

    public static UserSessionDto get() {
        return users.get();
    }

    public static void remove() {
        users.remove();
    }

    public static void set(String decrypt) {
        String[] split = decrypt.split(":");
        set(new UserSessionDto(
                Long.parseLong(split[0]),
                split[1],
                split[2],
                AuthRole.valueOf(split[3])
        ));
    }

    public static Long requireUserId() {
        return get().userId();
    }

}
