package net.prisontech.prisonbreak.api.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by JPaul on 1/11/2016.
 */
public class Strings
{
    public static String fixFontSize(String string, int size)
    {
        String upperCase = string.toUpperCase();

        for (int i = 0; i < string.length(); i++)
        {
            if (string.charAt(i) == 'I' || string.charAt(i) == ' ')
            {
                upperCase += " ";
            }
        }

        int spaces = size - string.length();
        spaces = (spaces * 2);

        for (int i = 0; i < spaces; i++)
        {
            upperCase += " ";
        }

        return upperCase;
    }

    public static String capitalizeFirst(String origin)
    {
        return origin.length() == 0 ? origin : origin.substring(0, 1).toUpperCase() + origin.substring(1);
    }

    public static String handleError(String message, String... error)
    {
        //have to convert to array due to replaceeach displaying x amount of lines based on the number of errors
        List<String> array = new ArrayList<>();
        Collections.addAll(array, error);
        String[] errors = array.toArray(new String[array.size()]);

        String capitalized = capitalizeFirst(message);

        return StringUtils.replaceEach(ChatColor.RED + capitalized, errors, handleErrorColours(errors));
    }

    private static String[] handleErrorColours(String[] errors)
    {
        List<String> colouredStrings = new ArrayList<>();

        for (String error : errors)
        {
            colouredStrings.add(ChatColor.YELLOW + error + ChatColor.RESET + ChatColor.RED);
        }

        return colouredStrings.toArray(new String[colouredStrings.size()]);
    }

    private static String[] handleKeywordColours(String[] keywords)
    {
        List<String> colouredStrings = new ArrayList<>();

        for (String keyword : keywords)
        {
            colouredStrings.add(ChatColor.AQUA + ChatColor.BOLD.toString() + keyword + ChatColor.RESET + ChatColor.RED);
        }

        return colouredStrings.toArray(new String[colouredStrings.size()]);
    }

    public static String join(String[] elements, String separator, int startIndex, int endIndex)
    {
        ;

        StringBuilder result = new StringBuilder();

        while (startIndex < endIndex)
        {
            if (result.length() != 0)
            {
                result.append(separator);
            }

            if (elements[startIndex] != null)
            {
                result.append(elements[startIndex]);
            }
            startIndex++;
        }

        return result.toString();
    }

    public static String join(String[] elements, String separator)
    {
        return join(elements, separator, 0, elements.length);
    }

    public static String join(List<String> elements, String separator, int startIndex, int size)
    {
        return join(elements.toArray(new String[elements.size()]), separator, startIndex, size);
    }

    public static String join(List<String> elements, String separator)
    {
        return join(elements, separator, 0, elements.size());
    }

    public static boolean stringContainsItemFromList(String inputString, List<String> items)
    {
        for (String item : items)
        {
            if (inputString.contains(item))
            {
                return true;
            }
        }
        return false;
    }

    public static String stringFromList(String inputString, List<String> elements)
    {
        for (String element : elements)
        {
            if (inputString.contains(element))
            {
                return element;
            }
        }

        return null;
    }
}
