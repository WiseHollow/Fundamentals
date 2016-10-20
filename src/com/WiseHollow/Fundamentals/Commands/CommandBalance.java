package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Main;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.PlayerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandBalance implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Language.YouMustBeLoggedIn);
            return true;
        }

        Player player = (Player) sender;
        if (!sender.hasPermission("Fundamentals.Balance"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (Main.economy == null)
        {
            player.sendMessage(Language.PREFIX_WARNING + "You must have Vault installed to use this.");
            return true;
        }

        if (args.length > 0 && PermissionCheck.HasModeratorPermissions(sender))
        {
            Player target = PlayerUtil.GetPlayer(args[0]);
            if (target == null || !target.isOnline())
            {
                player.sendMessage(Language.YouMustBeLoggedIn);
                return true;
            }

            sender.sendMessage(Language.PREFIX + target.getName() + "'s balance: " + Main.economy.getBalance(target));
        }
        else
        {
            sender.sendMessage(Language.PREFIX + "Your balance: " + Main.economy.getBalance(player));
        }

        return true;
    }
}
