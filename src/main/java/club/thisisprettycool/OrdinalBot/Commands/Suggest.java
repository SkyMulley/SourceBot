package club.thisisprettycool.OrdinalBot.Commands;

import club.thisisprettycool.OrdinalBot.Main;
import club.thisisprettycool.OrdinalBot.Objects.CommandCore;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import java.util.concurrent.TimeUnit;

public class Suggest extends CommandCore {
    private static Emoji cross = EmojiManager.getForAlias("negative_squared_cross_mark");
    private static Emoji tick = EmojiManager.getForAlias("white_check_mark");
    public Suggest() {
        commandName = "Suggest";
        helpMessage = "Suggest an change in the server!";
        usage = "suggest <suggestion>";
        addAlias("suggestion");
    }

    @Override
    public boolean executeCommand(MessageReceivedEvent event, String[] argsArray) {
        try {
            if (Main.getDbManager().getSuggestChannel() == 0) {
                event.getChannel().sendMessage("A suggest channel hasn't been setup, please contact an Admin to have one done");
                return false;
            }
            if (argsArray.length >= 2) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.withAuthorIcon(event.getAuthor().getAvatarURL());
                builder.withAuthorName("Suggestion by "+event.getAuthor().getName());
                builder.withDescription(event.getMessage().getContent().substring(argsArray[0].length()));
                IMessage message = event.getGuild().getChannelByID(Main.getDbManager().getSuggestChannel()).sendMessage(builder.build());
                message.addReaction(tick);
                TimeUnit.SECONDS.sleep(1);
                message.addReaction(cross);
                event.getChannel().sendMessage("Thanks for the suggestion!");
                Main.getDbManager().createSuggestion(event.getAuthor().getLongID(),event.getMessage().getContent().substring(argsArray[0].length()),message.getLongID());
                return true;
            }
        }catch (Exception e) {
            event.getChannel().sendMessage("Something went wrong, it could be that I don't have permissions to send inside the suggestion channel");
            return false;
        }
        return false;
    }
}
