package app;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.ExamSession;
import model.Question;
import model.User;
import ui.Theme;
import ui.screens.ExamScreen;
import ui.screens.LoginScreen;
import ui.screens.ProfileScreen;
import ui.screens.ResultScreen;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    // Active screens
    private LoginScreen loginScreen;
    private ProfileScreen profileScreen;
    private ExamScreen examScreen;
    private ResultScreen resultScreen;

    // Databases
    private Map<String, User> userDatabase;
    private List<Question> questionDatabase;

    // State tracking
    private String currentCard = "login";
    private ExamSession activeSession = null;

    public MainFrame() {
        super("Online Examination System");
        initWindow();
        initDatabases();
        initScreens();
        
        // Show login screen initially
        showLogin();
    }

    private void initWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(950, 700);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null); // Center on screen
        setBackground(Theme.BG_DARK);

        // Session management confirmation dialog on window close during exam
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });
    }

    private void initDatabases() {
        // Pre-configure dummy users
        userDatabase = new HashMap<>();
        userDatabase.put("student", new User("student", "password", "Alex Mercer"));
        userDatabase.put("admin", new User("admin", "admin", "System Administrator"));

        // Initialize exam questions
        questionDatabase = new ArrayList<>();
        questionDatabase.add(new Question(
            1, 
            "What is the time complexity of searching in a balanced Binary Search Tree (BST)?", 
            new String[]{"O(1)", "O(log n)", "O(n)", "O(n log n)"}, 
            1
        ));
        questionDatabase.add(new Question(
            2, 
            "Which of the following is NOT a primitive data type in Java?", 
            new String[]{"int", "boolean", "String", "char"}, 
            2
        ));
        questionDatabase.add(new Question(
            3, 
            "What does HTML stand for?", 
            new String[]{
                "Hyper Text Markup Language", 
                "High Tech Markup Language", 
                "Hyper Tabular Markup Language", 
                "Home Tool Markup Language"
            }, 
            0
        ));
        questionDatabase.add(new Question(
            4, 
            "Which HTTP status code represents 'Internal Server Error'?", 
            new String[]{"400 Bad Request", "401 Unauthorized", "404 Not Found", "500 Internal Server Error"}, 
            3
        ));
        questionDatabase.add(new Question(
            5, 
            "In relational databases, what does ACID stand for?", 
            new String[]{
                "Atomicity, Consistency, Isolation, Durability", 
                "Access, Control, Integrity, Distribution", 
                "Algorithms, Coding, Integration, Deployment", 
                "Advanced, Computer, Information, Database"
            }, 
            0
        ));
        questionDatabase.add(new Question(
            6, 
            "Which programming language is officially recommended for modern Android development?", 
            new String[]{"Java", "Kotlin", "Swift", "C#"}, 
            1
        ));
        questionDatabase.add(new Question(
            7, 
            "What is the main purpose of DNS (Domain Name System)?", 
            new String[]{
                "To encrypt web browser traffic", 
                "To map human-readable domain names to numerical IP addresses", 
                "To speed up database search queries", 
                "To detect and quarantine virus packages"
            }, 
            1
        ));
        questionDatabase.add(new Question(
            8, 
            "Which data structure operates on a Last-In, First-Out (LIFO) model?", 
            new String[]{"Queue", "Stack", "Min Heap", "Circular Buffer"}, 
            1
        ));
        questionDatabase.add(new Question(
            9, 
            "What does JSON stand for?", 
            new String[]{
                "Java System Object Notation", 
                "JavaScript Object Notation", 
                "Joint Standard Object Network", 
                "JavaScript Oriented Node"
            }, 
            1
        ));
        questionDatabase.add(new Question(
            10, 
            "Which keyword is used to prevent inheritance of a class or overriding of a method in Java?", 
            new String[]{"static", "abstract", "final", "private"}, 
            2
        ));
    }

    private void initScreens() {
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(Theme.BG_DARK);

        loginScreen = new LoginScreen(this);
        profileScreen = new ProfileScreen(this);
        examScreen = new ExamScreen(this);
        resultScreen = new ResultScreen(this);

        mainContentPanel.add(loginScreen, "login");
        mainContentPanel.add(profileScreen, "profile");
        mainContentPanel.add(examScreen, "exam");
        mainContentPanel.add(resultScreen, "result");

        add(mainContentPanel);
    }

    // Navigation and Flow API
    public void showLogin() {
        currentCard = "login";
        activeSession = null;
        cardLayout.show(mainContentPanel, "login");
    }

    public void showProfile(User user) {
        currentCard = "profile";
        profileScreen.setUser(user);
        cardLayout.show(mainContentPanel, "profile");
    }

    public void startExam(User user) {
        currentCard = "exam";
        // Default duration: 30 minutes
        activeSession = new ExamSession(user, questionDatabase, 30);
        examScreen.startSession(activeSession);
        cardLayout.show(mainContentPanel, "exam");
    }

    public void showResults(ExamSession session) {
        currentCard = "result";
        activeSession = null; // No longer active
        resultScreen.setSession(session);
        cardLayout.show(mainContentPanel, "result");
    }

    public void logout() {
        showLogin();
    }

    // Authentication API
    public User authenticate(String username, String password) {
        User user = userDatabase.get(username.toLowerCase());
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean userExists(String username) {
        return userDatabase.containsKey(username.toLowerCase());
    }

    public void registerUser(User user) {
        userDatabase.put(user.getUsername().toLowerCase(), user);
    }

    private void handleWindowClose() {
        if ("exam".equals(currentCard)) {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "An exam is currently in progress! Are you sure you want to quit?\nYour progress will be lost.",
                "Quit Active Exam?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                if (examScreen != null) {
                    examScreen.stopTimer();
                }
                dispose();
                System.exit(0);
            }
        } else {
            dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Run application on Event Dispatch Thread (EDT) for Swing safety
        SwingUtilities.invokeLater(() -> {
            // Set Nimbus Look and Feel to make it look nicer out of the box
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                // Fallback to default L&F if Nimbus is unavailable
            }
            new MainFrame().setVisible(true);
        });
    }
}
