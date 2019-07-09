package club.thisisprettycool.OrdinalBot.Commands;

import club.thisisprettycool.OrdinalBot.Main;
import club.thisisprettycool.OrdinalBot.Objects.CommandCore;
import com.vdurmont.emoji.EmojiManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Guild;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Query extends CommandCore {
    public Query () {
        commandName = "Query";
        helpMessage = "Run a query on the SQL database";
        usage = "query <string>";
        adminOnly = true;
        helpViewable = false;
    }

    @Override
    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {
        try {
            ResultSet rs = Main.getDbManager().runQuery(event.getMessage().getContent().substring(5 + Main.getDbManager().getPrefix().length()));
            event.getMessage().addReaction(EmojiManager.getForAlias("ok"));
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            String embed = "";
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) embed = embed + (",  ");
                    String columnValue = rs.getString(i);
                    embed = embed + (columnValue + " " + rsmd.getColumnName(i) + "");
                }
                embed = embed + "\n\n";
            }
            event.getChannel().sendMessage("```"+embed+"```");
            return true;
        }catch (Exception e) {
            return true;
        }
    }
}