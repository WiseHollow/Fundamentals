package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.DataCollection.PlayerData;
import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.PlayerUtil;
import com.WiseHollow.Fundamentals.Settings;
import com.WiseHollow.Fundamentals.Tasks.TeleportTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandTeleport implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.YouMustBeLoggedIn);
            return true;
        }

        Player player = (Player) sender;
        if (!sender.hasPermission("Fundamentals.TP")) {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        if (args.length == 1) {
            Player target = PlayerUtil.GetPlayer(args[0]);
            PlayerData targetData = PlayerData.GetPlayerData(target);

            if (target == null || !target.isOnline()) {
                player.sendMessage(Language.PlayerMustBeLoggedIn);
                return true;
            } else if (targetData != null && targetData.hasTeleportDisabled()) {
                player.sendMessage(Language.PREFIX_COLOR + Language.HasTeleportDisabled);
                return true;
            }

            TeleportTask task = new TeleportTask(player, target.getLocation().clone(), Settings.TeleportDelay);
            task.Run();

            return true;
        } else if (args.length == 2 && sender.hasPermission("Fundamentals.TP.Other")) {
            Player source = PlayerUtil.GetPlayer(args[0]);
            Player target = PlayerUtil.GetPlayer(args[1]);
            PlayerData targetData = PlayerData.GetPlayerData(target);

            if (target == null || !target.isOnline()) {
                player.sendMessage(Language.PlayerMustBeLoggedIn);
                return true;
            } else if (source == null || !source.isOnline()) {
                player.sendMessage(Language.PREFIX_COLOR + Language.PlayerMustBeLoggedIn);
                return true;
            } else if (targetData != null && targetData.hasTeleportDisabled()) {
                player.sendMessage(Language.PREFIX_COLOR + Language.HasTeleportDisabled);
                return true;
            }

            TeleportTask task = new TeleportTask(source, target.getLocation().clone(), 0);
            task.Run();

            return true;
        }

        return false;
    }
}
