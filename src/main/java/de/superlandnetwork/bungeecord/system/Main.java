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

import de.superlandnetwork.bungeecord.system.commands.*;
import de.superlandnetwork.bungeecord.system.listeners.ChatListener;
import de.superlandnetwork.bungeecord.system.listeners.JoinListener;
import de.superlandnetwork.bungeecord.system.listeners.MotdListener;
import de.superlandnetwork.lib.database.MySQL;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

public final class Main extends Plugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        registerListener();
        registerCommand();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MySQL getMySQL() {
        return new MySQL(Objects.requireNonNull(getConfig()).getString("mysql.host"), getConfig().getString("mysql.port"), getConfig().getString("mysql.database"), getConfig().getString("mysql.username"), getConfig().getString("mysql.password"));
    }

    private Configuration getConfig() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveDefaultConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerListener() {
        PluginManager pl = getProxy().getPluginManager();
        pl.registerListener(this, new ChatListener());
        pl.registerListener(this, new JoinListener());
        pl.registerListener(this, new MotdListener());
    }

    private void registerCommand() {
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
    }

}
