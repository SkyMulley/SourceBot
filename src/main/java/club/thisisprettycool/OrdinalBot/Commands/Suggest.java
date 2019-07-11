package club.thisisprettycool.OrdinalBot.Commands;

import club.thisisprettycool.OrdinalBot.Main;
import club.thisisprettycool.OrdinalBot.Objects.CommandCore;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;

import java.util.concurrent.TimeUnit;

public class Suggest extends CommandCore {
    public Suggest() {
        commandName = "Suggest";
        helpMessage = "Suggest an change in the server!";
        usage = "suggest <suggestion>";
        addAlias("suggestion");
    }

    @Override
    public boolean executeCommand(MessageCreateEvent event, String[] argsArray, String content) {
        try {
            if (Main.getDbManager().getSuggestChannel() == 0) {
                event.getMessage().getChannel().block().createMessage("A suggest channel hasn't been setup, please contact an Admin to have one done");
                return false;
            }
            if (argsArray.length >= 2) {
                Message message = event.getMessage().getChannel().block().createMessage(c ->c.setEmbed(embed -> {
                    embed.setAuthor("Suggestion by "+event.getMember().get().getUsername(),event.getMember().get().getAvatarUrl(),event.getMember().get().getAvatarUrl());
                    embed.setDescription(content.substring(argsArray[0].length()));
                })).block();
                message.addReaction(ReactionEmoji.unicode("white_check_mark"));
                message.addReaction(ReactionEmoji.unicode("negative_squared_cross_mark"));
                event.getMessage().getChannel().block().createMessage("Thanks for the suggestion!");
                Main.getDbManager().createSuggestion(event.getMember().get().getId().asLong(),content.substring(argsArray[0].length()),message.getId().asLong());
                return true;
            }
        }catch (Exception e) {
            event.getMessage().getChannel().block().createMessage("Something went wrong, it could be that I don't have permissions to send inside the suggestion channel");
            return false;
        }
        return false;
    }
}
