package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Settings;
import com.WiseHollow.Fundamentals.Tasks.AFKTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandEnchant implements CommandExecutor
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
        if (!sender.hasPermission("Fundamentals.Enchant"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (player.getInventory().getItemInMainHand() == null)
        {
            player.sendMessage(Language.PREFIX_WARNING + "You must have an item in your hand. ");
            return true;
        }

        if (args.length < 1)
            return false;

        Enchantment enchantment = null;
        int level = 1;

        try
        {
            enchantment = Enchantment.getByName(args[0].toUpperCase());
            if (args.length > 1)
                level = Integer.valueOf(args[1]);
        }
        catch(Exception ex)
        {
            player.sendMessage(ex.getMessage());
            return true;
        }

        if (! Settings.AllowUnsafeEnchantments)
            player.getInventory().getItemInMainHand().addEnchantment(enchantment, level);
        else
            player.getInventory().getItemInMainHand().addUnsafeEnchantment(enchantment, level);

        player.sendMessage(Language.PREFIX + "Enchantment: " + enchantment.getName() + " added at level " + level);

        return true;
    }
}
