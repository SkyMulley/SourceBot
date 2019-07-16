package club.thisisprettycool.OrdinalBot.Objects;

import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import discord4j.core.object.entity.Message;

import java.awt.*;
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
            channel.edit(c -> c.setEmbed(embed -> {
                embed.setColor(Color.GREEN);
                embed.setAuthor("Server Status",channel.getClient().getSelf().block().getAvatarUrl(),channel.getClient().getSelf().block().getAvatarUrl());
                embed.addField("Server is online!", "There are currently " + server.getNumOfPlayers() + " players online!", false);
                embed.addField("Server Map", "" + server.getMapName(), false);
                embed.addField("Connect Now!", "steam://connect/"+ip+":"+port, false);
                try {
                    embed.setImage("https://image.gametracker.com/images/maps/160x120/garrysmod/" + server.getMapName() + ".jpg");
                } catch (Exception e) {
                }
                embed.setFooter("Last Refreshed at " + LocalDateTime.now(), channel.getClient().getSelf().block().getAvatarUrl());
            }));
        } catch (Exception e) {
            buildFailedEmbed();
            e.printStackTrace();
        }
    }


    private void buildFailedEmbed() {
        channel.edit(c -> c.setEmbed(embed -> {
            embed.setColor(Color.RED);
            embed.setAuthor("Server Status",channel.getClient().getSelf().block().getAvatarUrl(),channel.getClient().getSelf().block().getAvatarUrl());
            embed.addField("Server is Offline","Check for the server failed, this could mean its offline or there's an issue with the Steam Servers",false);
            embed.setFooter("Last Refreshed at " + LocalDateTime.now(), channel.getClient().getSelf().block().getAvatarUrl());
        }));
    }
}