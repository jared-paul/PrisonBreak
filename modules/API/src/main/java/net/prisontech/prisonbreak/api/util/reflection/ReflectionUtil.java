package net.prisontech.prisonbreak.api.util.reflection;

import java.lang.reflect.Field;

/**
 * Created by JPaul on 2/20/2016.
 */
public class ReflectionUtil
{
    public static void setPrivateField(Object obj, String fieldName, Object newValue) throws Exception
    {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, newValue);
    }

    public static Object getPrivateField(Object obj, String fieldName) throws Exception
    {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static Field getField(Class clazz, String fieldToGet) throws NoSuchFieldException
    {
        Field field = clazz.getDeclaredField(fieldToGet);
        field.setAccessible(true);
        return field;
    }
}
