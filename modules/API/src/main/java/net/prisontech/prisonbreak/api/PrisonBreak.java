package net.prisontech.prisonbreak.api;

import net.prisontech.prisonbreak.api.blackmarket.IBlackMarketRegistry;
import net.prisontech.prisonbreak.api.kits.IKitRegistry;
import net.prisontech.prisonbreak.api.logging.Log;
import net.prisontech.prisonbreak.api.loot.IChestRegistry;
import net.prisontech.prisonbreak.api.util.BungeeChannelApi;
import org.bukkit.World;

public class PrisonBreak
{
    private static IPrisonBreakMain PLUGIN;
    public static final Log LOG = new Log("[PrisonBreak]");

    public static void setPlugin(IPrisonBreakMain plugin)
    {
        if (PLUGIN != null)
        {
            return;
        }

        PLUGIN = plugin;
    }

    public static IPrisonBreakMain getPlugin()
    {
        return PLUGIN;
    }

    public static World getWorld()
    {
        return PLUGIN.getWorld();
    }

    public static BungeeChannelApi getChannelAPI()
    {
        return PLUGIN.getChannelAPI();
    }

    public static IMainConfig getMainConfig()
    {
        return PLUGIN.getMainConfig();
    }

    public static IKitRegistry getKitRegistry()
    {
        return PLUGIN.getKitRegistry();
    }

    public static IChestRegistry getChestRegistry()
    {
        return PLUGIN.getChestRegistry();
    }

    public static IBlackMarketRegistry getBlackMarketRegistry()
    {
        return PLUGIN.getBlackMarketRegistry();
    }
}
