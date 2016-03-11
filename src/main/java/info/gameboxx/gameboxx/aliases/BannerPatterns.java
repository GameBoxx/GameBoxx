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

package info.gameboxx.gameboxx.aliases;

import info.gameboxx.gameboxx.aliases.internal.AliasMap;
import org.bukkit.block.banner.PatternType;

import java.io.File;
import java.util.List;
import java.util.Map;

public class BannerPatterns extends AliasMap<PatternType> {

    private BannerPatterns() {
        super("BannerPatterns", new File(ALIASES_FOLDER, "BannerPatterns.yml"), "bannerpattern", "patterns", "pattern", "patterntypes", "patterntype");
    }

    @Override
    public void onLoad() {
        add(PatternType.BASE, "Base", "b", "None");
        add(PatternType.SQUARE_BOTTOM_LEFT, "Bottom Left Corner", "bl", "Bottom Left Square", "blc", "BLCorner", "BLSquare", "v<");
        add(PatternType.SQUARE_BOTTOM_RIGHT, "Bottom Right Corner", "br", "Bottom Right Square", "BRCorner", "BRSquare", "v>");
        add(PatternType.SQUARE_TOP_LEFT, "Top Left Corner", "tl", "top Left Square", "tlc", "TLCorner", "TLSquare", "^<");
        add(PatternType.SQUARE_TOP_RIGHT, "Top Right Corner", "tr", "Top right Square", "trc", "TRCorner", "TRSquare", "^>");
        add(PatternType.STRIPE_BOTTOM, "Bottom Stripe", "bs", "BStripe", "_");
        add(PatternType.STRIPE_TOP, "Top Stripe", "ts", "TStripe", "^_");
        add(PatternType.STRIPE_LEFT, "Left Stripe", "ls", "LStripe", "[");
        add(PatternType.STRIPE_RIGHT, "Right Stripe", "rs", "RStripe", "]");
        add(PatternType.STRIPE_CENTER, "Center Stripe", "cs", "CStripe", "Vertical Stripe", "VStripe", "|");
        add(PatternType.STRIPE_MIDDLE, "Middle Stripe", "ms", "MStripe", "Horizontal Stripe", "HStripe", "-");
        add(PatternType.STRIPE_DOWNRIGHT, "Down Right Stripe", "drs", "DRStripe", "Up Left Stripe", "ULStripe", "Backward Slash", "BSlash", "\\");
        add(PatternType.STRIPE_DOWNLEFT, "Down Left Stripe", "dls", "DLSTripe", "Up Right Stripe", "URStripe", "Slash", "Forward Slash", "FSlash", "/");
        add(PatternType.STRIPE_SMALL, "Small Stripes", "ss", "Stripes", "Vertical Stripes", "SStripes", "VStripes", "||");
        add(PatternType.CROSS, "Diagonal Cross", "cr", "Cross", "DCross", "X");
        add(PatternType.STRAIGHT_CROSS, "Square Cross", "sc", "Plus", "Add", "SCross", "Straight Cross", "SCross", "+");
        add(PatternType.TRIANGLE_BOTTOM, "Bottom Triangle", "bt", "BTriangle", "^");
        add(PatternType.TRIANGLE_TOP, "Top Triangle", "tt", "TTriangle", "V");
        add(PatternType.TRIANGLES_BOTTOM, "Bottom Triangle Sawtooth", "bts", "Bottom Triangles", "BTriangle Sawtooth", "BTriangle", "BTriangles", "M");
        add(PatternType.TRIANGLES_TOP, "Top Triangle Sawtooth", "tts", "Top Triangles", "TTriangle Sawtooth", "TTriangle", "TTriangles", "W");
        add(PatternType.DIAGONAL_LEFT, "Left Diagonal", "ld", "LDiagonal", "|/");
        add(PatternType.DIAGONAL_RIGHT, "Right Diagonal", "rd", "RDiagonal", "/|");
        add(PatternType.DIAGONAL_LEFT_MIRROR, "Left Upside-down Diagonal", "lud", "ULDiagonal", "MLDiagonal", "LUDiagonal", "LMDiagonal", "|\\");
        add(PatternType.DIAGONAL_RIGHT_MIRROR, "Right Upside-down Diagonal", "rud", "URDiagonal", "MRDiagonal", "RUDiagonal", "RMDiagonal", "\\|");
        add(PatternType.CIRCLE_MIDDLE, "Middle Circle", "mc", "Circle", "Round", "MCircle", "O", "0");
        add(PatternType.RHOMBUS_MIDDLE, "Middle Rhombus", "mr", "Rhombus", "MRhombus", "<>");
        add(PatternType.HALF_VERTICAL, "Vertical Half Left", "vh", "VHalf Left", "VHalfL", "Vertical Half", "VHalf", "#|");
        add(PatternType.HALF_HORIZONTAL, "Horizontal Half Top", "hh", "HHalf Top", "HHalfT", "Horizontal Half", "HHalf", "#^");
        add(PatternType.HALF_VERTICAL_MIRROR, "Vertical Half Right", "vhr", "VHalf Right", "VHalfR", "Vertical Half Inverted", "VHalf Inverted", "VHalfI", "|#");
        add(PatternType.HALF_HORIZONTAL_MIRROR, "Horizontal Half Bottom", "hhb", "HHalf Bottom", "HHalfB", "Horizontal Half Inverted", "HHalf Inverted", "HHalfI", "#v");
        add(PatternType.BORDER, "Border", "bo", "[]");
        add(PatternType.CURLY_BORDER, "Curly Border", "cbo", "CBorder", "{}");
        add(PatternType.CREEPER, "Creeper", "cre", ":[");
        add(PatternType.GRADIENT, "Gradient Down", "gra", "Grad Down", "GDown", "GradientD", "GradD", "Gradient", "Grad", "=v");
        add(PatternType.GRADIENT_UP, "Gradient Up", "gru", "Grad Up", "GUp", "GradientU", "GradU", "=^");
        add(PatternType.BRICKS, "Brick", "bri", "Bricks", "#");
        add(PatternType.SKULL, "Skull", "sku", "Wither", "Wither Skull", "WSkull", "#X");
        add(PatternType.FLOWER, "Flower", "flo", "Plant", "*");
        add(PatternType.MOJANG, "Mojang", "moj", "Logo", "@");
    }

    public static PatternType get(String string) {
        return instance()._get(string);
    }

    public static String getName(PatternType key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(PatternType key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(PatternType key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static BannerPatterns instance() {
        if (getMap(BannerPatterns.class) == null) {
            aliasMaps.put(BannerPatterns.class, new BannerPatterns());
        }
        return (BannerPatterns)getMap(BannerPatterns.class);
    }
}
