package club.thisisprettycool.OrdinalBot.Commands;

import club.thisisprettycool.OrdinalBot.Main;
import club.thisisprettycool.OrdinalBot.Objects.CommandCore;
import club.thisisprettycool.OrdinalBot.Objects.ServerLink;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.object.util.Snowflake;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings extends CommandCore {
    public Settings() {
        commandName = "Settings";
        helpMessage = "**ADMIN ONLY** Change the settings of the bot";
        usage = "settings <setsuggestionchannel/setserverinfochannel/setprefix/setip/setoptchannel/setadminrole/seteventrole/setgeneralrole/setdeveloprole/seteventstaffrole> (Entry)";
        adminOnly = true;
    }

    @Override
    public boolean executeCommand(MessageCreateEvent event, String[] argsArray, String content) {
        if(argsArray.length==1) {
            event.getMessage().getChannel().block().createMessage(c -> c.setEmbed(embed -> {
                embed.setAuthor("Bot Settings",event.getClient().getSelf().block().getAvatarUrl(),event.getClient().getSelf().block().getAvatarUrl());
                embed.setFooter("Called by "+event.getMember().get().getNickname().get(),event.getMember().get().getAvatarUrl());
                embed.addField("Admin Role",""+event.getGuild().block().getRoleById(Snowflake.of(Main.getDbManager().getAdminRole())),true);
                embed.addField("Event Role",""+event.getGuild().block().getRoleById(Snowflake.of(Main.getDbManager().getEventRole())),true);
                embed.addField("General Role",""+event.getGuild().block().getRoleById(Snowflake.of(Main.getDbManager().getGeneralRole())),true);
                embed.addField("Update Role",""+event.getGuild().block().getRoleById(Snowflake.of(Main.getDbManager().getDevelopRole())),true);
                embed.addField("Event Staff Role",""+event.getGuild().block().getRoleById(Snowflake.of(Main.getDbManager().getEventStaffRole())),true);
            }));
            return true;
        }
        if(argsArray.length>=2) {
            if(argsArray.length==2) {
                if(argsArray[1].equalsIgnoreCase("setsuggestionchannel")) {
                    Main.getDbManager().setSuggestChannel(event.getMessage().getChannel().block().getId().asLong());
                    event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
                    return true;
                }
                if(argsArray[1].equalsIgnoreCase("setserverinfochannel")) {
                    if(Main.getDbManager().getIP()==null) {
                        notEnoughShit(event);
                        return false;
                    }
                    event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
                    setupServerInfo(event);
                    return true;
                }
                if(argsArray[1].equalsIgnoreCase("setoptchannel")) {
                    if(Main.getDbManager().getEventRole()==0 || Main.getDbManager().getDevelopRole()==0 || Main.getDbManager().getGeneralRole()==0) {
                        notEnoughShit(event);
                        return false;
                    }
                    event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
                    setupOptChannel(event);
                    return true;
                }
            }
            if(argsArray.length==3) {
                if(argsArray[1].equalsIgnoreCase("setprefix")) {
                    Main.getDbManager().setPrefix(argsArray[2]);
                    event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
                    return true;
                }
                if(argsArray[1].equalsIgnoreCase("setip")) {
                    Main.getDbManager().setIP(argsArray[2]);
                    event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
                    return true;
                }
                if(argsArray[1].equalsIgnoreCase("setadminrole")) {
                    try {
                        Main.getDbManager().setAdminRole(Long.parseLong(getOnlyDigits(argsArray[2])));
                        event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
                        return true;
                    }catch (Exception e) {
                        wrongShit(event);
                        return false;
                    }
                }
                if(argsArray[1].equalsIgnoreCase("seteventrole")) {
                    try {
                        Main.getDbManager().setEventRole(Long.parseLong(getOnlyDigits(argsArray[2])));
                        event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
                        return true;
                    }catch (Exception e) {
                        wrongShit(event);
                        return false;
                    }
                }
                if(argsArray[1].equalsIgnoreCase("setgeneralrole")) {
                    try {
                        Main.getDbManager().setGeneralRole(Long.parseLong(getOnlyDigits(argsArray[2])));
                        event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
                        return true;
                    }catch (Exception e) {
                        wrongShit(event);
                        return false;
                    }
                }
                if(argsArray[1].equalsIgnoreCase("setdeveloprole")) {
                    try {
                        Main.getDbManager().setDevelopRole(Long.parseLong(getOnlyDigits(argsArray[2])));
                        event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
                        return true;
                    }catch (Exception e) {
                        wrongShit(event);
                        return false;
                    }
                }
                if(argsArray[1].equalsIgnoreCase("seteventstaffrole")) {
                    try {
                        Main.getDbManager().setEventStaffRole(Long.parseLong(getOnlyDigits(argsArray[2])));
                        event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
                        return true;
                    }catch (Exception e) {
                        e.printStackTrace();
                        wrongShit(event);
                        return false;
                    }
                }
            }
            argsNotFound(event);
            return false;
        } else {
            argsNotFound(event);
            return false;
        }
    }

    private void setupServerInfo(MessageCreateEvent event) {
        try {
            if (Main.getDbManager().getServerInfoMessage() != 0L) {
                event.getClient().getMessageById(Snowflake.of(Main.getDbManager().getServerInfoChannel()),Snowflake.of(Main.getDbManager().getServerInfoMessage())).block().delete();
            }
            Main.getDbManager().setServerInfoChannel(event.getMessage().getChannel().block().getId().asLong());
            Main.getDbManager().setServerInfoMessage(event.getMessage().getChannel().block().createMessage("Setting up server info...").block().getId().asLong());
            String[] array = Main.getDbManager().getIP().split(":");
            new ServerLink(event.getClient().getMessageById(Snowflake.of(Main.getDbManager().getServerInfoChannel()),Snowflake.of(Main.getDbManager().getServerInfoMessage())).block(),array[0], Integer.parseInt(array[1]));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupOptChannel(MessageCreateEvent event) {

    }

    private void wrongShit(MessageCreateEvent event) {
        event.getMessage().getChannel().block().createMessage(c -> c.setEmbed(embed -> {
            embed.setAuthor("You have not entered the right stuff here",event.getMember().get().getAvatarUrl(),event.getMember().get().getAvatarUrl());
            embed.setFooter("Called by "+event.getMember().get().getNickname().get(),event.getMember().get().getAvatarUrl());
            embed.setDescription("You probably don't know what you're doing then");
        }));
    }

    private void notEnoughShit(MessageCreateEvent event) {
        event.getMessage().getChannel().block().createMessage(c -> c.setEmbed(embed -> {
            embed.setAuthor("You don't have enough stuff yet",event.getMember().get().getAvatarUrl(),event.getMember().get().getAvatarUrl());
            embed.setFooter("Called by "+event.getMember().get().getNickname().get(),event.getMember().get().getAvatarUrl());
            embed.setDescription("You'll need to enter variables in places");
        }));
    }

    private static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }
}
