package net.prisontech.prisonbreak.kits;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_12_R1.Item;
import net.prisontech.prisonbreak.api.kits.IKit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Kit implements IKit
{
    private String name;
    private String permission;
    private LinkedHashMap<Material, Integer> contents;

    public Kit(String name, String permission)
    {
        this.name = name;
        this.permission = permission;
        this.contents = Maps.newLinkedHashMap();
    }

    public Kit(String permission, LinkedHashMap<Material, Integer> contents)
    {
        this.permission = permission;
        this.contents = contents;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getPermission()
    {
        return permission;
    }

    @Override
    public LinkedHashMap<Material, Integer> getContents()
    {
        return this.contents;
    }

    @Override
    public void setContents(LinkedHashMap<Material, Integer> contents)
    {
        this.contents = contents;
    }

    @Override
    public ItemStack createSlip()
    {
        ItemStack slip = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta = slip.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + this.name);
        slip.setItemMeta(itemMeta);

        setMaxStackSize(CraftItemStack.asNMSCopy(slip).getItem(), 1);

        return slip;
    }

    private void setMaxStackSize(Item item, int stackAmount)
    {
        try
        {
            Field field = Item.class.getDeclaredField("maxStackSize");
            field.setAccessible(true);
            field.setInt(item, stackAmount);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
