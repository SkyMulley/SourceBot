package club.thisisprettycool.OrdinalBot;

import club.thisisprettycool.OrdinalBot.Objects.Suggestion;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class DBManager {
    Connection c = null;
    Statement stmt = null;

    DBManager() {
        try {
            if(!new File("OrdinalBot.db").exists()) {
                c = DriverManager.getConnection("jdbc:sqlite:OrdinalBot.db");
                stmt = c.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS Suggestions (UserID bigint(20), Suggestion varchar(2000), SuggestionMessage varchar(20))");
                stmt.execute("CREATE TABLE IF NOT EXISTS Settings (Prefix VARCHAR(5), ServerInfoChannel bigint(20), ServerInfoIP VARCHAR(20), SuggestChannel bigint(20), OptChannel bigint(20))");
                stmt.execute("CREATE TABLE IF NOT EXISTS Tags (UserID bigint(20), Tag varchar(50))");
                stmt.execute("CREATE TABLE IF NOT EXISTS Blacklisted (UserID bigint(20))");
                stmt.execute("CREATE TABLE IF NOT EXISTS Roles (AdminRole bigint(20), EventRole bigint(20), GeneralRole bigint(20), DeveloperRole bigint(20), EventStaff bigint(20))");
                System.out.println("Database does not exist, generating records");
                stmt.execute("INSERT INTO Settings (Prefix,ServerInfoChannel,ServerInfoIP,SuggestChannel,OptChannel) VALUES ('?',0,'null',0,0)");
                stmt.execute("INSERT INTO Roles (AdminRole, EventRole, GeneralRole, DeveloperRole, EventStaff) VALUES (0,0,0,0,0)");
            }
            c = DriverManager.getConnection("jdbc:sqlite:OrdinalBot.db");
            System.out.println("Database connection complete");
        } catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public void createSuggestion(long userid, String suggestion, long channelid) {
        try {
            stmt = c.createStatement();
            stmt.execute("INSERT INTO Suggestions(UserID, Suggestion, SuggestionMessage) VALUES ("+userid+",'"+suggestion+"','"+channelid+"')");
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

    public void setIP(String prefix) {
        try {
            stmt = c.createStatement();
            stmt.execute("UPDATE Settings SET ServerInfoIP = '"+prefix+"'");
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public String getIP() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Settings");
            while (rs.next()) {
                return rs.getString("serverinfoip");
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
            stmt.execute("UPDATE Roles SET AdminRole = "+role+"");
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public long getAdminRole() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Roles");
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

    public long getSuggestChannel() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Settings");
            if(rs.next()) {
                return rs.getLong("SuggestChannel");
            }
            return 0;
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return 0;
        }
    }

    public void setSuggestChannel(long id) {
        try {
            stmt = c.createStatement();
            stmt.execute("UPDATE Settings SET SuggestChannel = "+id+" WHERE SuggestChannel = "+getSuggestChannel());
        } catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public ResultSet runQuery(String statement) {
        try {
            stmt = c.createStatement();
            return stmt.executeQuery(statement);
        } catch (Exception e) {
            return null;
        }
    }

    public long getServerInfoMessage() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Settings");
            if(rs.next()) {
                return rs.getLong("serverinfochannel");
            }
            return 0;
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return 0;
        }
    }

    public void setServerInfoMessage(long id) {
        try {
            stmt = c.createStatement();
            stmt.execute("UPDATE Settings SET ServerInfoChannel = "+id);
        } catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public long getOptMessage() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Settings");
            if(rs.next()) {
                return rs.getLong("OptChannel");
            }
            return 0;
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return 0;
        }
    }

    public void setOptMessage(long id) {
        try {
            stmt = c.createStatement();
            stmt.execute("UPDATE Settings SET OptChannel = "+id+" WHERE OptChannel = "+getServerInfoMessage());
        } catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public void setEventRole(long role) {
        try {
            stmt = c.createStatement();
            stmt.execute("UPDATE Roles SET EventRole = "+role+"");
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public long getEventRole() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Roles");
            while (rs.next()) {
                return rs.getLong("eventrole");
            }
            return 0;
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return 0;
        }
    }

    public void setGeneralRole(long role) {
        try {
            stmt = c.createStatement();
            stmt.execute("UPDATE Roles SET GeneralRole = "+role+"");
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public long getGeneralRole() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Roles");
            while (rs.next()) {
                return rs.getLong("generalrole");
            }
            return 0;
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return 0;
        }
    }

    public void setDevelopRole(long role) {
        try {
            stmt = c.createStatement();
            stmt.execute("UPDATE Roles SET DeveloperRole = "+role+"");
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public long getDevelopRole() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Roles");
            while (rs.next()) {
                return rs.getLong("developerrole");
            }
            return 0;
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return 0;
        }
    }

    public void setEventStaffRole(long role) {
        try {
            stmt = c.createStatement();
            stmt.execute("UPDATE Roles SET EventStaff = "+role+"");
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
        }
    }

    public long getEventStaffRole() {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Roles");
            while (rs.next()) {
                return rs.getLong("eventstaff");
            }
            return 0;
        }catch (Exception e) {
            System.out.println("Something has gone wrong");
            e.printStackTrace();
            return 0;
        }
    }
}
