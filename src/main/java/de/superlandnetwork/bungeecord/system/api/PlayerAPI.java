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

import de.superlandnetwork.bungeecord.api.database.MySQL;
import de.superlandnetwork.bungeecord.permission.api.PermissionAPI;
import de.superlandnetwork.bungeecord.permission.api.User;
import de.superlandnetwork.bungeecord.system.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void addCoins(int i) {
        // TODO
    }

    public void takeCoins(int i) {
        // TODO
    }

    public int getCoins() {
        // TODO
        return 0;
    }

    public void setCoins(int i) {
        // TODO
    }

}
