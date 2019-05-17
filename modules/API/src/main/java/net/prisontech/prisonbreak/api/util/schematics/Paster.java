package net.prisontech.prisonbreak.api.util.schematics;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.util.io.file.FilenameException;
import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.util.Tuple;
import net.prisontech.prisonbreak.api.util.reflection.ReflectionUtil;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by JPaul on 8/11/2016.
 */
public class Paster
{
    public static String getCardinalDirection(Player player)
    {
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D)
        {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 45.0D))
        {
            return "W";
        }
        if ((45.0D <= rotation) && (rotation < 135.0D))
        {
            return "N";
        }
        if ((135.0D <= rotation) && (rotation < 225.0D))
        {
            return "E";
        }
        if ((225.0D <= rotation) && (rotation < 315.0D))
        {
            return "S";
        }
        if ((315.0D <= rotation) && (rotation < 360.0D))
        {
            return "W";
        }
        return null;
    }

    public static void rotate(int rotationAngle, File schematic)
    {
        try
        {
            saveSchematic(schematic, rotationAngle);
        } catch (IOException | DataException | FilenameException | EmptyClipboardException | MaxChangedBlocksException e)
        {
            e.printStackTrace();
        }
    }

    public static void rotate(BlockFace blockFace, File schemFile)
    {
        if (blockFace == BlockFace.WEST)
        {
            try
            {
                saveSchematic(schemFile, 180);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else if (blockFace == BlockFace.NORTH)
        {
            try
            {
                saveSchematic(schemFile, 270);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else if (blockFace == BlockFace.EAST)
        {
            try
            {
                saveSchematic(schemFile, 0);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else if (blockFace == BlockFace.SOUTH)
        {
            try
            {
                saveSchematic(schemFile, 90);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void saveSchematic(File saveFile, int rotateAngle)
            throws MaxChangedBlocksException, EmptyClipboardException, FilenameException, DataException, IOException
    {
        CuboidClipboard cuboidClipboard = SchematicFormat.MCEDIT.load(saveFile);
        if (rotateAngle != 0)
        {
            cuboidClipboard.rotate2D(rotateAngle);
        }

        SchematicFormat.MCEDIT.save(cuboidClipboard, saveFile);
    }

    public static void paste(File schematicFile, Location location)
    {
        Schematic schematic = loadSchematic(schematicFile, location);
        paste(schematic);
    }

    public static void paste(Schematic schematic)
    {
        for (Map.Entry<Location, Tuple<Material, Byte>> dataEntry : schematic.getBlockData().entrySet())
        {
            Location location = dataEntry.getKey();
            Material material = dataEntry.getValue().getA();
            byte data = dataEntry.getValue().getB();

            if (material == Material.WOOL)
            {
                if (data != DyeColor.PINK.getWoolData())
                {
                    location.getBlock().setTypeIdAndData(material.getId(), data, true);
                }
            }
            else
            {
                location.getBlock().setTypeIdAndData(material.getId(), data, true);
            }
        }
    }

    public static Schematic loadSchematic(File schematicFile, Location location)
    {
        return getBlockData(schematicFile, location);
    }

    public static Schematic getBlockData(File schematicFile, org.bukkit.Location location)
    {
        Map<Location, Tuple<Material, Byte>> all = Maps.newHashMap();
        int height = -1;
        int length = -1;
        int width = -1;

        try
        {
            SchematicFormat schematic = SchematicFormat.getFormat(schematicFile);
            CuboidClipboard clipboard = schematic.load(schematicFile);
            BaseBlock[][][] data = (BaseBlock[][][]) ReflectionUtil.getPrivateField(clipboard, "data");

            for (int x = 0; x < clipboard.getSize().getBlockX(); x++)
            {
                for (int y = 0; y < clipboard.getSize().getBlockY(); y++)
                {
                    for (int z = 0; z < clipboard.getSize().getBlockZ(); z++)
                    {
                        BaseBlock blockData = data[x][y][z];
                        Material material = Material.getMaterial(blockData.getId());

                        byte materialData = (byte) blockData.getData();

                        BlockVector blockVector = new com.sk89q.worldedit.Vector(x, y, z).add(new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ()).add(clipboard.getOffset())).toBlockPoint();
                        org.bukkit.Location blockLocation = new org.bukkit.Location(location.getWorld(), blockVector.getX(), blockVector.getY(), blockVector.getZ());

                        all.put(blockLocation, new Tuple<>(material, materialData));
                    }
                }
            }

            height = clipboard.getHeight();
            length = clipboard.getLength();
            width = clipboard.getWidth();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return new Schematic(all, width, length, height);
    }
}
