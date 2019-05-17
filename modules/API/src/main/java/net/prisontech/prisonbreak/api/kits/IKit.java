package net.prisontech.prisonbreak.api.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IKit
{
    String getName();

    String getPermission();

    LinkedHashMap<Material, Integer> getContents();

    void setContents(LinkedHashMap<Material, Integer> contents);

    ItemStack createSlip();
}
