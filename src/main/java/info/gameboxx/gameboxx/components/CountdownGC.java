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

import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.game.GameComponent;
import info.gameboxx.gameboxx.game.GameSession;
import info.gameboxx.gameboxx.util.SoundEffect;
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.entity.Player;

/**
 * Adding this component will add an countdown before the game starts.
 */
//TODO: Method to start/stop/reset the countdown.
public class CountdownGC extends GameComponent {

    private int countdown = 30;

    private int seconds;
    private int mainInterval;
    private int startSecondInterval;
    private SoundEffect sound;
    private String message;

    /**
     * @see GameComponent
     * @param seconds The amount of seconds to count down from.
     * @param mainInterval The interval to send a message and play a sound. (Recommended at 10)
     * @param startSecondInterval At which time the second countdown starts.
     * @param sound The {@link SoundEffect} to play when the countdown triggers. (may be null for no sound)
     * @param message The message to broadcast when the countdown triggers.
     *                Use the {seconds} placeholder in the message for displaying the time!
     */
    public CountdownGC(Game game, int seconds, int mainInterval, int startSecondInterval, SoundEffect sound, String message) {
        super(game);
        addDependency(PlayersCP.class);

        this.seconds = seconds;
        this.countdown = seconds;
        this.mainInterval = mainInterval;
        this.startSecondInterval = startSecondInterval;
        this.sound = sound;
        this.message = message;
    }

    @Override
    public CountdownGC newInstance(GameSession session) {
        return (CountdownGC) new CountdownGC(getGame(), seconds, mainInterval, startSecondInterval, sound, message).setSession(session);
    }

    public void count() {
        if (countdown <= 0) {
            countdown = 0;
            //TODO: Start the session.
            return;
        }
        if (countdown % mainInterval == 0 || countdown <= startSecondInterval) {
            sound.play(getDependency(PlayersCP.class).getOnlinePlayers());
            //TODO: Have a message component or put this somewhere else.
            for (Player player : getDependency(PlayersCP.class).getOnlinePlayers()) {
                player.sendMessage(Str.color(message));
            }
        }
        countdown--;
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
     * @return Seconds.
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Set the amount in seconds to start the countdown from.
     * @param seconds The seconds to start the countdown from.
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Get the main interval between counts.
     * @return The interval between counts in seconds.
     */
    public int getMainInterval() {
        return mainInterval;
    }

    /**
     * Set the main interval between counts.
     * It will be used like: (time % interval == 0)
     * The higher it is the less frequent it will count.
     * @param mainInterval The interval between counts in seconds.
     */
    public void setMainInterval(int mainInterval) {
        this.mainInterval = mainInterval;
    }

    /**
     * Get the start time in seconds when to start the second interval.
     * @return The start time for the second interval.
     */
    public int getStartSecondInterval() {
        return startSecondInterval;
    }

    /**
     * Set the start time in seconds when to start the second interval.
     * For example if you set it to 5 it would count like 30, 20, 10, 5, 4, 3, 2, 1...
     * @param startSecondInterval The start time for the second interval.
     */
    public void setStartSecondInterval(int startSecondInterval) {
        this.startSecondInterval = startSecondInterval;
    }

    /**
     * Get the {@link SoundEffect} to play on each count.
     * @return The {@link SoundEffect} to play on each count. May be null if there is no sound to play!
     */
    public SoundEffect getSound() {
        return sound;
    }

    /**
     * Set the {@link SoundEffect} to play on each count.
     * @param sound The {@link SoundEffect} to play on each count. Set to null to have no sound play.
     */
    public void setSound(SoundEffect sound) {
        this.sound = sound;
    }

    /**
     * Get the message that will be broadcasted on each count.
     * Before displaying the message replace {seconds} with the remaining seconds on the countdown.
     * @return The message that will be broadcasted.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message that will be broadcasted on each count.
     * Use the {seconds} placeholder in the message for displaying the time!
     * @param message The message that will be broadcasted.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
