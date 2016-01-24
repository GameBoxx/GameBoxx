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
