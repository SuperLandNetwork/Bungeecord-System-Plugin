/*
 * MIT License
 *
 * Copyright (c) 2019 Filli Group (Einzelunternehmen)
 * Copyright (c) 2019 Filli IT (Einzelunternehmen)
 * Copyright (c) 2019 Filli Games (Einzelunternehmen)
 * Copyright (c) 2019 Ursin Filli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.superlandnetwork.bungeecord.system.api;

import de.superlandnetwork.bungeecord.permission.api.PermissionAPI;
import de.superlandnetwork.bungeecord.permission.api.User;
import de.superlandnetwork.bungeecord.system.Main;
import de.superlandnetwork.lib.database.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class PlayerAPI {

    private MySQL mySQL;
    private UUID uuid;

    public PlayerAPI(UUID uuid) {
        mySQL = Main.getInstance().getMySQL();
        this.uuid = uuid;
    }

    public void connect() throws SQLException {
        mySQL.connect();
    }

    public void close() throws SQLException {
        mySQL.close();
    }

    public void updatePlayer(String name) throws SQLException {
        String sql = "SELECT `id` FROM `sln_mc_users` WHERE `uuid`='" + uuid.toString() + "'";
        ResultSet rs = mySQL.getResult(sql);
        if (rs.next()) {
            String sql2 = "UPDATE `sln_mc_users` SET `last_name`='" + name + "' WHERE `uuid`='" + uuid.toString() + "'";
            mySQL.update(sql2);
        } else {
            String sql2 = "INSERT INTO `sln_mc_users` (`uuid`, `last_name`) VALUES ('" + uuid.toString() + "', '" + name + "')";
            mySQL.update(sql2);
        }
        String sql2 = "SELECT `id` FROM `sln_users` WHERE `uuid`='" + uuid.toString() + "'";
        ResultSet rs2 = mySQL.getResult(sql2);
        if (!rs2.next()) {
            String sql3 = "INSERT INTO `sln_users` (`uuid`) VALUES ('" + uuid.toString() + "')";
            mySQL.update(sql3);
        }
        new GMCBM_API().updateName(this.uuid, name);
    }

    public void checkRankTime() throws SQLException {
        PermissionAPI permissionAPI = new PermissionAPI();
        permissionAPI.connect();
        User user = permissionAPI.getUser(uuid);
        for (int i : user.getGroupIds()) {
            if (i == 1) continue;
            if (user.getTimes().get(i) == -1) continue;
            long time = user.getTimes().get(i);
            if (time > System.currentTimeMillis()) continue;
            permissionAPI.removeGroup(uuid, i, time);
        }
        permissionAPI.close();
    }

    public void addCoins(int i) throws SQLException {
        setCoins(getCoins()+i);
    }

    public void takeCoins(int i) throws SQLException {
        setCoins(getCoins()-i);
    }

    public int getCoins() throws SQLException {
        String sql = "SELECT `coins` FROM `sln_mc_users` WHERE `uuid`='"+uuid.toString()+"'";
        ResultSet rs = mySQL.getResult(sql);
        if (rs.next())
            return rs.getInt("coins");
        return 0;
    }

    public void setCoins(int i) throws SQLException {
        String sql = "UPDATE `sln_mc_users` SET `coins`='"+i+"' WHERE `uuid`='"+uuid.toString()+"'";
        mySQL.update(sql);
    }

    public Optional<String> getTeamSpeak() throws SQLException {
        String sql = "SELECT `uid` FROM `sln_users` WHERE `uuid`='"+uuid.toString()+"'";
        ResultSet rs = mySQL.getResult(sql);
        if (rs.next())
            return Optional.ofNullable(rs.getString("uid"));
        return Optional.empty();
    }

    public void sendTeamSpeak(String name, String uid) throws SQLException {
        String sql = "INSERT INTO `sln_verify`(`uuid`, `name`, `type`, `content`, `type_add`) VALUES ('"+uuid.toString()+"', '"+name+"', '1', '"+uid+"', '1')";
        if (inProgressTeamSpeak())
            sql = "UPDATE `sln_verify` SET `send`='0' WHERE `uuid`='"+uuid.toString()+"' AND `type`='1'";
        mySQL.update(sql);
    }

    public void removeTeamSpeak() throws SQLException {
        String sql = "INSERT INTO `sln_verify`(`uuid`, `type`, `type_add`) VALUES ('"+uuid.toString()+"', '1', '0')";
        mySQL.update(sql);
    }

    public Optional<String> getDiscord() throws SQLException {
        String sql = "SELECT `discord` FROM `sln_users` WHERE `uuid`='"+uuid.toString()+"'";
        ResultSet rs = mySQL.getResult(sql);
        if (rs.next())
            return Optional.ofNullable(rs.getString("discord"));
        return Optional.empty();
    }

    public void sendDiscord(String name, String discord) throws SQLException {
        String sql = "INSERT INTO `sln_verify`(`uuid`, `name`, `type`, `content`, `type_add`) VALUES ('"+uuid.toString()+"', '"+name+"', '2', '"+discord+"', '1')";
        if (inProgressDiscord())
            sql = "UPDATE `sln_verify` SET `send`='0' WHERE `uuid`='"+uuid.toString()+"' AND `type`='2'";
        mySQL.update(sql);
    }

    public void removeDiscord() throws SQLException {
        String sql = "INSERT INTO `sln_verify`(`uuid`, `type`, `type_add`) VALUES ('"+uuid.toString()+"', '2', '0')";
        mySQL.update(sql);
    }

    public boolean inProgressTeamSpeak() throws SQLException {
        String sql = "SELECT `id` FROM `sln_verify` WHERE `uuid`='"+uuid.toString()+"' AND `type`='1'";
        ResultSet rs = mySQL.getResult(sql);
        return rs.next();
    }

    public boolean inProgressDiscord() throws SQLException {
        String sql = "SELECT `id` FROM `sln_verify` WHERE `uuid`='"+uuid.toString()+"' AND `type`='2'";
        ResultSet rs = mySQL.getResult(sql);
        return rs.next();
    }

}
