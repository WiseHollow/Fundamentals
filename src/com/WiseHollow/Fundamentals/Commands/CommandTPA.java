package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Main;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by John on 10/13/2016.
 */
public class CommandTPA implements CommandExecutor
{
    public static HashMap<Player, Player> tpaHash = new HashMap<>();
    public static HashMap<Player, Integer> tpaTaskIDs = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Language.YouMustBeLoggedIn);
            return true;
        }

        Player player = (Player) sender;
        if (!sender.hasPermission("Fundamentals.TPA"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length != 1)
        {
            return false;
        }

        Player target = PlayerUtil.GetPlayer(args[0]);

        if (target == null || !target.isOnline())
        {
            player.sendMessage(Language.PlayerMustBeLoggedIn);
            return true;
        }

        if (tpaHash.containsKey(player))
            tpaHash.remove(player);

        tpaHash.put(player, target);
        player.sendMessage(Language.PREFIX_COLOR + "Teleport request sent!");
        target.sendMessage(Language.PREFIX_COLOR + player.getName() + " is requesting to teleport to you, </tpAccept or /tpDeny>.");

        tpaTaskIDs.put(player, Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () ->
        {
            if (tpaHash.containsKey(player) && tpaHash.get(player).equals(target))
            {
                tpaHash.remove(player);
            }
        }, 20*60*2));

        return true;
    }
}
