import java.io.DataOutputStream;
import java.io.IOException;

public class Messages {
    byte type;
    Point point;
    String username;
    boolean ready = false;
    boolean playerOneMessage = true;

    public Messages(Point point) {
        this.type = 10;
        this.point = point;
    }

    public Messages (boolean ready){
        this.type = 3;
        this.ready = ready;
    }

    public Messages (String username){
        this.username = username;
        this.type = 0;
    }

    public Messages (int type){
        this.type = (byte)type;
    }

    public void sendMessage (DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(type);
        if (type == 10){
            dataOutputStream.writeInt(point.row);
            dataOutputStream.writeInt(point.col);
        } else if (type == 0){
            dataOutputStream.writeUTF(username);
        } else {
            dataOutputStream.writeByte(type);
        }
        dataOutputStream.flush();

    }
}

