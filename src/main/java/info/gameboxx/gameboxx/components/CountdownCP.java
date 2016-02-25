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

package info.gameboxx.gameboxx.components;

import info.gameboxx.gameboxx.components.internal.GameComponent;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.game.GameSession;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.options.single.IntOption;
import info.gameboxx.gameboxx.util.SoundEffect;
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Adding this component will add an countdown before the game starts.
 */
//TODO: Method to start/stop/reset the countdown.
public class CountdownCP extends GameComponent {
    
    public static final long TICKS_IN_SECOND = 20L;

    private int countdown = 30;
    private CountdownRunnable runnable;

    public CountdownCP(Game game) {
        super(game);
        addDependency(PlayersCP.class);

        this.runnable = new CountdownRunnable();
    }

    @Override
    public void registerOptions() {
        registerGameOption("countdown-time", new IntOption("CountdownTime", 30).min(0).setDescription(Msg.getRaw("opt.countdown-time").getRaw()));
        registerGameOption("count-interval", new IntOption("CountInterval", 10).min(1).setDescription(Msg.getRaw("opt.count-interval").getRaw()));
        registerGameOption("count-seconds", new IntOption("CountSeconds", 5).min(0).setDescription(Msg.getRaw("opt.count-seconds").getRaw()));
        //registerGameOption("count-sound", new SoundOption("CountSound", new SoundEffect(Sound.NOTE_PLING)).setDescription(Msg.getRaw("opt.count-sound").getRaw()));;
    }

    @Override
    public CountdownCP newInstance(GameSession session) {
        return (CountdownCP) new CountdownCP(getGame()).setSession(session);
    }


    /**
     * Get the remaining countdown time in seconds.
     * @return The remaining time on the countdown in seconds.
     */
    public int getCountdown() {
        return countdown;
    }

    /**
     * Force override the countdown time.
     * There is no need to manually decrease the countdown time unless you want to force decrease it.
     * @param countdown The countdown time in seconds to set.
     */
    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    /**
     * Get the amount in seconds to start the countdown from.
     * @return countdown-time option value.
     */
    public int getCountdownTime() {
        return getGameOptions().getInt(path("countdown-time"));
    }

    /**
     * Get the main interval between counts.
     * @return count-interval option value.
     */
    public int getCountInterval() {
        return getGameOptions().getInt(path("count-interval"));
    }

    /**
     * Get the start time in seconds when to start the second interval.
     * @return count-seconds option value.
     */
    public int getCountSeconds() {
        return getGameOptions().getInt(path("count-seconds"));
    }

    /**
     * Get the {@link SoundEffect} to play on each count.
     * @return count-sound option value.
     */
    public SoundEffect getSound() {
        //TODO: Implement when SoundOption is added.
        return new SoundEffect(Sound.NOTE_PLING);
    }

    /**
     * Get the message that will be broadcasted on each count.
     * @return The message that will be broadcasted.
     */
    public String getMessage() {
        //TODO: Get configurable message.
        return "".replace("{game}", getGame().getName()).replace("{seconds}", String.valueOf(getCountdown())).replace("{s}", getCountdown() == 1 ? "" : "s");
    }

    
    /**
     * Starts the countdown associated with this class.
     */
    public void startCountdown() {
        runnable.runTaskTimer(getAPI(), 0L, TICKS_IN_SECOND);
    }
    
    /**
     * Stops the countdown.
     */
    public void stopCountdown() {
        runnable.cancel();
    }
    
    /**
     * Resets the countdown.
     */
    public void resetCountdown() {
        stopCountdown();
        this.countdown = 30;
        startCountdown();
    }
    
    /**
     * Resumes the countdown.
     */
    public void resumeCountdown() {
        startCountdown();
    }


    public void count() {
        if (countdown <= 0) {
            countdown = 0;
            runnable.cancel();
            // TODO: Start the session.
            return;
        }
        if (countdown % getCountInterval() == 0 || countdown <= getCountSeconds()) {
            SoundEffect sound = getSound();
            if (sound != null) {
                sound.play(getDependency(PlayersCP.class).getOnlinePlayers());
            }
            //TODO: Have a message component or put this somewhere else.
            for (Player player : getDependency(PlayersCP.class).getOnlinePlayers()) {
                player.sendMessage(Str.color(getMessage()));
            }
        }
        countdown--;
    }
    
    private class CountdownRunnable extends BukkitRunnable {
        @Override
        public void run() {
            count();
        }
    }
}
