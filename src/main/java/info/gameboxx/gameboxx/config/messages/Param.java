package info.gameboxx.gameboxx.config.messages;

public class Param {

    private String param;
    private Object value;

    public Param(String param, Object value) {
        this.param = param;
        this.value = value;
    }

    public String getParam() {
        return param;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }
        return value.toString();
    }

    public static Param P(String param, Object value) {
        return new Param(param, value);
    }
}
