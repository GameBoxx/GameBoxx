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

package info.gameboxx.gameboxx.nms.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import info.gameboxx.gameboxx.nms.util.NMSUtil_V1_10_R1;
import info.gameboxx.gameboxx.util.Utils;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftMetaBook;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Field;
import java.util.*;

public class ItemUtils_V1_10_R1 implements ItemUtils {

    public BookMeta setBookPages(BookMeta meta, List<String> JSONPages) {
        if (meta == null) {
            return meta;
        }
        try {
            CraftMetaBook craftMetaBook = (CraftMetaBook)meta;
            craftMetaBook.pages.clear();
            for (String JSON : JSONPages) {
                craftMetaBook.pages.add(NMSUtil_V1_10_R1.serializeChat(JSON));
            }
        } catch (Exception e) {}
        return meta;
    }

    public List<String> getBookPages(BookMeta meta) {
        if (meta == null) {
            return new ArrayList<>();
        }
        List<String> pages = new ArrayList<>();
        try {
            CraftMetaBook craftMetaBook = (CraftMetaBook)meta;
            for (IChatBaseComponent page : craftMetaBook.pages) {
                //TODO: Add support in TextParser to convert JSON to text.
                pages.add(IChatBaseComponent.ChatSerializer.a(page));
            }
        } catch (Exception e) {}
        return pages;
    }


    @Override
    public SkullMeta setSkullTexture(SkullMeta meta, String skinUrl) {
        if (meta == null) {
            return meta;
        }

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", new String(Utils.getSkullTexture(skinUrl))));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return meta;
    }

    @Override
    public String getSkullTexture(SkullMeta meta) {
        if (meta == null) {
            return null;
        }

        String skinUrl = null;
        try {
            //Get the profile properties.
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            Collection<Property> properties = ((GameProfile) profileField.get(meta)).getProperties().get("textures");

            //Get the texture property.
            for (Property prop : properties) {
                if (prop.getName().equalsIgnoreCase("textures")) {
                    //Convert the encoded texture string to json.
                    String jsonString = new String(Base64.decodeBase64(prop.getValue()));
                    JSONObject json = (JSONObject)new JSONParser().parse(jsonString);

                    //Get the texture code from JSON.
                    json = (JSONObject)json.get("textures");
                    json = (JSONObject)json.get("SKIN");
                    skinUrl = (String)json.get("url");

                    //Get the last bit of the url to get the short code.
                    String[] split = skinUrl.split("/");
                    skinUrl = split[split.length - 1];
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException | ParseException e) {
            e.printStackTrace();
        }
        return skinUrl;
    }
}
