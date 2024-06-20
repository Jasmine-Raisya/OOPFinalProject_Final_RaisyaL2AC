//import of necessary classes, this repeats the same
// from the Theme class
import java.awt.Color;
import java.awt.Font;

//Declaration of ThemeManager Class
// Manage different themes available
public class ThemeManager {

    //Declaration of Static Themes: light, dark, blue.
    //  initialized with properties: background color, text color, font)
    public static Theme lightTheme = new Theme(Color.WHITE, Color.BLACK, new Font("Arial", Font.PLAIN, 14));
    public static Theme darkTheme = new Theme(Color.DARK_GRAY, Color.WHITE, new Font("Arial", Font.PLAIN, 14));
    public static Theme blueTheme = new Theme(Color.BLUE, Color.WHITE, new Font("Arial", Font.PLAIN, 14));

}
