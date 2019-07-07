package club.thisisprettycool.OrdinalBot;

import club.thisisprettycool.OrdinalBot.Commands.Help;
import club.thisisprettycool.OrdinalBot.Commands.Ping;
import club.thisisprettycool.OrdinalBot.Objects.CommandCore;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class MessageListener {
    private List<CommandCore> commandList = new ArrayList<>();

    public MessageListener() {
        getCommands();
    }

    @EventSubscriber
    public void onMessageRecieved(MessageReceivedEvent event) {
        if(!event.getChannel().isPrivate() || !event.getAuthor().isBot()) {
            if(!Main.getDbManager().isBlacklisted(event.getAuthor().getLongID())) {
                String prefix = Main.getDbManager().getPrefix();
                if(event.getMessage().getContent().startsWith("<@"+event.getClient().getOurUser().getLongID()+">")) {
                    event.getChannel().sendMessage("Hey There!\nMy prefix is `"+prefix+"`, use `"+prefix+"`help for commands and their usages");
                    return;
                }
                String[] argArray = event.getMessage().getContent().split(" ");
                if (argArray.length == 0 || !argArray[0].startsWith(prefix)) {
                    return;
                }
                String commandStr = argArray[0].substring(prefix.length());
                for(CommandCore command :commandList) {
                    if(commandStr.toLowerCase().contains(command.getCommandName().toLowerCase()) || command.isAlias(commandStr.toLowerCase())) {
                        if(command.isAdminOnly()) {
                            if(!event.getAuthor().equals(event.getClient().getApplicationOwner()) || !event.getAuthor().getRolesForGuild(event.getGuild()).contains(event.getGuild().getRoleByID(Main.getDbManager().getAdminRole()))) {
                                return;
                            }
                        }
                        System.out.println(event.getAuthor().getName() +" ran command "+commandStr+"\nFull message: "+event.getMessage().getContent());
                        command.executeCommand(event,argArray);
                    }
                }
            }
        }
    }

    private void getCommands() {
        commandList.add(new Ping());

        commandList.add(new Help(commandList));
    }
}
