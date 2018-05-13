package com.sscf.investment.sdk.utils;

import java.lang.reflect.Field;

/**
 * Created by davidwei on 2016/10/08
 */
public final class ReflectUtils {

    public static void setFieldValue(Object o, String key, Object value) {
        final Field field = getField(o.getClass(), key);

        if (field == null) {
            return;
        }

        final Class fieldType = field.getType();
        if (String.class.equals(fieldType)) {
            if (value != null) {
                value = value.toString();
            }
            try {
                field.set(o, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } if (int.class.equals(fieldType)) {
            try {
                field.setInt(o, value == null ? 0 : (Integer) value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (double.class.equals(fieldType)) {
            try {
                field.setDouble(o, value == null ? 0d : (Double) value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (float.class.equals(fieldType)) {
            try {
                field.setFloat(o, value == null ? 0f : (Float) value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (long.class.equals(fieldType)) {
            try {
                field.setLong(o, value == null ? 0L : (Long) value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (byte.class.equals(fieldType)) {
            try {
                field.setByte(o, value == null ? 0 : (Byte) value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (short.class.equals(fieldType)) {
            try {
                field.setShort(o, value == null ? 0 : (Short) value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (boolean.class.equals(fieldType)) {
            try {
                field.setBoolean(o, value == null ? false : (Boolean) value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                field.set(o, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Field getField(final Class<?> clazz, String key) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(key);
            field.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return field;
    }

    public static Object getFieldValue(Object o, String key) {
        final Field field = getField(o.getClass(), key);

        if (field != null) {
            try {
                return field.get(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
