package net.prisontech.prisonbreak.api.loot;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemWrapper
{
    private Material material;
    private String name;
    private List<String> lore;
    private int amountLow;
    private int amountHigh;
    private int chance;

    public ItemWrapper(Material material, int amountLow, int amountHigh, int chance)
    {
        this(material, "", Lists.newArrayList(), amountLow, amountHigh, chance);
    }

    public ItemWrapper(Material material, String name, List<String> lore, int amountLow, int amountHigh, int chance)
    {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.amountLow = amountLow;
        this.amountHigh = amountHigh;
        this.chance = chance;
    }

    public Material getMaterial()
    {
        return material;
    }

    public String getName()
    {
        return name;
    }

    public List<String> getLore()
    {
        return lore;
    }

    public int getAmountLow()
    {
        return amountLow;
    }

    public int getAmountHigh()
    {
        return amountHigh;
    }

    public int getChance()
    {
        return chance;
    }

    @Override
    public String toString()
    {
        return "[Material=" + material.name() + ", AmountLow=" + amountLow + ", AmountHigh=" + amountHigh + ", Chance=" + chance + "]";
    }
}
