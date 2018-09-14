package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.PlayerUtil;
import com.WiseHollow.Fundamentals.Tasks.AFKTask;
import com.WiseHollow.Fundamentals.Tasks.GodTask;
import com.WiseHollow.Fundamentals.Tasks.JailTask;
import com.WiseHollow.Fundamentals.Tasks.VanishTask;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandWhoIs implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (!sender.hasPermission("Fundamentals.WhoIs")) {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length == 0)
            return false;

        Player target = PlayerUtil.GetPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage(Language.PlayerMustBeLoggedIn);
            return true;
        }

        sender.sendMessage(Language.PREFIX + "Player information: " + ChatColor.RESET + target.getName());
        sender.sendMessage(Language.PREFIX_COLOR + "Health: " + ChatColor.RESET + target.getHealth() + "/" + target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        sender.sendMessage(Language.PREFIX_COLOR + "Food: " + ChatColor.RESET + target.getFoodLevel() + "/" + "20");
        sender.sendMessage(Language.PREFIX_COLOR + "Walk-Speed: " + ChatColor.RESET + target.getWalkSpeed());
        sender.sendMessage(Language.PREFIX_COLOR + "Fly-Speed: " + ChatColor.RESET + target.getFlySpeed());
        sender.sendMessage(Language.PREFIX_COLOR + "GameMode: " + ChatColor.RESET + target.getGameMode().name());
        sender.sendMessage(Language.PREFIX_COLOR + "Location: " + ChatColor.RESET + target.getWorld().getName() + "   " + target.getLocation().getBlockX() + " " + target.getLocation().getBlockY() + " " + target.getLocation().getBlockZ());
        sender.sendMessage(Language.PREFIX_COLOR + "God Mode: " + ChatColor.RESET + ((GodTask.GetTask(target) == null) ? "false" : "true"));
        sender.sendMessage(Language.PREFIX_COLOR + "AFK: " + ChatColor.RESET + ((AFKTask.GetTask(target) == null) ? "false" : "true"));
        sender.sendMessage(Language.PREFIX_COLOR + "Jail: " + ChatColor.RESET + ((JailTask.GetTask(target) == null) ? "false" : "true"));
        sender.sendMessage(Language.PREFIX_COLOR + "Vanish: " + ChatColor.RESET + ((VanishTask.GetTask(target) == null) ? "false" : "true"));

        return true;
    }
}
