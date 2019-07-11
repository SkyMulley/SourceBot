package club.thisisprettycool.OrdinalBot;

import club.thisisprettycool.OrdinalBot.Commands.*;
import club.thisisprettycool.OrdinalBot.Objects.CommandCore;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class MessageListener {
    private List<CommandCore> commandList = new ArrayList<>();
    private DiscordClient cli;

    public MessageListener(DiscordClient cli) {
        this.cli = cli;
        getCommands();
        cli.getEventDispatcher().on(MessageCreateEvent.class).subscribe(event -> onMessageRecieved(event));
    }

    public void onMessageRecieved(MessageCreateEvent event) {
        if(!event.getGuildId().isPresent() || !event.getMember().get().isBot()) {
            if(!Main.getDbManager().isBlacklisted(event.getMember().get().getId().asLong())) {
                String prefix = Main.getDbManager().getPrefix();
                String content = event.getMessage().getContent().get();
                if(content.equals("<@"+event.getClient().getSelf().block().getId()+">")) {
                    event.getMessage().getChannel().block().createMessage("Hey There!\nMy prefix is `"+prefix+"`, use `"+prefix+"`help for commands and their usages");
                    return;
                }
                String[] argArray = content.split(" ");
                if (argArray.length == 0 || !argArray[0].startsWith(prefix)) {
                    return;
                }
                String commandStr = argArray[0].substring(prefix.length());
                for(CommandCore command :commandList) {
                    if(commandStr.toLowerCase().contains(command.getCommandName().toLowerCase()) || command.isAlias(commandStr.toLowerCase())) {
                        if(command.isAdminOnly()) {
                            if(!event.getMember().get().getRoleIds().equals(Main.getDbManager().getAdminRole())) {
                                if(!event.getMember().get().equals(event.getClient().getApplicationInfo().block().getOwner().block())) {
                                    return;
                                }
                            }
                        }
                        System.out.println(event.getMember().get().getUsername() +" ran command "+commandStr+"\nFull message: "+event.getMessage().getContent());
                        command.executeCommand(event,argArray,content);
                    }
                }
            }
        }
    }

    private void getCommands() {
        commandList.add(new Ping());
        commandList.add(new Suggest());
        commandList.add(new Query());
        commandList.add(new Settings());

        commandList.add(new Help(commandList));
    }
}
