package com.b07group47.taamcollectionmanager;

public class UserState {
    private static boolean isAdmin = false;

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void setIsAdmin(boolean isAdmin) {
        UserState.isAdmin = isAdmin;
    }
}
