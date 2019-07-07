package club.thisisprettycool.OrdinalBot;

import club.thisisprettycool.OrdinalBot.Objects.Suggestion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBManager {
    Connection c = null;
    Statement stmt = null;

    DBManager() {
        try {
            c = DriverManager.getConnection("jdbc:sqlite:OrdinalBot.db");
            stmt = c.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS Suggestions (UserID bigint(20), Suggestion varchar(2000), SuggestionMessage varchar(20))");
            stmt.execute("CREATE TABLE IF NOT EXISTS Settings (Prefix VARCHAR(5), ServerInfoChannel bigint(20), ServerInfoIP VARCHAR(20), SuggestChannel bigint(20), AdminRole bigint(20))");
            stmt.execute("CREATE TABLE IF NOT EXISTS Tags (UserID bigint(20), Tag varchar(50))");
            stmt.execute("CREATE TABLE IF NOT EXISTS Blacklisted (UserID bigint(20))");
            System.out.println("Database connection complete");
        } catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public void createSuggestion(long userid, String suggestion, long channelid) {
        try {
            stmt = c.createStatement();
            stmt.execute("INSERT INTO Suggestions ("+userid+","+suggestion+","+channelid+")");
        } catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public Suggestion getSuggestion(long message) {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Suggestions");
            while (rs.next()) {
                if (message == rs.getLong("suggestionmessage")) {
                    return new Suggestion(rs.getLong("userid"), rs.getString("suggestion"), rs.getLong("suggestionmessage"));
                }
            }
            return null;
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return null;
        }
    }

    public void setPrefix(String prefix) {
        try {
            stmt = c.createStatement();
            stmt.execute("UPDATE Settings SET Prefix = '"+prefix+"'");
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public String getPrefix() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Settings");
            while (rs.next()) {
                return rs.getString("prefix");
            }
            return "?";
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return "?";
        }
    }

    public void setAdminRole(long role) {
        try {
            stmt = c.createStatement();
            stmt.execute("UPDATE Settings SET AdminRole = "+role+"");
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public long getAdminRole() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Settings");
            while (rs.next()) {
                return rs.getLong("adminrole");
            }
            return 0;
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return 0;
        }
    }

    public boolean toggleBlacklist(long userid) {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Blacklisted");
            while (rs.next()) {
                if(userid == rs.getLong("userid")) {
                    stmt.execute("DELETE FROM Blacklisted WHERE UserID = "+userid);
                    return false;
                }
            }
            stmt.execute("INSERT INTO Suggestions ("+userid+")");
            return true;
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return false;
        }
    }

    public boolean isBlacklisted(long userid) {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Blacklisted");
            while (rs.next()) {
                if(userid == rs.getLong("userid")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return false;
        }
    }
}
