package info.gameboxx.gameboxx.config.messages;

import info.gameboxx.gameboxx.GameMsg;
import info.gameboxx.gameboxx.config.EasyConfig;

import java.util.HashMap;
import java.util.Map;

public class MessageCfg extends EasyConfig {

    private static MessageCfg instance;
    private int defaultsChanged = 0;
    public String prefix = "&8[&4GameBoxx&8] ";
    public Map<String, Map<String, String>> messages = new HashMap<String, Map<String, String>>();

    public MessageCfg(String fileName) {
        instance = this;
        this.setFile(fileName);
        load();

        for (GameMsg msg : GameMsg.values()) {
            setDefault(msg.getCategory(), msg.getName(), msg.getDefault());
        }
        saveDefaults();
    }

    public void setDefault(String category, String name, String message) {
        Map<String, String> catMessages = new HashMap<String, String>();
        if (messages.containsKey(category)) {
            catMessages = messages.get(category);
        }
        if (!catMessages.containsKey(name)) {
            catMessages.put(name, message);
            messages.put(category, catMessages);
            defaultsChanged++;
        }
    }

    public void saveDefaults() {
        if (defaultsChanged <= 0) {
            return;
        }
        defaultsChanged = 0;
        save();
    }

    public String getMessage(String category, String name) {
        if (!messages.containsKey(category)) {
            return null;
        }
        Map<String, String> catMesssages = messages.get(category);
        if (!catMesssages.containsKey(name)) {
            return null;
        }
        return catMesssages.get(name);
    }

    public static MessageCfg inst() {
        return instance;
    }
}
