package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 10/13/2016.
 */
public class CommandGive implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Give"))
        {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length > 1)
        {
            Player target = PlayerUtil.GetPlayer(args[0]);
            if (target == null || !target.isOnline())
            {
                sender.sendMessage(Language.YouMustBeLoggedIn);
                return true;
            }

            String[] materialData = args[1].toUpperCase().split(":");

            Material given;
            try
            {
                given = Material.valueOf(materialData[0]);
            }
            catch(Exception ex)
            {
                sender.sendMessage(Language.PREFIX_WARNING + "Invalid material given.");
                return false;
            }
            int amount = 64;
            if (args.length == 3)
            {
                try
                {
                    amount = Integer.valueOf(args[2]);
                }
                catch(Exception ex)
                {
                    sender.sendMessage(Language.PREFIX_WARNING + "Invalid amount to give.");
                    return false;
                }
            }

            if (materialData.length == 1)
                target.getInventory().addItem(new ItemStack(given, amount));
            else
            {
                byte data;
                try
                {
                    data = Byte.valueOf(materialData[1]);
                }
                catch(Exception ex)
                {
                    sender.sendMessage(ex.getMessage());
                    return false;
                }

                target.getInventory().addItem(new ItemStack(given, amount, data));
            }
            target.sendMessage(Language.PREFIX_COLOR + "You were given x" + amount + " of " + given.name());

        }
        else
        {
            return false;
        }

        return true;
    }
}
