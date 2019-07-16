package club.thisisprettycool.OrdinalBot;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;

public class Main {
    private static DiscordClient cli;
    private static DBManager dbManager;
    public static void main(String[] args) {
        dbManager = new DBManager();
        try {
            Thread.sleep(2000);
        }catch (Exception e) {}
        cli = new DiscordClientBuilder("NTMwNzY4ODQ5NTk1NzkzNDE5.XSecQg.eM8oKfzzmaBEckaknV2ZJLbc2ms").build();
        cli.getEventDispatcher().on(ReadyEvent.class).subscribe(event -> System.out.println("OrdinalBot online! Running as "+event.getSelf().getUsername()));
        new MessageListener(cli);
        cli.login().block();
    }

    public static DBManager getDbManager() {return dbManager;}
}
