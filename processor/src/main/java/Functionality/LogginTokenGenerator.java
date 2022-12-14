package Functionality;

import java.time.LocalTime;

/**
 Super simple singleton class that returns a login token based on a username and the current LokalTime
 */

public class LogginTokenGenerator {

    private static final LogginTokenGenerator instance = new LogginTokenGenerator();

    private LogginTokenGenerator() {
    }

    public String makeNewToken(String user) {
        LocalTime time = LocalTime.now();
        return user + time;
    }

    public static LogginTokenGenerator getInstance(){
        return instance;
    }
}
