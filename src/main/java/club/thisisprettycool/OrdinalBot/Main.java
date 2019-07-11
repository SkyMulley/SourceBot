package club.thisisprettycool.OrdinalBot;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;

public class Main {
    private static DiscordClient cli;
    private static DBManager dbManager;
    public static void main(String[] args) {
        cli = new DiscordClientBuilder("NTMwNzY4ODQ5NTk1NzkzNDE5.XSJZ_g.anysBHal3Bf4-L8_ZG7UGNH4SJ4").build();
        cli.login().block();
        new MessageListener(cli);
        cli.login();
        dbManager = new DBManager();
    }

    public static DBManager getDbManager() {return dbManager;}
}
