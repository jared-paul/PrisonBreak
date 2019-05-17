package net.prisontech.prisonbreak.api.blackmarket;

import net.prisontech.prisonbreak.api.config.IRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface IBlackMarketRegistry extends IRegistry
{
    int getBoostPercent(Player player);

    Map<String, Integer> getSellBoosters();

    Map<Location, String> getNPCs();

    Map<Material, Integer> getItemPrices();
}
