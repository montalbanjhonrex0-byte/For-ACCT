import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class Dashboard extends JFrame {

    // COLOR PALETTE
    Color green = new Color(22, 130, 85);
    Color darkGreen = new Color(15, 95, 62);
    Color lightGreen = new Color(230, 245, 237);
    Color white = Color.WHITE;
    Color gray = new Color(110, 120, 115);

    public Dashboard() {

        setTitle("Journal Voucher System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(white);

        // =========================
        // SIDEBAR
        // =========================

        JPanel sidebar = new JPanel();
        sidebar.setBackground(green);
        sidebar.setPreferredSize(new Dimension(230, 700));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(
            BorderFactory.createEmptyBorder(25, 15, 20, 15)
        );

        JLabel logo = new JLabel("JOURNAL SYSTEM");
        logo.setFont(new Font("Arial", Font.BOLD, 19));
        logo.setForeground(white);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(45));

        JButton dashboardBtn = createButton("Dashboard", true);
        JButton createBtn = createButton("Create Voucher", false);
        JButton recordsBtn = createButton("Records", false);
        JButton logoutBtn = createButton("Logout", false);

        sidebar.add(dashboardBtn);
        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createBtn);
        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(recordsBtn);

        sidebar.add(Box.createVerticalGlue());

        sidebar.add(logoutBtn);

        // =========================
        // CONTENT
        // =========================

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(white);
        content.setBorder(
            BorderFactory.createEmptyBorder(35, 35, 30, 35)
        );

        // WELCOME SECTION
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(white);
        welcomePanel.setLayout(new BoxLayout(
            welcomePanel, BoxLayout.Y_AXIS
        ));

        JLabel welcome = new JLabel("Welcome to Journal Voucher System");
        welcome.setFont(new Font("Arial", Font.BOLD, 27));
        welcome.setForeground(darkGreen);

        JLabel subtitle = new JLabel(
            "Manage your journal entries and financial transactions."
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(gray);

        welcomePanel.add(welcome);
        welcomePanel.add(Box.createVerticalStrut(8));
        welcomePanel.add(subtitle);

        content.add(welcomePanel, BorderLayout.NORTH);

        
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(white);
        centerPanel.setLayout(new BoxLayout(
            centerPanel, BoxLayout.Y_AXIS
        ));

        centerPanel.add(Box.createVerticalStrut(35));

        JPanel cardsPanel = new JPanel(
            new GridLayout(1, 2, 20, 0)
        );
        cardsPanel.setBackground(white);
        cardsPanel.setMaximumSize(new Dimension(1000, 155));
        cardsPanel.setPreferredSize(new Dimension(1000, 155));

        cardsPanel.add(createCard("TOTAL DEBIT", "₱0.00"));
        cardsPanel.add(createCard("TOTAL CREDIT", "₱0.00"));

        centerPanel.add(cardsPanel);

        centerPanel.add(Box.createVerticalStrut(35));

        
        JLabel recentTitle = new JLabel("Recent Transactions");
        recentTitle.setFont(new Font("Arial", Font.BOLD, 20));
        recentTitle.setForeground(darkGreen);

        centerPanel.add(recentTitle);
        centerPanel.add(Box.createVerticalStrut(15));

        JLabel emptyMessage = new JLabel(
            "No transactions available yet."
        );
        emptyMessage.setForeground(gray);
        emptyMessage.setFont(new Font("Arial", Font.PLAIN, 14));

        centerPanel.add(emptyMessage);

        content.add(centerPanel, BorderLayout.CENTER);

        

        createBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                this, "Create Voucher page coming next!"
            );
        });

        recordsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                this, "Transaction Records page coming next!"
            );
        });

        dashboardBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                this, "You are already on Dashboard."
            );
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(
                null, "You have logged out."
            );
        });

        // ASSEMBLE WINDOW

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(content, BorderLayout.CENTER);

        add(mainPanel);

        setVisible(true);
    }

    // =========================
    // CUSTOM BUTTON
    // =========================

    private JButton createButton(String text, boolean selected) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 14));

        button.setForeground(white);

        button.setBackground(
            selected ? darkGreen : green
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setBorder(
            BorderFactory.createEmptyBorder(14, 15, 14, 10)
        );

        button.setMaximumSize(
            new Dimension(200, 48)
        );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        return button;
    }

    // =========================
    // SUMMARY CARD
    // =========================

    private JPanel createCard(String heading, String amount) {

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(lightGreen);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(210, 230, 218)
                ),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
            )
        );

        JLabel title = new JLabel(heading);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(darkGreen);

        JLabel value = new JLabel(amount);
        value.setFont(new Font("Arial", Font.BOLD, 27));
        value.setForeground(green);

        card.add(title, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);

        return card;
    }

    // =========================
    // MAIN METHOD
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Dashboard();
        });

    }
}
