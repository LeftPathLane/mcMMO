package com.gmail.nossr50.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.nossr50.Users;
import com.gmail.nossr50.mcPermissions;
import com.gmail.nossr50.events.chat.McMMOAdminChatEvent;
import com.gmail.nossr50.events.chat.McMMOPartyChatEvent;

public class ChatAPI {
    /**
     * Send a message to all members of a party
     * </br>
     * This function is designed for API usage.
     *
     * @param sender The name of the sender to display in the chat
     * @param party The name of the party to send to
     * @param message The message to send
     */
    public void sendPartyChat(String sender, String party, String message) {
        McMMOPartyChatEvent chatEvent = new McMMOPartyChatEvent(sender, party, message);
        Bukkit.getPluginManager().callEvent(chatEvent);

        if(chatEvent.isCancelled()) return;

        String pPrefix = ChatColor.GREEN + "(" + ChatColor.WHITE + chatEvent.getSender() + ChatColor.GREEN + ") ";

        Bukkit.getLogger().info("[P](" + chatEvent.getParty() + ")" + "<" + chatEvent.getSender() + "> " + chatEvent.getMessage());

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (Users.getProfile(player).inParty()) {
                if (Users.getProfile(player).getParty().equalsIgnoreCase(chatEvent.getParty())) {
                    player.sendMessage(pPrefix + chatEvent.getMessage());
                }
            }
        }
    }

    /**
     * Send a message to administrators
     * </br>
     * This function is designed for API usage.
     *
     * @param sender The name of the sender to display in the chat
     * @param message The message to send
     */
    public void sendAdminChat(String sender, String message) {
        McMMOAdminChatEvent chatEvent = new McMMOAdminChatEvent(sender, message);
        Bukkit.getPluginManager().callEvent(chatEvent);

        if(chatEvent.isCancelled()) return;

        String aPrefix = ChatColor.AQUA + "{" + ChatColor.WHITE + chatEvent.getSender() + ChatColor.AQUA + "} ";

        Bukkit.getLogger().info("[A]<" + chatEvent.getSender() + "> " + chatEvent.getMessage());

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (mcPermissions.getInstance().adminChat(player) || player.isOp())
                player.sendMessage(aPrefix + chatEvent.getMessage());
        }
    }
}
