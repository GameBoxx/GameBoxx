/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 GameBoxx <http://gameboxx.info>
 *  Copyright (c) 2016 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package info.gameboxx.gameboxx.config.components;


import info.gameboxx.gameboxx.components.internal.GameComponent;
import info.gameboxx.gameboxx.config.EasyConfig;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Field;

public class ComponentCfg extends EasyConfig {


    public ComponentCfg(Class<? extends GameComponent> clazz, String name) {
        setFile(name);
        for (Field field : clazz.getDeclaredFields()) {
            setFields(field);
        }
        load();
    }

    public ComponentCfg(Class<? extends GameComponent> clazz, Plugin plugin) {
        setFile(plugin);
        for (Field field : clazz.getDeclaredFields()) {
            setFields(field);
        }
        load();
    }

    public ComponentCfg(Class<? extends GameComponent> clazz, File file) {
        setFile(file);
        for (Field field : clazz.getDeclaredFields()) {
            setFields(field);
        }
        load();
    }

}
