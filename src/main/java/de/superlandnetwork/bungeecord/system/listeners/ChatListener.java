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

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent e) {
        if (!e.isCommand()) return;
        if (!(e.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer p = (ProxiedPlayer) e.getSender();
        String s = e.getMessage().toLowerCase();
        if (s.startsWith("/pl") || s.startsWith("/plugin") || s.startsWith("/?") ||
                s.startsWith("/ver") || s.startsWith("/version") || s.startsWith("/about") ||
                s.startsWith("/bungee")) {
            if (!p.hasPermission("bungeesystem.command.plugin")) e.setCancelled(true);
            return;
        }
        if (s.startsWith("/me")) {
            if (!p.hasPermission("bungeesystem.command.me")) e.setCancelled(true);
        }
    }

}
