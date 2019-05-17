package net.prisontech.prisonbreak.api;

import net.prisontech.prisonbreak.api.blackmarket.IBlackMarketRegistry;
import net.prisontech.prisonbreak.api.kits.IKitRegistry;
import net.prisontech.prisonbreak.api.loot.IChestRegistry;
import net.prisontech.prisonbreak.api.util.BungeeChannelApi;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public interface IPrisonBreakMain extends Plugin
{
    IKitRegistry getKitRegistry();

    IChestRegistry getChestRegistry();

    IBlackMarketRegistry getBlackMarketRegistry();

    BungeeChannelApi getChannelAPI();

    World getWorld();

    IMainConfig getMainConfig();
}
