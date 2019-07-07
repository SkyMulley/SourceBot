package club.thisisprettycool.OrdinalBot.Objects;

public class Suggestion {
    private long user;
    private String suggestion;
    private long message;
    public Suggestion(long user, String suggestion, long message) {
        this.user = user;
        this.suggestion = suggestion;
        this.message = message;
    }

    public long getUser() {return user;}
    public String getSuggestion() {return suggestion;}
    public long getMessage() {return message;}
}
