package com.WiseHollow.Fundamentals;

import org.bukkit.ChatColor;

/**
 * Created by John on 10/13/2016.
 */
public class Language
{
    public static final String PREFIX = ChatColor.GREEN + "" + ChatColor.BOLD + "[Fundamentals] "+ ChatColor.RESET;
    public static final String PREFIX_COLOR = ChatColor.GREEN + "";
    public static final String PREFIX_WARNING = ChatColor.DARK_RED + "[Warn] ";
    public static final String DoesNotHavePermission = PREFIX_WARNING + "You do not have permission for this function.";
    public static final String YouMustBeLoggedIn = PREFIX_WARNING + "You must be logged in to do this.";
    public static final String PlayerMustBeLoggedIn = PREFIX_WARNING + "That player must be logged in for this.";
}
