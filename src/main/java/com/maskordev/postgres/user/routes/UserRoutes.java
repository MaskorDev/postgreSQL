package com.maskordev.postgres.user.routes;

public class UserRoutes {
    private static final String ROOT = "/api/v1/user";

    public static final String CREATE = ROOT;

    public static final String BY_ID = ROOT + "/{id}";

    public static final String ALL_USERS = ROOT + "/all_users";
}
