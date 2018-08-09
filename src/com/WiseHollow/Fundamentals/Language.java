package com.WiseHollow.Fundamentals;

import org.bukkit.ChatColor;

/**
 * Created by John on 10/13/2016.
 */
public class Language
{
    public static final String PREFIX = ChatColor.GOLD + "[Fundamentals] "+ ChatColor.RESET;
    public static final String PREFIX_COLOR = ChatColor.GOLD + "";
    public static final String PREFIX_WARNING = ChatColor.DARK_RED + "[Warn] ";
    public static final String DoesNotHavePermission = PREFIX_WARNING + "You do not have permission for this function.";
    public static final String YouMustBeLoggedIn = PREFIX_WARNING + "You must be logged in to do this.";
    public static final String PlayerMustBeLoggedIn = PREFIX_WARNING + "That player must be logged in for this.";
    public static final String MustBeANumber = PREFIX_WARNING + "What value you entered is not numerical.";
    public static final String CannotSendYourselfMoney = PREFIX_WARNING + "You cannot send yourself money.";
    public static final String SentMoney = PREFIX + "You sent %1 to %2.";
    public static final String ReceivedMoney = PREFIX + "You received %1 from %2.";
    public static final String MoneySetTo = PREFIX + "Your cash balance was set to %1";
}
