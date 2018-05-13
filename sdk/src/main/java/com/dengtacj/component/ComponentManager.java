package com.dengtacj.component;

import java.util.HashMap;

/**
 * Created by davidwei on 2017/09/04
 */
public final class ComponentManager {

    private static ComponentManager instance;

    private final HashMap<String, Object> managers = new HashMap<>();

    private ComponentManager() {
    }

    public static ComponentManager getInstance() {
        if (instance == null) {
            synchronized (ComponentManager.class) {
                if (instance == null) {
                    instance = new ComponentManager();
                }
            }
        }
        return instance;
    }

    public void addManager(String managerName, Object managerImpl) {
        managers.put(managerName, managerImpl);
    }

    public void removeManager(String managerName) {
        managers.remove(managerName);
    }

    public Object getManager(String managerName) {
        return managers.get(managerName);
    }

    public static void registerComponent(String className) {
        final IComponent component = getComponent(className);
        if (component != null) {
            component.onCreate();
        }
    }

    public static void unregisterComponent(String className) {
        final IComponent component = getComponent(className);
        if (component != null) {
            component.onDestroy();
        }
    }

    private static IComponent getComponent(String className) {
        try {
            final Class clazz = Class.forName(className);
            return (IComponent) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
