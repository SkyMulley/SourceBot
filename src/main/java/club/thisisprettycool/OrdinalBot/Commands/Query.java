package club.thisisprettycool.OrdinalBot.Commands;

import club.thisisprettycool.OrdinalBot.Main;
import club.thisisprettycool.OrdinalBot.Objects.CommandCore;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.reaction.ReactionEmoji;

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
    public boolean executeCommand(MessageCreateEvent event, String[] argArray, String content) {
        try {
            ResultSet rs = Main.getDbManager().runQuery(content.substring(5 + Main.getDbManager().getPrefix().length()));
            event.getMessage().addReaction(ReactionEmoji.unicode("ok"));
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
            event.getMessage().getChannel().block().createMessage("```"+embed+"```");
            return true;
        }catch (Exception e) {
            return true;
        }
    }
}