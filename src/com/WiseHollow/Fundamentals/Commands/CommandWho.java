package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by John on 10/13/2016.
 */
public class CommandWho implements CommandExecutor
{
    private Collection<Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> (!player.hasMetadata("vanished") || player.getMetadata("vanished").equals(false)))
                .collect(Collectors.toList());
    }

    private String GetOnlinePlayers()
    {
        StringBuilder list = new StringBuilder();

        getOnlinePlayers()
                .forEach(player -> list.append(PlayerUtil.GetPlayerPrefix(player)).append(player.getName()).append(" "));

        return list.toString();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Who"))
        {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        String onlinePlayers = GetOnlinePlayers();

        sender.sendMessage(Language.PREFIX + "Online players (" + getOnlinePlayers().size() + "): " + GetOnlinePlayers());
        return true;
    }
}
