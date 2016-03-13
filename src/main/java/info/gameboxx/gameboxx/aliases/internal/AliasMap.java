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

package info.gameboxx.gameboxx.aliases.internal;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class AliasMap<T> {

    protected static Map<Class<? extends AliasMap>, AliasMap> aliasMaps = new HashMap<>();
    protected static final File ALIASES_FOLDER = new File(GameBoxx.get().getDataFolder(), "aliases");

    protected final String name;
    protected final List<String> nameAliases;

    protected Map<T, Alias<T>> map = new HashMap<>();
    protected Map<String, T> search = new HashMap<>();
    protected Map<String, List<String>> aliasMap = new HashMap<>();

    private YamlConfiguration config;
    private File file;

    public AliasMap(String name, File file, String... nameAliases) {
        this.file = file;
        if (file.isDirectory()) {
            file.mkdirs();
        } else {
            file.getParentFile().mkdirs();
        }
        this.name = name;
        this.nameAliases = new ArrayList<>(Arrays.asList(nameAliases));
        load();
    }

    public static <T extends AliasMap> T getMap(Class<? extends AliasMap> type) {
        return (T)Utils.convertInstance(aliasMaps.get(type), type);
    }

    public void load() {
        search.clear();
        aliasMap.clear();
        map.clear();

        config = YamlConfiguration.loadConfiguration(file);
        onLoad();

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void onLoad();

    public String getKey(T key) {
        return key.toString();
    }

    public String getCfgKey(T key, String type) {
        return name + "." + getKey(key) + "." + type;
    }

    public void add(T key, String name, String... aliases) {
        String configKey = this.name + "." + key.toString();
        if (config.contains(getCfgKey(key, "name"))) {
            name = config.getString(getCfgKey(key, "name"));
        } else {
            config.set(getCfgKey(key, "name"), name);
        }

        List<String> aliasList = new ArrayList<>();
        if (config.contains(getCfgKey(key, "aliases"))) {
            aliasList = config.getStringList(getCfgKey(key, "aliases"));
        } else {
            config.set(getCfgKey(key, "aliases"), aliases);
            aliasList.addAll(Arrays.asList(aliases));
        }
        String nameAlias = name.toLowerCase().replace(" ", "").replace("_", "");
        if (!aliasList.contains(nameAlias)) {
            aliasList.add(nameAlias);
        }
        String keyAlias = getKey(key).toLowerCase().replace("_", "");
        if (!aliasList.contains(keyAlias)) {
            aliasList.add(keyAlias);
        }

        map.put(key, new Alias<T>(name, aliasList));
        aliasMap.put(name, aliasList);
        for (String alias : aliasList) {
            search.put(alias.toLowerCase().replace(" ", ""), key);
        }
    }

    public T _get(String string) {
        return search.get(string.toLowerCase().replace(" ", "").replace("_", ""));
    }

    public String _getName(T key) {
        if (key == null) {
            return null;
        }
        return map.get(key) == null ? Str.camelCase(key.toString()) : map.get(key).getName().replace(" ", "");
    }

    public String _getDisplayName(T key) {
        if (key == null) {
            return null;
        }
        return map.get(key) == null ? Str.camelCase(key.toString()) : map.get(key).getName();
    }

    public List<String> _getAliases(T key) {
        if (key == null) {
            return null;
        }
        return map.get(key) == null ? new ArrayList<>(Arrays.asList(key.toString().toLowerCase().replace("_", ""))) : map.get(key).getAliases();
    }

    public Map<String, List<String>> _getAliasMap() {
        return aliasMap;
    }
}
