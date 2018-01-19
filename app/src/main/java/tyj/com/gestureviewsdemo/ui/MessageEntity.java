package tyj.com.gestureviewsdemo.ui;

/**
 * @author ChenYe
 */

public class MessageEntity {
    public int what;
    public Object obj;

    private MessageEntity() {

    }

    private static class Holder {
        private static final MessageEntity INSTANCE = new MessageEntity();
    }

    public static MessageEntity obtianMessage() {
        return Holder.INSTANCE;
    }
}
