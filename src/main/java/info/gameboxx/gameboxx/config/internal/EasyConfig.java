/*
 The MIT License (MIT)

 Copyright (c) 2016 GameBoxx <http://gameboxx.info>
 Copyright (c) 2016 contributors

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

package info.gameboxx.gameboxx.config.internal;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;


/*/**
 * Simple configuration class.
 * To use this class just extend from it.
 * <p>
 * It will create config options for the fields in the config class.
 * private, static, final and transient fields will be ignored.
 * So if you want to create a field and not have it in the config you should make it private transient static or/and final.
 * <p>
 * Double underscores in field names will be replaces with new config sections (indents).
 * <p>
 * The following data modifier for fields are supported: String, Boolean, Integer, Double, Float, Long, Byte, Char, Location, Vector & UUID. List, Map & Enum (The data inside these can be any of these data modifier. However, for maps the key has to be a string.)
 * <p>
 * To create a new config file call setFile() on the config class that extends from this.
 * Then call load() to load in all config values in to the fields.
 * And call save() to save the values from the fields to the config file.
 *
 * @author (original idea)md_5
 * @author (by)codename_B
 * @author (by)MrFigg
 * @author (customized)Worstboy
 */
public abstract class EasyConfig {

    protected transient File file = null;
    protected transient YamlConfiguration conf = new YamlConfiguration();
    private transient Field[] fields;

    public void setFields(Field... fields) {
        this.fields = new Field[fields.length];
        int i = 0;
        for (Field field : fields) {
            this.fields[i] = field;
            i++;
        }
    }

    /**
     * Set the file (location) for the config.
     * Can pass in a File, Plugin, or String with the path like: "plugins/Essence/Messages.yml"
     * Must be called before using config.load() or config.save();
     */
    public EasyConfig setFile(Object input) {
        if (input == null) {
            new InvalidConfigurationException("File cannot be null!").printStackTrace();
        } else if (input instanceof File) {
            file = (File) input;
        } else if (input instanceof Plugin) {
            file = getFile((Plugin) input);
        } else if (input instanceof String) {
            file = new File((String) input);
        }
        return this;
    }

    /**
     * Load the config file in to memory.
     * The value of all fields will be set with the config values.
     */
    public void load() {
        if (file != null) {
            try {
                if (!file.exists()) {
                    if (file.getParentFile() != null) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }

                conf.load(file);
                if (fields == null) {
                    onLoad(conf);
                } else onLoad(conf, fields);
                conf.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new InvalidConfigurationException("File cannot be null!").printStackTrace();
        }
    }

    /**
     * Save the config file to disk.
     * All values from the fields will be placed in the config.
     */
    public void save() {
        if (file != null) {
            try {
                if (!file.exists()) {
                    if (file.getParentFile() != null) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
                if (fields == null) {
                    onSave(conf);
                } else onSave(conf, fields);
                conf.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new InvalidConfigurationException("File cannot be null!").printStackTrace();
        }
    }


    private void onLoad(ConfigurationSection cs) throws Exception {
        onLoad(cs, getClass().getDeclaredFields());
    }

    private void onLoad(ConfigurationSection cs, Field[] fields) throws Exception {
        for (Field field : fields) {
            String path = field.getName().replaceAll("__", ".");
            if (doSkip(field)) {
                continue;
            }
            if (cs.isSet(path)) {
                field.set(this, loadObject(field, cs, path));
            } else {
                cs.set(path, saveObject(field.get(this), field, cs, path));
            }
        }
    }

    private void onSave(ConfigurationSection cs) throws Exception {
        onSave(cs, getClass().getDeclaredFields());
    }

    private void onSave(ConfigurationSection cs, Field[] fields) throws Exception {
        for (Field field : fields) {
            String path = field.getName().replaceAll("__", ".");
            if (doSkip(field)) {
                continue;
            }
            cs.set(path, saveObject(field.get(this), field, cs, path));
        }
    }

    private Object loadObject(Field field, ConfigurationSection cs, String path) throws Exception {
        return loadObject(field, cs, path, 0);
    }

    private Object saveObject(Object obj, Field field, ConfigurationSection cs, String path) throws Exception {
        return saveObject(obj, field, cs, path, 0);
    }

    private Object loadObject(Field field, ConfigurationSection cs, String path, int depth) throws Exception {
        Class clazz = getClassAtDepth(field.getGenericType(), depth);
        if (EasyConfig.class.isAssignableFrom(clazz) && isConfigurationSection(cs.get(path))) {
            return getSimpleConfig(clazz, cs.getConfigurationSection(path));
        } else if (UUID.class.isAssignableFrom(clazz) && isUUID(cs.get(path))) {
            return getUUID((String) cs.get(path));
        } else if (Location.class.isAssignableFrom(clazz) && isJSON(cs.get(path))) {
            return getLocation((String) cs.get(path));
        } else if (Vector.class.isAssignableFrom(clazz) && isJSON(cs.get(path))) {
            return getVector((String) cs.get(path));
        } else if (Map.class.isAssignableFrom(clazz) && isConfigurationSection(cs.get(path))) {
            return getMap(field, cs.getConfigurationSection(path), path, depth);
        } else if (clazz.isEnum() && isString(cs.get(path))) {
            return getEnum(clazz, (String) cs.get(path));
        } else if (List.class.isAssignableFrom(clazz) && isConfigurationSection(cs.get(path))) {
            Class subClazz = getClassAtDepth(field.getGenericType(), depth + 1);
            if (EasyConfig.class.isAssignableFrom(subClazz) || Location.class.isAssignableFrom(subClazz) || Vector.class.isAssignableFrom(subClazz) ||
                    Map.class.isAssignableFrom(subClazz) || List.class.isAssignableFrom(subClazz) || subClazz.isEnum()) {
                return getList(field, cs.getConfigurationSection(path), path, depth);
            } else {
                return cs.get(path);
            }
        } else {
            return cs.get(path);
        }
    }

    private Object saveObject(Object obj, Field field, ConfigurationSection cs, String path, int depth) throws Exception {
        Class clazz = getClassAtDepth(field.getGenericType(), depth);
        if (EasyConfig.class.isAssignableFrom(clazz) && isSimpleConfig(obj)) {
            return getSimpleConfig((EasyConfig) obj, path, cs);
        } else if (UUID.class.isAssignableFrom(clazz) && isUUID(obj)) {
            return getUUID((UUID) obj);
        } else if (Location.class.isAssignableFrom(clazz) && isLocation(obj)) {
            return getLocation((Location) obj);
        } else if (Vector.class.isAssignableFrom(clazz) && isVector(obj)) {
            return getVector((Vector) obj);
        } else if (Map.class.isAssignableFrom(clazz) && isMap(obj)) {
            return getMap((Map) obj, field, cs, path, depth);
        } else if (clazz.isEnum() && isEnum(clazz, obj)) {
            return getEnum((Enum) obj);
        } else if (List.class.isAssignableFrom(clazz) && isList(obj)) {
            Class subClazz = getClassAtDepth(field.getGenericType(), depth + 1);
            if (EasyConfig.class.isAssignableFrom(subClazz) || Location.class.isAssignableFrom(subClazz) || Vector.class.isAssignableFrom(subClazz) ||
                    Map.class.isAssignableFrom(subClazz) || List.class.isAssignableFrom(subClazz) || subClazz.isEnum()) {
                return getList((List) obj, field, cs, path, depth);
            } else {
                return obj;
            }
        } else {
            return obj;
        }
    }

    private Class getClassAtDepth(Type type, int depth) throws Exception {
        if (depth <= 0) {
            String className = type.toString();
            if (className.length() >= 6 && className.substring(0, 6).equalsIgnoreCase("class ")) {
                className = className.substring(6);
            }
            if (className.contains("<")) {
                className = className.substring(0, className.indexOf("<"));
            }
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                if (className.equalsIgnoreCase("byte")) return Byte.class;
                if (className.equalsIgnoreCase("short")) return Short.class;
                if (className.equalsIgnoreCase("int")) return Integer.class;
                if (className.equalsIgnoreCase("long")) return Long.class;
                if (className.equalsIgnoreCase("float")) return Float.class;
                if (className.equalsIgnoreCase("double")) return Double.class;
                if (className.equalsIgnoreCase("char")) return Character.class;
                if (className.equalsIgnoreCase("boolean")) return Boolean.class;
                throw ex;
            }
        }
        depth--;
        ParameterizedType pType = (ParameterizedType) type;
        Type[] typeArgs = pType.getActualTypeArguments();
        return getClassAtDepth(typeArgs[typeArgs.length - 1], depth);
    }



    /*
    * class detection
    */

    private boolean isString(Object obj) {
        return obj instanceof String;
    }

    private boolean isUUID(Object o) {
        if (o instanceof UUID) {
            return true;
        }
        if (o instanceof String) {
            try {
                UUID.fromString((String) o);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean isConfigurationSection(Object o) {
        try {
            return (ConfigurationSection) o != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isJSON(Object obj) {
        try {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.startsWith("{")) {
                    return new JSONParser().parse(str) != null;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isSimpleConfig(Object obj) {
        try {
            return (EasyConfig) obj != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isLocation(Object obj) {
        try {
            return (Location) obj != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isVector(Object obj) {
        try {
            return (Vector) obj != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isMap(Object obj) {
        try {
            return (Map) obj != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isList(Object obj) {
        try {
            return (List) obj != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isEnum(Class clazz, Object obj) {
        if (!clazz.isEnum()) return false;
        for (Object constant : clazz.getEnumConstants()) {
            if (constant.equals(obj)) {
                return true;
            }
        }
        return false;
    }


    /*
    * loading conversion
    */

    private EasyConfig getSimpleConfig(Class clazz, ConfigurationSection cs) throws Exception {
        EasyConfig obj = (EasyConfig) clazz.newInstance();
        obj.onLoad(cs);
        return obj;
    }

    private UUID getUUID(String string) {
        try {
            return UUID.fromString(string);
        } catch (Exception e) {
            return null;
        }
    }

    private Location getLocation(String json) throws Exception {
        JSONObject data = (JSONObject) new JSONParser().parse(json);

        World world = Bukkit.getWorld((String) data.get("world"));
        double x = Double.parseDouble((String) data.get("x"));
        double y = Double.parseDouble((String) data.get("y"));
        double z = Double.parseDouble((String) data.get("z"));
        float pitch = Float.parseFloat((String) data.get("pitch"));
        float yaw = Float.parseFloat((String) data.get("yaw"));

        Location loc = new Location(world, x, y, z);
        loc.setPitch(pitch);
        loc.setYaw(yaw);
        return loc;
    }

    private Vector getVector(String json) throws Exception {
        JSONObject data = (JSONObject) new JSONParser().parse(json);

        double x = Double.parseDouble((String) data.get("x"));
        double y = Double.parseDouble((String) data.get("y"));
        double z = Double.parseDouble((String) data.get("z"));

        return new Vector(x, y, z);
    }

    private Map getMap(Field field, ConfigurationSection cs, String path, int depth) throws Exception {
        depth++;
        Set<String> keys = cs.getKeys(false);
        Map map = new HashMap();
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                Object in = cs.get(key);
                in = loadObject(field, cs, key, depth);
                map.put(key, in);
            }
        }
        return map;
    }

    private List getList(Field field, ConfigurationSection cs, String path, int depth) throws Exception {
        depth++;
        int listSize = cs.getKeys(false).size();
        String key = path;
        if (key.lastIndexOf(".") >= 0) {
            key = key.substring(key.lastIndexOf("."));
        }
        List list = new ArrayList();
        if (listSize > 0) {
            int loaded = 0;
            int i = 0;
            while (loaded < listSize) {
                if (cs.isSet(key + i)) {
                    Object in = cs.get(key + i);
                    in = loadObject(field, cs, key + i, depth);
                    list.add(in);
                    loaded++;
                }
                i++;
                if (i > (listSize * 3)) {
                    loaded = listSize;
                }
            }
        }
        return list;
    }

    private Enum getEnum(Class clazz, String string) throws Exception {
        if (!clazz.isEnum()) {
            throw new Exception("Class " + clazz.getName() + " is not an enum.");
        }
        for (Object constant : clazz.getEnumConstants()) {
            if (((Enum) constant).toString().equals(string)) {
                return (Enum) constant;
            }
        }
        throw new Exception("String " + string + " not a valid enum constant for " + clazz.getName());
    }


    /*
    * saving conversion
    */

    private ConfigurationSection getSimpleConfig(EasyConfig obj, String path, ConfigurationSection cs) throws Exception {
        ConfigurationSection subCS = cs.createSection(path);
        obj.onSave(subCS);
        return subCS;
    }

    private String getUUID(UUID uuid) {
        return uuid.toString();
    }

    private String getLocation(Location loc) {
        String ret = "{";
        ret += "\"world\":\"" + loc.getWorld().getName() + "\"";
        ret += ",\"x\":\"" + loc.getX() + "\"";
        ret += ",\"y\":\"" + loc.getY() + "\"";
        ret += ",\"z\":\"" + loc.getZ() + "\"";
        ret += ",\"pitch\":\"" + loc.getPitch() + "\"";
        ret += ",\"yaw\":\"" + loc.getYaw() + "\"";
        ret += "}";
        if (!isJSON(ret)) {
            return getLocationJSON(loc);
        }
        try {
            getLocation(ret);
        } catch (Exception ex) {
            return getLocationJSON(loc);
        }
        return ret;
    }

    private String getLocationJSON(Location loc) {
        JSONObject data = new JSONObject();

        data.put("world", loc.getWorld().getName());
        data.put("x", String.valueOf(loc.getX()));
        data.put("y", String.valueOf(loc.getY()));
        data.put("z", String.valueOf(loc.getZ()));
        data.put("pitch", String.valueOf(loc.getPitch()));
        data.put("yaw", String.valueOf(loc.getYaw()));

        return data.toJSONString();
    }

    private String getVector(Vector vec) {
        String ret = "{";
        ret += "\"x\":\"" + vec.getX() + "\"";
        ret += ",\"y\":\"" + vec.getY() + "\"";
        ret += ",\"z\":\"" + vec.getZ() + "\"";
        ret += "}";
        if (!isJSON(ret)) {
            return getVectorJSON(vec);
        }
        try {
            getVector(ret);
        } catch (Exception ex) {
            return getVectorJSON(vec);
        }
        return ret;
    }

    private String getVectorJSON(Vector vec) {
        JSONObject data = new JSONObject();

        data.put("x", String.valueOf(vec.getX()));
        data.put("y", String.valueOf(vec.getY()));
        data.put("z", String.valueOf(vec.getZ()));

        return data.toJSONString();
    }

    private ConfigurationSection getMap(Map map, Field field, ConfigurationSection cs, String path, int depth) throws Exception {
        depth++;
        ConfigurationSection subCS = cs.createSection(path);
        Set<String> keys = map.keySet();
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                Object out = map.get(key);
                out = saveObject(out, field, cs, path + "." + key, depth);
                subCS.set(key, out);
            }
        }
        return subCS;
    }

    private ConfigurationSection getList(List list, Field field, ConfigurationSection cs, String path, int depth) throws Exception {
        depth++;
        ConfigurationSection subCS = cs.createSection(path);
        String key = path;
        if (key.lastIndexOf(".") >= 0) {
            key = key.substring(key.lastIndexOf("."));
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object out = list.get(i);
                out = saveObject(out, field, cs, path + "." + key + (i + 1), depth);
                subCS.set(key + (i + 1), out);
            }
        }
        return subCS;
    }

    private String getEnum(Enum enumObj) {
        return enumObj.toString();
    }


    private boolean doSkip(Field field) {
        return Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()) || Modifier.isPrivate(field.getModifiers());
    }

    private File getFile(Plugin plugin) {
        return new File(plugin.getDataFolder(), "config.yml");
    }
}