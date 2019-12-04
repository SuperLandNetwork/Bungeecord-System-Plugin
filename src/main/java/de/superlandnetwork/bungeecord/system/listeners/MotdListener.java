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

package de.superlandnetwork.bungeecord.system.listeners;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.UUID;

public class MotdListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPing(ProxyPingEvent e) {
        ServerPing response = e.getResponse();
        ServerPing.Players players = response.getPlayers();
        ServerPing.Protocol version = response.getVersion();

        version.setName("SuperLandNetwork.de 1.14.4");
        version.setProtocol(498);
        players.setMax(100);
        players.setSample(new ServerPing.PlayerInfo[]{
                new ServerPing.PlayerInfo("§7» §aSuperLandNetwork.de", UUID.randomUUID()),
                new ServerPing.PlayerInfo("§7» §cMC: 1.14.4", UUID.randomUUID()),
                new ServerPing.PlayerInfo("§7» §4Maintenance", UUID.randomUUID()),
                new ServerPing.PlayerInfo("§7» §bwww.SuperLandNetwork.de", UUID.randomUUID()),
        });

        response.setVersion(version);
        response.setPlayers(players);
        response.setDescriptionComponent(new TextComponent("§aSuperLandNetwork.de §7» §aMinecraft Server §7» §b1.14.4" +
                "     " +
                "§7» §4Maintenance!"));
        System.out.println("Debug: " + e.getConnection().getVersion());
    }

}
