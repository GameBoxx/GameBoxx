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

package info.gameboxx.gameboxx.commands.test;

import info.gameboxx.gameboxx.commands.api.BaseCmd;
import info.gameboxx.gameboxx.commands.api.CmdData;
import info.gameboxx.gameboxx.commands.api.data.ArgRequirement;
import info.gameboxx.gameboxx.options.single.LocationO;
import info.gameboxx.gameboxx.options.single.ParticleO;
import info.gameboxx.gameboxx.options.single.PlayerO;
import info.gameboxx.gameboxx.util.ParticleEffect;
import org.bukkit.Location;

public class ParticleCmd extends BaseCmd {
    public ParticleCmd() {
        super("particle", "par", "pa");

        addArgument("location", ArgRequirement.REQUIRED, new LocationO());
        addArgument("particle", ArgRequirement.REQUIRED, new ParticleO());
        addArgument("player", ArgRequirement.OPTIONAL, new PlayerO());


    }

    @Override
    public void onCommand(CmdData data) {
        ParticleEffect particle = (ParticleEffect)data.getArg("particle");
        Location loc = (Location)data.getArg("location");
        if (data.hasArg("player")) {
            particle.displayPlayer(data.getPlayer("player"), loc);
        } else {
            particle.displayWorld(loc);
        }
    }

}
