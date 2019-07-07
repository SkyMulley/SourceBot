package club.thisisprettycool.OrdinalBot.Objects;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

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

    public boolean executeCommand(MessageReceivedEvent event,String[] argArray) {return true;}

    public void argsNotFound(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName("Whoops!");
        builder.withDescription("You haven't supplied the right args in your request. Check with the help command if you are unsure of the usage of this command.");
        builder.withColor(255,0,0);
        builder.withAuthorName(event.getAuthor().getName());
        RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
    }
}
