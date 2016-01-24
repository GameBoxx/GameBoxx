package info.gameboxx.gameboxx.components;

import info.gameboxx.gameboxx.game.GameComponent;
import info.gameboxx.gameboxx.util.SoundEffect;
import info.gameboxx.gameboxx.util.Utils;

/**
 * Adding this component will add an countdown before the game starts.
 */
public class CountdownGC extends GameComponent {

    private int countdown = 30;

    private int seconds;
    private int mainInterval;
    private int startSecondInterval;
    private SoundEffect sound;
    private String message;

    /**
     * Creates a new countdown component.
     * @param parent The parent {@link GameComponent}.
     * @param seconds The amount of seconds to count down from.
     * @param mainInterval The interval to send a message and play a sound. (Recommended at 10)
     * @param startSecondInterval At which time the second countdown starts.
     * @param sound The {@link SoundEffect} to play when the countdown triggers. (may be null for no sound)
     * @param message The message to broadcast when the countdown triggers.
     *                Use the {seconds} placeholder in the message for displaying the time!
     */
    public CountdownGC(GameComponent parent, int seconds, int mainInterval, int startSecondInterval, SoundEffect sound, String message) {
        super(parent);
        this.seconds = seconds;
        this.countdown = seconds;
        this.mainInterval = mainInterval;
        this.startSecondInterval = startSecondInterval;
        this.sound = sound;
        this.message = message;
    }

    /**
     * Creates a new countdown component with the default settings.
     * @param parent The parent {@link GameComponent}.
     */
    public CountdownGC(GameComponent parent) {
        super(parent);
        //TODO: Get defaults from config. (Not like below that was just for testing) There should be a separate config for defaults.
        /**
        this.seconds = getAPI().getCfg().arena__defaults__countdown__time;
        this.countdown = seconds;
        this.mainInterval = getAPI().getCfg().arena__defaults__countdown__interval;
        this.startSecondInterval = getAPI().getCfg().arena__defaults__countdown__startSecondInterval;
        this.sound = new SoundEffect(Sound.valueOf(getAPI().getCfg().arena__defaults__countdown__sound),
                (float)getAPI().getCfg().arena__defaults__countdown__soundVolume, (float)getAPI().getCfg().arena__defaults__countdown__soundPitch);
        this.message = GameMsg.COUNTDOWN.getMsg();
         */
    }

    public void count(int seconds) {
        sound.play(Utils.getPlayerList(getParent().getComponent(PlayersCP.class).getPlayers()));
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

    /** @see GameComponent#deepCopy() */
    @Override
    public CountdownGC deepCopy() {
        CountdownGC clone = new CountdownGC(getParent(), seconds, mainInterval, startSecondInterval, sound, message);
        copyChildComponents(this, clone);
        return clone;
    }
}
