package com.example.alarms;

public class AppStatus {
    private static int status = 0;

    public static void setStatus(int status) {
        AppStatus.status = status;
    }

    public static void setActive()
    {
        status = 1;
    }

    public static void setInActive()
    {
        status = 0;
    }
    public static int getStatus()
    {
        return status;
    }

}
