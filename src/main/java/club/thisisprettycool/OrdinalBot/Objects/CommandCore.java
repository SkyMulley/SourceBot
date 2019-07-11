package club.thisisprettycool.OrdinalBot.Objects;

import discord4j.core.event.domain.message.MessageCreateEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommandCore {
    protected String commandName;
    protected String helpMessage;
    protected String usage;
    protected boolean adminOnly = false;
    protected boolean helpViewable = true;
    protected List<String> commandAliases = new ArrayList<>();

    public String getCommandName() {return commandName;}
    public String getHelpMessage() {return helpMessage;}
    public String getUsage() {return usage;}
    public boolean isAdminOnly() {return adminOnly;}
    public boolean isHelpViewable() {return helpViewable;}
    public List<String> getAliases() {return commandAliases;}

    public void addAlias(String command) {commandAliases.add(command);}

    public boolean isAlias(String command) {
        if(commandAliases.contains(command)) {
            return true;
        }
        return false;
    }

    public boolean executeCommand(MessageCreateEvent event, String[] argArray, String content) {return true;}

    public void argsNotFound(MessageCreateEvent event) {
        event.getMessage().getChannel().block().createMessage( s->
                s.setEmbed(embed -> {
                            embed.setAuthor("Whoops", event.getMember().get().getAvatarUrl(), event.getMember().get().getAvatarUrl());
                            embed.setDescription("You haven't supplied the right args in your request. Check with the help command if you are unsure of the usage of this command.");
                            embed.setColor(Color.RED);
                        }));
    }
}
