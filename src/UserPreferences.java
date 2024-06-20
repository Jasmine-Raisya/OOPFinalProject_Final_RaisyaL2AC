//importing of necessary class
import java.util.prefs.Preferences;
//Declaration of the UserPreferences class: manage user Preferences
public class UserPreferences {
    //Private field: store user preference
    private Preferences prefs;
    //Static Constant: store key for theme preference
    private static final String THEME_KEY = "theme";

    //Constructor: initialize user preference for the current user
    public UserPreferences() {
        //user node package for this class
        prefs = Preferences.userNodeForPackage(UserPreferences.class);
    }
    //Method: save chosen theme
    public void saveTheme(String themeName) {
        prefs.put(THEME_KEY, themeName);
    }
    //Method: loadTheme of the users chosen theme
    public String loadTheme() {
        return prefs.get(THEME_KEY, "light"); // Default to light theme
    }
}
