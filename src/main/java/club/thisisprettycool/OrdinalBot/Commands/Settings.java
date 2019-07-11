package club.thisisprettycool.OrdinalBot.Commands;

import club.thisisprettycool.OrdinalBot.Main;
import club.thisisprettycool.OrdinalBot.Objects.CommandCore;
import club.thisisprettycool.OrdinalBot.Objects.ServerLink;

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
    public boolean executeCommand(MessageReceivedEvent event, String[] argsArray) {
        if(argsArray.length==1) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.withAuthorName("Bot Settings");
            builder.withFooterText("Called by "+event.getAuthor().getName());
            builder.appendField("Admin Role",""+event.getGuild().getRoleByID(Main.getDbManager().getAdminRole()),true);
            builder.appendField("Event Role",""+event.getGuild().getRoleByID(Main.getDbManager().getEventRole()),true);
            builder.appendField("General Role",""+event.getGuild().getRoleByID(Main.getDbManager().getGeneralRole()),true);
            builder.appendField("Update Role",""+event.getGuild().getRoleByID(Main.getDbManager().getDevelopRole()),true);
            builder.appendField("Event Staff Role",""+event.getGuild().getRoleByID(Main.getDbManager().getEventStaffRole()  ),true);
            event.getChannel().sendMessage(builder.build());
            return true;
        }
        if(argsArray.length>=2) {
            if(argsArray.length==2) {
                if(argsArray[1].equalsIgnoreCase("setsuggestionchannel")) {
                    Main.getDbManager().setSuggestChannel(event.getChannel().getLongID());
                    event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
                    return true;
                }
                if(argsArray[1].equalsIgnoreCase("setserverinfochannel")) {
                    if(Main.getDbManager().getIP()==null) {
                        notEnoughShit(event);
                        return false;
                    }
                    event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
                    setupServerInfo(event);
                    return true;
                }
                if(argsArray[1].equalsIgnoreCase("setoptchannel")) {
                    if(Main.getDbManager().getEventRole()==0 || Main.getDbManager().getDevelopRole()==0 || Main.getDbManager().getGeneralRole()==0) {
                        notEnoughShit(event);
                        return false;
                    }
                    event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
                    setupOptChannel(event);
                    return true;
                }
            }
            if(argsArray.length==3) {
                if(argsArray[1].equalsIgnoreCase("setprefix")) {
                    Main.getDbManager().setPrefix(argsArray[2]);
                    event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
                    return true;
                }
                if(argsArray[1].equalsIgnoreCase("setip")) {
                    Main.getDbManager().setIP(argsArray[2]);
                    event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
                    return true;
                }
                if(argsArray[1].equalsIgnoreCase("setadminrole")) {
                    try {
                        Main.getDbManager().setAdminRole(Long.parseLong(getOnlyDigits(argsArray[2])));
                        event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
                        return true;
                    }catch (Exception e) {
                        wrongShit(event);
                        return false;
                    }
                }
                if(argsArray[1].equalsIgnoreCase("seteventrole")) {
                    try {
                        Main.getDbManager().setEventRole(Long.parseLong(getOnlyDigits(argsArray[2])));
                        event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
                        return true;
                    }catch (Exception e) {
                        wrongShit(event);
                        return false;
                    }
                }
                if(argsArray[1].equalsIgnoreCase("setgeneralrole")) {
                    try {
                        Main.getDbManager().setGeneralRole(Long.parseLong(getOnlyDigits(argsArray[2])));
                        event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
                        return true;
                    }catch (Exception e) {
                        wrongShit(event);
                        return false;
                    }
                }
                if(argsArray[1].equalsIgnoreCase("setdeveloprole")) {
                    try {
                        Main.getDbManager().setDevelopRole(Long.parseLong(getOnlyDigits(argsArray[2])));
                        event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
                        return true;
                    }catch (Exception e) {
                        wrongShit(event);
                        return false;
                    }
                }
                if(argsArray[1].equalsIgnoreCase("seteventstaffrole")) {
                    try {
                        Main.getDbManager().setEventStaffRole(Long.parseLong(getOnlyDigits(argsArray[2])));
                        event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
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

    private void setupServerInfo(MessageReceivedEvent event) {
        try {
            if (Main.getDbManager().getServerInfoMessage() != 0L) {
                event.getGuild().getMessageByID(Main.getDbManager().getServerInfoMessage()).delete();
            }
            Main.getDbManager().setServerInfoMessage(event.getChannel().sendMessage("Setting up server info...").getLongID());
            String[] array = Main.getDbManager().getIP().split(":");
            new ServerLink(event.getGuild().getMessageByID(Main.getDbManager().getServerInfoMessage()), array[0], Integer.parseInt(array[1]));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupOptChannel(MessageReceivedEvent event) {

    }

    private void wrongShit(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(255,0,0);
        builder.withAuthorName("You've not entered the right stuff here");
        builder.withDescription("You probably don't know what you're doing then");
        builder.withFooterText("Ran by "+event.getAuthor().getName());
        event.getChannel().sendMessage(builder.build());
    }

    private void notEnoughShit(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(255,0,0);
        builder.withAuthorName("You've not entered the right stuff yet");
        builder.withDescription("You're missing a few variables here and there");
        builder.withFooterText("Ran by "+event.getAuthor().getName());
        event.getChannel().sendMessage(builder.build());
    }

    private static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }
}
