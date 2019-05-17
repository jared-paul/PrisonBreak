package net.prisontech.prisonbreak.api.util.schematics;

import net.prisontech.prisonbreak.api.util.Tuple;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Map;

/**
 * Created by JPaul on 9/28/2016.
 */
public class Schematic
{
    private Map<Location, Tuple<Material, Byte>> blockData;
    private int width;
    private int length;
    private int height;

    public Schematic(Map<Location, Tuple<Material, Byte>> blockData, int width, int length, int height)
    {
        this.blockData = blockData;
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public Map<Location, Tuple<Material, Byte>> getBlockData()
    {
        return blockData;
    }

    public int getWidth()
    {
        return width;
    }

    public int getLength()
    {
        return length;
    }

    public int getHeight()
    {
        return height;
    }
}
