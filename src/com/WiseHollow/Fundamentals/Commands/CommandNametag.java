package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Tasks.AFKTask;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by John on 10/13/2016.
 */
public class CommandNametag implements CommandExecutor
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
        if (!sender.hasPermission("Fundamentals.Nametag"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length == 0)
            return false;

        String name = args[0].replaceAll("_", " ");

        ItemStack nameTag = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta meta = nameTag.getItemMeta();
        meta.setDisplayName(name);
        nameTag.setItemMeta(meta);

        player.getInventory().addItem(nameTag);

        return true;
    }
}
