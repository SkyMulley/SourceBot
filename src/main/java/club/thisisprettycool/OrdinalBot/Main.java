package club.thisisprettycool.OrdinalBot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class Main {
    private static IDiscordClient cli;
    private static DBManager dbManager;
    public static void main(String[] args) {
        cli = new ClientBuilder()
            .withToken("NTMwNzY4ODQ5NTk1NzkzNDE5.XSJZ_g.anysBHal3Bf4-L8_ZG7UGNH4SJ4")
                .build();
        cli.getDispatcher().registerListener(new MessageListener());
        cli.login();
        dbManager = new DBManager();
    }

    public static DBManager getDbManager() {return dbManager;}
}
