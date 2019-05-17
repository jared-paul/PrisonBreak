package net.prisontech.prisonbreak.loot;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.config.Registry;
import net.prisontech.prisonbreak.api.kits.IKit;
import net.prisontech.prisonbreak.api.loot.IChestRegistry;
import net.prisontech.prisonbreak.api.loot.ItemWrapper;
import net.prisontech.prisonbreak.api.loot.LocationValue;
import net.prisontech.prisonbreak.api.util.Strings;
import net.prisontech.prisonbreak.api.util.serialization.LocationSerialization;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChestRegistry extends Registry implements IChestRegistry
{
    private Set<Location> chestLocations = Sets.newHashSet();
    private LinkedList<ItemWrapper> chestContents = Lists.newLinkedList();

    public ChestRegistry() throws IOException
    {
        super("Chests");
    }

    @Override
    public Set<Location> getChestLocations()
    {
        String locationPath = "PrisonBreak.Chests";

        if (this.chestLocations.isEmpty())
        {
            for (Object locationString : this.config.getConfigurationSection(locationPath).getList("Locations"))
            {
                Location location = LocationSerialization.deserializeLocation((String) locationString, PrisonBreak.getWorld());
                this.chestLocations.add(location);
            }
        }

        return this.chestLocations;
    }

    @Override
    public LinkedList<ItemWrapper> getChestContents()
    {
        if (this.chestContents.isEmpty())
        {
            String itemPath = "PrisonBreak.Chests.Items";

            for (String item : this.config.getConfigurationSection(itemPath).getKeys(false))
            {
                String absoluteItemPath = itemPath + "." + item;

                try
                {
                    Material material = Material.valueOf(item.toUpperCase());

                    int amountLow;
                    int amountHigh;

                    if (this.config.getInt(absoluteItemPath + ".Amount") != 0)
                    {
                        amountLow = this.config.getInt(absoluteItemPath + ".Amount");
                        amountHigh = amountLow;

                        PrisonBreak.LOG.severe(item);
                    }
                    else
                    {
                        String amountTotal = this.config.getString(absoluteItemPath + ".Amount");
                        String[] split = amountTotal.split(":");
                        amountLow = Integer.valueOf(split[0]);
                        amountHigh = Integer.valueOf(split[1]);
                    }

                    int chance = this.config.getInt(absoluteItemPath + ".Chance");

                    this.chestContents.add(new ItemWrapper(material, amountLow, amountHigh, chance));
                } catch (IllegalArgumentException exception)
                {
                    PrisonBreak.LOG.severe(item + " is not a valid material");
                }
            }

            String kitPath = "PrisonBreak.Chests.Kits";

            for (String kitName : config.getConfigurationSection(kitPath).getKeys(false))
            {
                String absoluteKitPath = kitPath + "." + kitName;

                IKit kit = PrisonBreak.getKitRegistry().getKit(kitName);

                int chance = config.getInt(absoluteKitPath + ".Chance");

                this.chestContents.add(new ItemWrapper(Material.PAPER, ChatColor.GREEN + kit.getName(), Lists.<String>newArrayList(), 1, 1, chance));
            }
        }

        return this.chestContents;
    }
}
