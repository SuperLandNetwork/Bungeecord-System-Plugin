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

package de.superlandnetwork.bungeecord.system;

import de.superlandnetwork.bungeecord.api.database.MySQL;
import de.superlandnetwork.bungeecord.system.commands.*;
import de.superlandnetwork.bungeecord.system.listeners.ChatListener;
import de.superlandnetwork.bungeecord.system.listeners.JoinListener;
import de.superlandnetwork.bungeecord.system.listeners.MotdListener;
import de.superlandnetwork.bungeecord.system.utils.Config;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.Properties;

public final class Main extends Plugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pl = getProxy().getPluginManager();
        pl.registerCommand(this, new CommandDiscord(this));
        pl.registerCommand(this, new CommandEco(this));
        pl.registerCommand(this, new CommandFriend(this));
        pl.registerCommand(this, new CommandHelp(this));
        pl.registerCommand(this, new CommandJumpTo(this));
        pl.registerCommand(this, new CommandMoney(this));
        pl.registerCommand(this, new CommandMsg(this));
        pl.registerCommand(this, new CommandParty(this));
        pl.registerCommand(this, new CommandPing(this));
        pl.registerCommand(this, new CommandReport(this));
        pl.registerCommand(this, new CommandTeamChat(this));
        pl.registerCommand(this, new CommandTs(this));
        pl.registerListener(this, new ChatListener());
        pl.registerListener(this, new JoinListener());
        pl.registerListener(this, new MotdListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MySQL getMySQL() {
        Properties settings = new Config().getSettingsProps();
        return new MySQL(settings.getProperty("host"), settings.getProperty("port"), settings.getProperty("database"), settings.getProperty("username"), settings.getProperty("password"));
    }

}
