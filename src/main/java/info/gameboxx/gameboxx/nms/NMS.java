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

package info.gameboxx.gameboxx.nms;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.nms.worldloader.WorldLoader_v1_8_R3;
import info.gameboxx.gameboxx.nms.worldloader.WorldLoader;
import org.bukkit.Bukkit;

public class NMS {

    private static NMS instance = null;
    private String versionString = "unknown";
    private NMSVersion version;

    private WorldLoader worldLoader;

    private NMS() {
        try {
            versionString = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException e) {}
        version = NMSVersion.fromString(versionString);

        GameBoxx gb = GameBoxx.get();
        if (version == NMSVersion.V1_8_R3) {
            worldLoader = new WorldLoader_v1_8_R3(gb);
        }
    }

    public WorldLoader getWorldLoader() {
        return worldLoader;
    }



    public NMSVersion getVersion() {
        return version;
    }

    public String getVersionString() {
        return versionString;
    }

    public boolean isCompatible() {
        return version != null;
    }

    public static NMS get() {
        if(instance == null) {
            instance = new NMS();
        }
        return instance;
    }

}
