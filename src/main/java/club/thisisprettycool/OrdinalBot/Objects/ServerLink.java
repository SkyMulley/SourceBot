package club.thisisprettycool.OrdinalBot.Objects;

import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import discord4j.core.object.entity.Message;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerLink {
    private String ip;
    private Integer port;
    private SourceQueryClient client;
    private InetSocketAddress server;
    private Message channel;
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    public ServerLink(Message message, String ip , int port) {
        this.channel = message;
        this.ip = ip;
        this.port = port;
        client = new SourceQueryClient();
        server = new InetSocketAddress(ip,port);
        start();
    }
    private void start() {
        service.scheduleAtFixedRate(() -> {
            try {
                client.getServerInfo(server).whenComplete(((sourceServer, serverInfoError) -> {
                    if (serverInfoError != null) {
                        buildFailedEmbed();
                        return;
                    }
                    buildEmbed(sourceServer);
                }));
            } catch (Exception e) {
                e.printStackTrace();
                buildFailedEmbed();
                return;
            }
        }, 1,10, TimeUnit.SECONDS);
    }

    private void buildEmbed(SourceServer server) {
        try {
            EmbedBuilder builder = new EmbedBuilder();
            builder.withColor(0, 255, 0);
            builder.withAuthorName("Server Status");
            builder.appendField("Server is online!", "There are currently " + server.getNumOfPlayers() + " players online!", false);
            builder.appendField("Server Map", "" + server.getMapName(), false);
            builder.appendField("Connect Now!", "steam://connect/"+ip+":"+port, false);
            try {
                builder.withImage("https://image.gametracker.com/images/maps/160x120/garrysmod/" + server.getMapName() + ".jpg");
            } catch (Exception e) {
            }
            builder.withFooterText("Last Refreshed at " + LocalDateTime.now());
            channel.edit(builder.build());
        } catch (Exception e) {
            buildFailedEmbed();
            e.printStackTrace();
        }
    }


    private void buildFailedEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(255,0,0);
        builder.withAuthorName("Server Status");
        builder.appendField("Server is Offline","Check for the server failed, this could mean its offline or there's an issue with the Steam Servers",false);
        builder.withFooterText("Last Refreshed at "+ LocalDateTime.now());
        channel.edit(builder.build());
    }
}