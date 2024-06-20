//import necessary classes
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Date;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//store in a file
import java.io.*;


//Declaration of Class
public class ToDoListApp {
    //Declare Instance Variables
    private JFrame frame;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskInputField;
    // HashMap to store tasks
    private HashMap<String, DefaultListModel<Task>> userTasks; //HASH MAP FOR TASKS
    // HashMap to store user and passcodes
    private HashMap<String, User> users; //HASH MAP FOR USERS
    private String currentUser;
    private Theme currentTheme;



    //Constructor
    public ToDoListApp() {
        //Initialize HashMaps for user tasks and users
        userTasks = new HashMap<>(); //Tasks of each user
        users = new HashMap<>();
    }

    //PERSONALISATION AND THEME SPECIFICATIONS -----------------------------------------------------------------------
    //METHOD 1: APPLY THEME
    //method to apply a theme to application
    public void applyTheme(Theme theme) {
        //set current theme
        currentTheme = theme;
        //set frame background
        frame.getContentPane().setBackground(theme.getBackgroundColor());
        //set background and text for task input field
        taskInputField.setBackground(theme.getBackgroundColor());
        taskInputField.setForeground(theme.getTextColor());
        taskInputField.setFont(theme.getFont());
        // set background, text for task list
        taskList.setBackground(theme.getBackgroundColor());
        taskList.setForeground(theme.getTextColor());
        taskList.setFont(theme.getFont());
        // Apply the theme to other UI components as needed
        SwingUtilities.updateComponentTreeUI(frame);
    }
    //CUSTOM PERSONALISATION __________
    //METHOD 2: THEME MENU
    // method to create theme menu
    private void createThemeMenu() {
        //MenuBar
        JMenuBar menuBar = new JMenuBar();
        JMenu themeMenu = new JMenu("Themes");

        JMenuItem lightThemeItem = new JMenuItem("Light Theme");
        lightThemeItem.addActionListener(e -> {
            applyTheme(ThemeManager.lightTheme);
            saveCurrentTheme("light");
        });

        JMenuItem darkThemeItem = new JMenuItem("Dark Theme");
        darkThemeItem.addActionListener(e -> {
            applyTheme(ThemeManager.darkTheme);
            saveCurrentTheme("dark");
        });

        JMenuItem blueThemeItem = new JMenuItem("Blue Theme");
        blueThemeItem.addActionListener(e -> {
            applyTheme(ThemeManager.blueTheme);
            saveCurrentTheme("blue");
        });

        themeMenu.add(lightThemeItem);
        themeMenu.add(darkThemeItem);
        themeMenu.add(blueThemeItem);

        menuBar.add(themeMenu);
        frame.setJMenuBar(menuBar);
    }
    //METHOD 3: save current theme, used in METHOD 2(apply theme)
    private void saveCurrentTheme(String themeName) {
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.saveTheme(themeName);
    }
    //METHOD 4: load apply theme
    private void loadAndApplyTheme() {
        UserPreferences userPreferences = new UserPreferences();
        String themeName = userPreferences.loadTheme();
        switch (themeName) {
            case "dark":
                applyTheme(ThemeManager.darkTheme);
                break;
            case "blue":
                applyTheme(ThemeManager.blueTheme);
                break;
            case "light":
            default:
                applyTheme(ThemeManager.lightTheme);
                break;
        }
    }

//END OF PERSONALISATION AND THEME PREFERENCES--------------------------------------------------------------------------

//LOGIN PAGE STARTS HERE------------------------------------------------------------------------------------------------
    //METHOD 5: create and show the login window
    public void createAndShowLogin() {

    //DEFAULT DISPLAY FRAME ______
        //new JFrame: login window
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500); //Default size of login page

        loadUserList();

        //new JPanel: login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //new JLabel: title
        JLabel titleLabel = new JLabel("Daily To-Dos For You");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 50)));


    //INTERACTIVE GUI_____
        //new JComboBox for selecting existing users
        JComboBox<String> userComboBox = new JComboBox<>(users.keySet().toArray(new String[0]));

        userComboBox.setPreferredSize(new Dimension(0,20));
        userComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        userComboBox.addActionListener(e -> showPasscodePrompt((String) userComboBox.getSelectedItem()));
        loginPanel.add(userComboBox);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20))); //space between the buttons


        //new JButton to add new account
        JButton newAccountButton = new JButton("Add New Account");
        newAccountButton.setPreferredSize(new Dimension(200, 50));
        newAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountButton.setFocusPainted(false);
        newAccountButton.setFont(new Font("Arial", Font.PLAIN, 14));
        newAccountButton.addActionListener(e -> showNewAccountPrompt());
        loginPanel.add(newAccountButton);

        frame.add(loginPanel);
        frame.setVisible(true);



        //Create JButton to remove existing account
        JButton removeAccountButton = new JButton("Remove Account");
        removeAccountButton.setPreferredSize(new Dimension(200, 50));
        removeAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeAccountButton.setFocusPainted(false);
        removeAccountButton.setFont(new Font("Arial", Font.PLAIN, 14));
        removeAccountButton.addActionListener(e -> {
            String selectedUser = (String) userComboBox.getSelectedItem();
            if (selectedUser!= null) {
                users.remove(selectedUser);
                userTasks.remove(selectedUser);
                //saveData();
                saveUserList();
                JOptionPane.showMessageDialog(frame, "User removed successfully");
                userComboBox.removeItem(selectedUser);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a user to remove");
            }
        });
        //remove account button to the login panel
        loginPanel.add(removeAccountButton);
    }

    //subclasses used in LOGIN STARTS HERE --------------------------------------------------------------------------

    //PASSCODE AUTHORISED ACCESS PROMPT __________
    //METHOD 6: PASSCODE PROMPT
    private void showPasscodePrompt(String username) {
        //new JPanel for passcode Panel
        JPanel passcodePanel = new JPanel();
        passcodePanel.setLayout(new BoxLayout(passcodePanel, BoxLayout.Y_AXIS));
        //new JLabel for passcode prompt
        JLabel passcodeLabel = new JLabel("Enter passcode for " + username + ":");
        passcodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passcodePanel.add(passcodeLabel);

        //Creation of JPasswordField: for entering password
        JPasswordField passcodeField = new JPasswordField(20);
        passcodeField.setMaximumSize(new Dimension(200, 30));
        passcodeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passcodePanel.add(passcodeField);

        // Showing Confirm Dialog for the Passcode Enter Corresponding, works by pressing enter
        int result = JOptionPane.showConfirmDialog(frame, passcodePanel,
                "Passcode", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String passcode = new String(passcodeField.getPassword()).trim(); //Setting the Passcode through String
            if (passcode.equals(users.get(username).getPasscode())) {
                currentUser = username;
                frame.dispose();
                createAndShowGUI();
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect passcode. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    //NEW ACCOUNT PROMPT_________
    //METHOD 7: SHOW NEW ACCOUNT PROMPT --
    private void showNewAccountPrompt() {

        //new JPanel for new Account Prompt / Signing In
        JPanel newAccountPanel = new JPanel();
        //Set layout
        newAccountPanel.setLayout(new BoxLayout(newAccountPanel, BoxLayout.Y_AXIS));


        //new JLabel: username prompt
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountPanel.add(usernameLabel);
        //new JTextField: enter username
        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(200, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountPanel.add(usernameField);

        //new JLabel: passcodeLabel
        JLabel passcodeLabel = new JLabel("Passcode:");
        passcodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountPanel.add(passcodeLabel);
        //new JPassword Field: enter password
        JPasswordField passcodeField = new JPasswordField(20);
        passcodeField.setMaximumSize(new Dimension(200, 30));
        passcodeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountPanel.add(passcodeField);

        //Show confirm dialog for new account
        int result = JOptionPane.showConfirmDialog(frame, newAccountPanel, "Create New Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        // Check whether user clicked OK
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            //Validate if all fields are filled and username is not repeated
            String passcode = new String(passcodeField.getPassword()).trim();
            if (!username.isEmpty() && !passcode.isEmpty()) {
                if (users.containsKey(username)) {
                    JOptionPane.showMessageDialog(frame, "Username already exists. Choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    currentUser = username; //set current user: new user
                    users.put(username, new User(username, passcode)); //Calls User Class
                    saveUserList();
                    frame.dispose();
                    //Create and ShowGUI for new user
                    createAndShowGUI();
                }
            } else {
                //Error message if any of the fields are empty
                JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    //METHOD 8: create and show To Do List
public void createAndShowGUI() {
    //new JFrame: for to-do list
    frame = new JFrame(" Your To-Do List");
        //frame settings
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 300);

    //Get task list model for current user, always defualt list model
        // as there is no saving methods on logout
    taskListModel = userTasks.getOrDefault(currentUser, new DefaultListModel<>());

    //new JList: task list, selection to single selection
    taskList = new JList<>(taskListModel);
    taskList.setCellRenderer(new TaskCellRenderer());
    taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    //new JtextField: entering new tasks
    taskInputField = new JTextField(20);
    taskInputField.addActionListener(e -> addTask());

    //BUTTONS:_____
        //corresponding JButtons for Add, Remove, Complete, and Logout
        //uses action listener to execute the methods.
    JButton addButton = new JButton("Add");
    addButton.addActionListener(e -> {
        addTask();}
    );
    JButton removeButton = new JButton("Remove");
    removeButton.addActionListener(e -> removeTask());

    JButton completeButton = new JButton("Complete");
    completeButton.addActionListener(e -> {
        completeTask();
    });

    JButton logoutButton = new JButton("Logout");
    logoutButton.addActionListener(e -> {
        saveUserList();
        frame.dispose();
        createAndShowLogin(); // back login page
    });

    //END OF BUTTONS_____


    //new JPanel: control Panel
    JPanel controlPanel = new JPanel();
        //add buttons to panel
    controlPanel.add(addButton);
    controlPanel.add(removeButton);
    controlPanel.add(completeButton);
    controlPanel.add(logoutButton);

    //new JPanel: input panel -- text field and button
    JPanel inputPanel = new JPanel();
    inputPanel.add(taskInputField);
    inputPanel.add(addButton);

    //setLayout of frame to border layout
    frame.setLayout(new BorderLayout());
    frame.add(new JScrollPane(taskList), BorderLayout.CENTER);
    frame.add(inputPanel, BorderLayout.NORTH);
    frame.add(controlPanel, BorderLayout.SOUTH);

    //window listener: exit the application on window close
    frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    });

    //CALL THE PERSONALISATION METHODS: 4 and 2
    loadAndApplyTheme();  // Apply the saved theme
    createThemeMenu();  // Add the theme selection menu
    frame.setVisible(true);
}

    //METHOD 9: add task
    private void addTask() {
        //get text from input field
        String taskText = taskInputField.getText().trim();
        //checks if text is not empty
        if (!taskText.isEmpty()) {
                //add task to task list model and clear input field
            taskListModel.addElement(new Task(taskText));
            taskInputField.setText("");

        }
    }
    //METHOD 10: remove task
    private void removeTask() {
        //get selected in index from task list
        int selectedIndex = taskList.getSelectedIndex();
        //if index is not -1
        if (selectedIndex != -1) {
                //remove task from task list model
            taskListModel.remove(selectedIndex);
        }
    }

    //METHOD 11: complete task
    private void completeTask() {
        //get selected index of task list
        int selectedIndex = taskList.getSelectedIndex();
        //if index not -1
        if (selectedIndex != -1) {
            //get task from task list model
            Task task = taskListModel.getElementAt(selectedIndex);
            //set completed status to true, and set completion time
            task.setCompleted(true);
            task.setCompletionTime(System.currentTimeMillis());
            //remove task from task list model, move to completedTaskListModel
            taskListModel.remove(selectedIndex);
            taskListModel.add(0, task); // Move the completed task to the top
        }
    }

    //METHOD 12: loadUserList from a file
    private void loadUserList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.ser"))) {
            //reads user list from a file: users.ser
            users = (HashMap<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //EXCEPTION HANDLING: print error message
            System.err.println("Error loading user list: " + e.getMessage());
        }
    }

    //METHOD 13: SaveUserList to a file
    private void saveUserList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
            //Write user list to the file
            oos.writeObject(users);
        } catch (IOException e) {
            //EXCEPTION HANDLING: error message
            System.err.println("Error saving user list: " + e.getMessage());
        }
    }

    //METHOD MAIN: start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoListApp().createAndShowLogin());
    } //invoke the login page -- necessary for swing


    //Inner Class: render tasks in the JList
    class TaskCellRenderer extends DefaultListCellRenderer { //INHERITANCE of Superclass TaskCellRenderer
        @Override //POLYMORPHISM: customise rendering of class
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            //Call superclass method to get default component
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            //if value is an instance of task:
            if (value instanceof Task) {
                //cast value to a Task
                Task task = (Task) value;
                    //if class is completed
                if (task.isCompleted()) {
                        //Create a string: Task Description, Completion Time >> STRIKE THROUGH
                    String description = "<html><strike>" + task.getDescription() + "</strike> (Completed: " + new Date(task.getCompletionTime()) + ")</html>";
                        //Set the text of the component to the description to display
                    setText(description);
                        //set foreground to gray
                    component.setForeground(Color.GRAY);
                } else {
                    //Set text of component to the task description
                    setText(task.getDescription());
                    //set foreground: as theme specifies
                    component.setForeground(currentTheme.getTextColor());
                }
            }
            return component; //return the component from the if else (is completed)
        }
    }
    }





