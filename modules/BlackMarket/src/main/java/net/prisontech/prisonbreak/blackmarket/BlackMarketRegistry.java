package net.prisontech.prisonbreak.blackmarket;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import io.netty.util.internal.ThreadLocalRandom;
import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.blackmarket.IBlackMarketRegistry;
import net.prisontech.prisonbreak.api.config.Registry;
import net.prisontech.prisonbreak.api.util.schematics.Paster;
import net.prisontech.prisonbreak.api.util.schematics.Schematic;
import net.prisontech.prisonbreak.api.util.serialization.LocationSerialization;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BlackMarketRegistry extends Registry implements IBlackMarketRegistry
{
    private FileConfiguration itemConfig;
    private File itemFile;
    private File schematicFile;
    private LinkedHashMap<Location, Integer> locations = Maps.newLinkedHashMap();
    private Map<Location, String> npcs = Maps.newHashMap();
    private Map<Material, Integer> itemPrices = Maps.newHashMap();
    private Map<String, Integer> sellBoosters = Maps.newHashMap();

    private List<BlackMarket> blackMarkets = Lists.newArrayList();

    public BlackMarketRegistry() throws IOException
    {
        super("BlackMarket");

        this.schematicFile = new File(PrisonBreak.getPlugin().getDataFolder() + "\\BlackMarkets\\" + config.get("PrisonBreak.BlackMarkets.Schematic") + ".schematic");
        schematicFile.getParentFile().mkdirs();

        if (!schematicFile.exists())
        {
            schematicFile.createNewFile();
        }

        this.itemFile = new File(PrisonBreak.getPlugin().getDataFolder() + "\\BlackMarkets\\ItemPrices.yml");

        if (!itemFile.exists())
        {
            itemFile.createNewFile();
        }

        itemConfig = YamlConfiguration.loadConfiguration(itemFile);
        itemConfig.options().copyDefaults(true);

        defaultItemPrices();


        spawnBlackMarket();

        spawnVillagers();

        spawnHolograms();
    }

    private void defaultItemPrices() throws IOException
    {
        String path = "PrisonBreak.BlackMarkets.ItemPrices";

        if (!itemConfig.isSet(path))
        {
            for (Material material : Material.values())
            {
                itemConfig.addDefault(path + "." + material.name(), 1);
            }
        }

        itemConfig.save(itemFile);
    }

    @Override
    public int getBoostPercent(Player player)
    {
        int boostPercent = 0;

        for (Map.Entry<String, Integer> boostEntry : sellBoosters.entrySet())
        {
            String permission = boostEntry.getKey();
            int boost = boostEntry.getValue();

            if (player.hasPermission(permission))
                boostPercent += boost;
        }

        return boostPercent;
    }

    @Override
    public Map<String, Integer> getSellBoosters()
    {
        if (sellBoosters.isEmpty())
        {
            String path = "PrisonBreak.BlackMarkets.Sell-Boosters";

            for (String sellBoosterName : config.getConfigurationSection(path).getKeys(false))
            {
                String sellBoosterPath = path + "." + sellBoosterName;

                String permission = config.getString(sellBoosterPath + ".Permission");
                int boostPercent = config.getInt(sellBoosterPath + ".Percentage");

                this.sellBoosters.put(permission, boostPercent);
            }
        }

        return sellBoosters;
    }

    @Override
    public Map<Material, Integer> getItemPrices()
    {
        if (itemPrices.isEmpty())
        {
            String path = "PrisonBreak.BlackMarkets.ItemPrices";

            for (String materialName : itemConfig.getConfigurationSection(path).getKeys(false))
            {
                String materialPath = path + "." + materialName;

                Material material = Material.valueOf(materialName.toUpperCase());
                int price = itemConfig.getInt(materialPath);

                this.itemPrices.put(material, price);
            }
        }

        return itemPrices;
    }

    @Override
    public Map<Location, String> getNPCs()
    {
        return this.npcs;
    }

    public LinkedHashMap<Location, Integer> getLocations()
    {
        String path = "PrisonBreak.BlackMarkets.Locations";

        if (locations.isEmpty())
        {
            for (String locations : config.getConfigurationSection(path).getKeys(false))
            {
                String locationPath = path + "." + locations;

                Location location = LocationSerialization.deserializeLocation(config.getString(locationPath + ".Location"), PrisonBreak.getWorld());
                int rotationAngle = config.getInt(locationPath + ".Rotation");

                this.locations.put(location, rotationAngle);
            }
        }

        return locations;
    }

    private void spawnBlackMarket()
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int randomIndex = random.nextInt(getLocations().keySet().size());

        //get random spawn location
        Map.Entry<Location, Integer> locationEntry = getEntry(randomIndex, getLocations());

        Location location = locationEntry.getKey();
        int rotation = locationEntry.getValue();

        Paster.rotate(rotation, schematicFile);
        Paster.paste(schematicFile, location);
        Paster.rotate(-rotation, schematicFile);
    }

    private void spawnVillagers()
    {
        String path = "PrisonBreak.BlackMarkets.NPCS";

        for (String npcs : config.getConfigurationSection(path).getKeys(false))
        {
            String npcPath = path + "." + npcs;

            String name = config.getString(npcPath + ".Name");
            final Location location = LocationSerialization.deserializeLocation(config.getString(npcPath + ".Location"), PrisonBreak.getWorld());
            location.getChunk().load();

            final Villager villager = (Villager) PrisonBreak.getWorld().spawnEntity(location, EntityType.VILLAGER);
            villager.setCustomName(name);
            villager.setCustomNameVisible(true);
            villager.setProfession(Villager.Profession.PRIEST);
            villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1000, false, false));

            this.npcs.put(location, name);
        }
    }

    private void spawnHolograms()
    {
        String path = "PrisonBreak.BlackMarkets.Holograms";

        for (String holograms : config.getConfigurationSection(path).getKeys(false))
        {
            String hologramPath = path + "." + holograms;

            String message = config.getString(hologramPath + ".Message");
            Location location = LocationSerialization.deserializeLocation(config.getString(hologramPath + ".Location"), PrisonBreak.getWorld());
            location.getChunk().load();

            ArmorStand hologram = (ArmorStand) PrisonBreak.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            hologram.setVisible(false);
            hologram.setGravity(false);
            hologram.setCustomName(message);
            hologram.setCustomNameVisible(true);
        }
    }

    private Map.Entry getEntry(int id, LinkedHashMap map)
    {
        Iterator iterator = map.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry) iterator.next();

            if (i == id)
            {
                return entry;
            }

            i++;
        }

        return null;
    }
}
