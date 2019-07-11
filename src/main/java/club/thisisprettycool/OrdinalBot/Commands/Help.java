package club.thisisprettycool.OrdinalBot.Commands;

import club.thisisprettycool.OrdinalBot.Main;
import club.thisisprettycool.OrdinalBot.Objects.CommandCore;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.Embed;
import discord4j.core.spec.EmbedCreateSpec;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class Help extends CommandCore {
    private static String message = "";
    private List<CommandCore> GC;
    public Help(List<CommandCore> GC) {
        commandName = "Help";
        helpViewable = false;
        this.GC = GC;
    }

    @Override
    public boolean executeCommand(MessageCreateEvent event, String[] argsArray, String content) {
        Consumer<EmbedCreateSpec> template = embed -> {
            embed.setAuthor("Help Menu",event.getClient().getSelf().block().getAvatarUrl(),event.getClient().getSelf().block().getAvatarUrl());
            embed.setColor(Color.WHITE);
        };
        String prefix = Main.getDbManager().getPrefix();
        if(argsArray.length==2) {
            boolean found = false;
            for(CommandCore GG : GC) {
                if (GG.getCommandName().equalsIgnoreCase(argsArray[1]) || GG.isAlias(argsArray[1])) {
                    if(GG.isHelpViewable()) {
                        template.andThen(embed -> {
                                    embed.setDescription("More help for the command `" + GG.getCommandName() + "`");
                                    embed.addField("Help Message", GG.getHelpMessage(), false);
                                    embed.addField("Usage", prefix + GG.getUsage(), false);
                        });
                        message = "`";
                        if(GG.getAliases().size()!=0) {
                            for (String alias : GG.getAliases()) {
                                message = message + alias + "\n";
                            }
                            message = message + "`";
                            template.andThen(embed -> {
                                embed.addField("Command Aliases", message, false);
                            });
                        }
                        event.getMessage().getChannel().block().createMessage(spec -> spec.setEmbed(template.andThen(embedspec ->{})));
                        return true;
                    } else {
                        event.getMessage().getChannel().block().createMessage("You don't have permission to look at the advanced help for this command");
                        return false;
                    }
                }
            }
            event.getMessage().getChannel().block().createMessage("No commands where found by that name. Please check your spelling and try again");
            return false;
        }else  {
            message = "";
            for (CommandCore GG : GC) {
                if (GG.isHelpViewable()) {
                    if(GG.isAdminOnly()) {
                        if(event.getMember().get().getRoleIds().equals(Main.getDbManager().getAdminRole()) || event.getMember().get().equals(event.getClient().getApplicationInfo().block().getOwner().block())) {
                            message = message + "\n**" + GG.getCommandName() + " - **" + GG.getHelpMessage() + "\nUsage: " + prefix + GG.getUsage();
                        }
                    } else {
                        message = message + "\n**" + GG.getCommandName() + " - **" + GG.getHelpMessage() + "\nUsage: " + prefix + GG.getUsage();
                    }
                }
            }
            template.andThen(embed -> {
                embed.addField("Commands", message, false);
            });
            event.getMessage().getChannel().block().createMessage(spec -> spec.setEmbed(template.andThen(embedspec ->{})));
        }
        return true;
    }
}