//Declares the Theme and will utilise the ThemeManager class in the main ToDoListApp class
// Import the necessary classes:
import java.awt.Color; // encapsulates colors using the RGB (Red, Green, Blue) color model
import java.awt.Font; //  encapsulates a set of fonts: to render text on the screen

// Declare the Theme class: represents a personalized theme for user
public class Theme {

    // Private fields to store the theme's properties
    private Color backgroundColor; // The background color of the theme
    private Color textColor; // The text color of the theme
    private Font font; // The font used in the theme

    // Constructor to initialize a new Theme object with the given properties
    public Theme(Color backgroundColor, Color textColor, Font font) {
        // Assign the given properties to the private fields
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.font = font;
    }

    // Getter method to retrieve the background color
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    // This method allows other classes to access the background color of the theme

    // Getter method to retrieve the text color
    public Color getTextColor() {
        return textColor;
    }
    // This method allows other classes to access the text color of the theme

    // Getter method to retrieve the font
    public Font getFont() {
        return font;
    }
    // This method allows other classes to access the font used in the theme
}