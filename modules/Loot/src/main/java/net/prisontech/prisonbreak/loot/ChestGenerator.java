package net.prisontech.prisonbreak.loot;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.server.v1_12_R1.*;
import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.loot.ItemWrapper;
import net.prisontech.prisonbreak.api.loot.LocationValue;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ChestGenerator
{
    public ChestGenerator()
    {
        for (Location location : PrisonBreak.getChestRegistry().getChestLocations())
        {
            spawnChest(location, PrisonBreak.getChestRegistry().getChestContents());
        }
    }

    private void spawnChest(Location location, List<ItemWrapper> items)
    {
        Block block = location.add(0, 1, 0).getBlock();
        block.setType(Material.CHEST);

        fillChest((Chest) block.getState(), items);
    }

    private void fillChest(Chest chest, List<ItemWrapper> items)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (ItemWrapper itemWrapper : items)
        {
            int amount;

            if (itemWrapper.getAmountHigh() - itemWrapper.getAmountLow() == 0)
            {
                amount = itemWrapper.getAmountHigh();
            } else
            {
                amount = random.nextInt(itemWrapper.getAmountLow(), itemWrapper.getAmountHigh());
            }

            int chance = random.nextInt(100);
            if (chance <= itemWrapper.getChance())
            {
                ItemStack item = new ItemStack(itemWrapper.getMaterial(), amount);
                ItemMeta itemMeta = item.getItemMeta();

                itemMeta.setLore(Lists.<String>newArrayList());

                if (!itemWrapper.getLore().isEmpty())
                {
                    itemMeta.setLore(itemWrapper.getLore());
                }

                if (!itemWrapper.getName().equals(""))
                {
                    itemMeta.setDisplayName(itemWrapper.getName());
                }
                else
                {
                    itemMeta.getLore().add(ChatColor.GOLD + "Price: " + ChatColor.RED + PrisonBreak.getBlackMarketRegistry().getItemPrices().get(itemWrapper.getMaterial()));
                }

                item.setItemMeta(itemMeta);

                chest.getBlockInventory().addItem(item);
            }
        }
    }
}
