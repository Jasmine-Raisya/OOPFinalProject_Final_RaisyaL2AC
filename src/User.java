//importing the interface
import java.io.Serializable;

// the Serializable interface: allows objects of this class to be serialized and deserialized
//      for storing or transmitting the user data.
public class User implements Serializable {
    // Private fields: store the username and passcode
    private String username;
    private String passcode;

    // Constructor: initialize a new User object with a given username and passcode
    public User(String username, String passcode) {
        // Assign usernames and passcodes to the private fields
        this.username = username;
        this.passcode = passcode;
    }

    // Getter method: retrieves the username
    public String getUsername() {
        return username;
    }

    // Getter method: retrieve the passcode
    public String getPasscode() {
        return passcode;
    }
}