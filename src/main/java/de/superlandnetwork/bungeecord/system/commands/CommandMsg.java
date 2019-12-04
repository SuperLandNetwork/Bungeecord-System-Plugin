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

package de.superlandnetwork.bungeecord.system.commands;

import de.superlandnetwork.bungeecord.permission.api.PermissionAPI;
import de.superlandnetwork.bungeecord.system.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class CommandMsg extends Command {

    public CommandMsg(Main m) {
        super("msg", "", "tell");
    }

    /**
     * Execute this command with the specified sender and arguments.
     *
     * @param sender the executor of this command
     * @param args   arguments used to invoke this command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (args.length < 2) return;
        String target = args[0];

        ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
        if (t == null) {
            p.sendMessage(new TextComponent("§7[§4Freunde§7] §cDer Spieler ist nicht online!"));
            return;
        }

        if (p.getUniqueId() == t.getUniqueId()) return;

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]);
        }

        try {
            PermissionAPI api = new PermissionAPI();
            api.connect();
            api.getChat(api.getHighestVisibleGroup(t.getUniqueId())).ifPresent(s ->
                    p.sendMessage(new TextComponent("§7[§4Freunde§7] §7Du -> " + ChatColor.translateAlternateColorCodes('&', s) + t.getDisplayName() + " §7: §e" + sb.toString())));
            api.getChat(api.getHighestVisibleGroup(p.getUniqueId())).ifPresent(s ->
                    t.sendMessage(new TextComponent("§7[§4Freunde§7] " + ChatColor.translateAlternateColorCodes('&', s) + p.getDisplayName() + " §7-> Dir §7: §e" + sb.toString())));
            api.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
