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

import de.superlandnetwork.bungeecord.system.Main;
import de.superlandnetwork.bungeecord.system.api.PlayerAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.Optional;

public class CommandTs extends Command {

    public CommandTs(Main m) {
        super("ts");
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
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("show")) {
                try {
                    PlayerAPI api = new PlayerAPI(p.getUniqueId());
                    api.connect();
                    Optional<String> s = api.getTeamSpeak();
                    if (s.isPresent())
                        p.sendMessage(new TextComponent("§7[§3TS§7] §eVerknpfte Identität: §7" + s.get()));
                    else
                        p.sendMessage(new TextComponent("§7[§3TS§7] §cDu hast aktuell keine Identität verknpft!"));
                    api.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                try {
                    PlayerAPI api = new PlayerAPI(p.getUniqueId());
                    api.connect();
                    if (api.getTeamSpeak().isPresent()) {
                        api.removeTeamSpeak();
                        p.sendMessage(new TextComponent("§7[§3TS§7] §aDeine Identität wurde entfernt!"));
                    } else
                        p.sendMessage(new TextComponent("§7[§3TS§7] §cDu hast keine Identität verknpft!"));
                    api.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return;
            }
            sendHelp(p);
            return;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                if (args[1].length() == 28 && args[1].endsWith("=")) {
                    try {
                        PlayerAPI api = new PlayerAPI(p.getUniqueId());
                        api.connect();
                        if (api.getTeamSpeak().isPresent())
                            api.removeTeamSpeak();
                        api.sendTeamSpeak(p.getName(), args[1]);
                        p.sendMessage(new TextComponent("§7[§3TS§7] §aDeine Identität wurde gesetzt. §aBestätige diese nun bitte auf dem TeamSpeak!"));
                        api.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                p.sendMessage(new TextComponent("§7[§3TS§7] §cDie Identität muss 28 Zeichen lang sein und mit einem '=' enden."));
                return;
            }
            sendHelp(p);
            return;
        }
        sendHelp(p);
    }

    public void sendHelp(ProxiedPlayer p) {
        p.sendMessage(new TextComponent("§7[§3TS§7] §6TeamSpeak Verwaltung"));
        p.sendMessage(new TextComponent("§e/ts show §7Zeigt die aktuell verknpfte Identität"));
        p.sendMessage(new TextComponent("§e/ts set <Identitt> §7Verknpft Identität mit deinem Minecraft-Account"));
        p.sendMessage(new TextComponent("§e/ts remove §7Entfernt die aktuell verknpfte Identität"));
    }

}
