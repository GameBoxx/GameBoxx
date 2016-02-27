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

package info.gameboxx.gameboxx.messages;

import java.util.ArrayList;
import java.util.List;

public enum Language {
    ENGLISH("English", "en"),
    FRENCH("French", "fr"),
    GERMAN("German", "de"),
    DUTCH("Dutch", "nl"),
    ITALIAN("Italian", "it"),
    SPANISH("Spanish", "es"),
    CZECH("Czech", "cz"),
    POLISH("Polish", "pl"),
    PORTUGUESE("Portuguese", "pt"),
    SWEDISH("Swedish", "sw"),
    DANISH("Danish", "da"),
    NORWEGIAN("Norwegian", "no"),
    GREEK("Greek", "gr"),
    BULGARIAN("Bulgarian", "bg"),
    ESTONIAN("Estonian", "et"),
    RUSSIAN("Russian", "ru"),
    JAPANESE("Japanese", "ja"),
    KOREAN("Korean", "ko"),
    CHINESE("Chinese", "zh"),
    HINDI("Hindi", "hi"),
    INDONESIAN("Indonesian", "id"),
    ARABIC("Arabic", "ar"),
    TURKISH("Turkish", "tr"),
    PERSIAN("Persian", "fa"),
    AFRIKAANS("Afrikaans", "af"),
    ;


    private static final List<String> NAMES = new ArrayList<>();

    static {
        for (Language lang : Language.values()) {
            NAMES.add(lang.getName() + " [" + lang.getID() + "]");
        }
    }


    private String name;
    private String id;

    Language(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return id;
    }

    public static Language find(String name) {
        for (Language lang : values()) {
            if (lang.getID().equalsIgnoreCase(name) || lang.getName().equalsIgnoreCase(name)) {
                return lang;
            }
        }
        return null;
    }

    public static List<String> getNames() {
        return NAMES;
    }
}