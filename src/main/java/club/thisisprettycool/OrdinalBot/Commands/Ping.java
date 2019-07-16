package club.thisisprettycool.OrdinalBot.Commands;

import club.thisisprettycool.OrdinalBot.Objects.CommandCore;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Ping extends CommandCore {
    public Ping () {
        commandName = "Ping";
        helpMessage = "Find out the latency between the server and client";
        usage = "ping";
    }

    @Override
    public boolean executeCommand(MessageCreateEvent event, String[] argArray,String content) {
        try {
            LocalDateTime sentTime = LocalDateTime.ofInstant(event.getMessage().getTimestamp(), ZoneId.systemDefault());
            event.getMessage().getChannel().subscribe(d -> d.createMessage("Waiting for reply..").subscribe(c -> {
                LocalDateTime repliedTime = LocalDateTime.ofInstant(c.getTimestamp(), ZoneId.systemDefault());
                long ping = Duration.between(sentTime, repliedTime).toMillis();
                c.edit(edit -> edit.setContent(String.format("Png! %s (%d ms)", event.getMember().get().getMention(), ping)));
            }));
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}