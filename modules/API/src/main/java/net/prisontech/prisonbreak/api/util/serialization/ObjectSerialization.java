package net.prisontech.prisonbreak.api.util.serialization;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by JPaul on 4/7/2016.
 */
public class ObjectSerialization
{
    public static String toBase64(Object object) throws IllegalStateException
    {
        try
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(object);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Unable to save generalGUI stacks.", e);
        }
    }

/*
     * A method to serialize an inventory to Base64 string.

     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub and GreyWolf336 on GitHub. Modified it a little bit myself.
*/

    public static Object fromBase64(String data)
    {
        try
        {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Object object = dataInput.readObject();

            dataInput.close();
            return object;
        }
        catch (ClassNotFoundException e)
        {
            try
            {
                throw new IOException("Unable to decode class type.", e);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
