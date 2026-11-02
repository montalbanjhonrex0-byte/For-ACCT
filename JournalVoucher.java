import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JournalVoucher extends JFrame {


    private final Color GREEN = new Color(22, 130, 85);
    private final Color DARK_GREEN = new Color(15, 95, 62);
    private final Color LIGHT_GREEN = new Color(235, 247, 240);
    private final Color WHITE = Color.WHITE;
    private final Color GRAY = new Color(110, 120, 115);
    private final Color BORDER = new Color(225, 232, 228);
    private final Color RED = new Color(210, 65, 65);


    private JTextField dateField;
    private JTextField descriptionField;


    private JPanel rowsPanel;
    private final ArrayList<EntryRow> rows = new ArrayList<>();


    private JLabel debitTotal;
    private JLabel creditTotal;
    private JLabel differenceLabel;
    private JLabel balanceStatus;

    private final String[] accounts = {
        "Select Account",
        "Cash",
        "Accounts Receivable",
        "Supplies",
        "Equipment",
        "Accounts Payable",
        "Capital",
        "Service Revenue",
        "Rent Expense",
        "Utilities Expense"
    };

    public JournalVoucher() {

        setTitle("Create Journal Voucher");
        setSize(1200, 850);
        setMinimumSize(new Dimension(950, 700));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);

    
        mainPanel.add(createSidebar(), BorderLayout.WEST);

    
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(WHITE);

        rightPanel.add(createHeader(), BorderLayout.NORTH);
        rightPanel.add(createScrollableForm(), BorderLayout.CENTER);
        rightPanel.add(createFooter(), BorderLayout.SOUTH);

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);

        // START WITH ONE ROW
        addEntryRow();

        setVisible(true);
    }

    
    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setBackground(GREEN);
        sidebar.setPreferredSize(new Dimension(230, 0));

        sidebar.setLayout(new BoxLayout(
            sidebar, BoxLayout.Y_AXIS
        ));

        sidebar.setBorder(
            new EmptyBorder(30, 18, 25, 18)
        );

        JLabel logo = new JLabel(
            "<html>▣ JOURNAL<br>&nbsp;&nbsp;&nbsp; SYSTEM</html>"
        );

        logo.setFont(new Font("Arial", Font.BOLD, 21));
        logo.setForeground(WHITE);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(65));

        JButton dashboardButton =
            createSidebarButton("⌂   Dashboard", false);

        JButton voucherButton =
            createSidebarButton("▤   Create Voucher", true);

        JButton recordsButton =
            createSidebarButton("▣   Records", false);

        sidebar.add(dashboardButton);
        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(voucherButton);
        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(recordsButton);

        sidebar.add(Box.createVerticalGlue());

        JSeparator separator = new JSeparator();
        separator.setForeground(LIGHT_GREEN);

        sidebar.add(separator);
        sidebar.add(Box.createVerticalStrut(20));

        JButton logoutButton =
            createSidebarButton("⇥   Logout", false);

        sidebar.add(logoutButton);

        dashboardButton.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this, "Dashboard navigation will be connected later."
            )
        );

        recordsButton.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this, "Records page will be added later."
            )
        );

        logoutButton.addActionListener(e -> dispose());

        return sidebar;
    }

    private JButton createSidebarButton(
        String text, boolean selected
    ) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(selected ? DARK_GREEN : WHITE);
        button.setBackground(selected ? WHITE : GREEN);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setBorder(
            new EmptyBorder(15, 12, 15, 8)
        );

        button.setMaximumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        return button;
    }

    
    private JPanel createHeader() {

        JPanel header = new JPanel();
        header.setBackground(WHITE);

        header.setLayout(new BoxLayout(
            header, BoxLayout.Y_AXIS
        ));

        header.setBorder(
            new EmptyBorder(30, 35, 15, 35)
        );

        JLabel title = new JLabel(
            "JOURNAL VOUCHER"
        );

        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(DARK_GREEN);

        JLabel subtitle = new JLabel(
            "Enter transaction information and account details."
        );

        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(GRAY);

        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        return header;
    }

    

    private JScrollPane createScrollableForm() {

        JPanel form = new JPanel();

        form.setBackground(WHITE);

        form.setLayout(new BoxLayout(
            form, BoxLayout.Y_AXIS
        ));

        form.setBorder(
            new EmptyBorder(15, 30, 20, 30)
        );

        form.add(createInformationCard());
        form.add(Box.createVerticalStrut(20));

        form.add(createEntriesCard());
        form.add(Box.createVerticalStrut(20));

        form.add(createBalanceCard());

        JScrollPane scrollPane = new JScrollPane(form);

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(WHITE);

        return scrollPane;
    }

    
    private JPanel createInformationCard() {

        JPanel card = createCard();

        card.setLayout(new BorderLayout(15, 20));

        JLabel title = createSectionTitle(
            "▣  Transaction Information"
        );

        JPanel fields = new JPanel(
            new GridLayout(1, 2, 25, 0)
        );

        fields.setBackground(WHITE);

        dateField = new JTextField(
            LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
        );

        descriptionField = new JTextField();
        descriptionField.setToolTipText(
            "Enter transaction description"
        );

        styleInput(dateField);
        styleInput(descriptionField);

        fields.add(
            createFieldPanel("Transaction Date", dateField)
        );

        fields.add(
            createFieldPanel("Description", descriptionField)
        );

        card.add(title, BorderLayout.NORTH);
        card.add(fields, BorderLayout.CENTER);

        return card;
    }

    
    private JPanel createEntriesCard() {

        JPanel card = createCard();

        card.setLayout(new BorderLayout(12, 18));

        
        JPanel top = new JPanel(new BorderLayout());

        top.setBackground(WHITE);

        JLabel title = createSectionTitle(
            "▤  Account Entries"
        );

        JButton addButton = new JButton("+  Add Row");

        styleButton(addButton, WHITE, GREEN);

        addButton.addActionListener(e -> addEntryRow());

        top.add(title, BorderLayout.WEST);
        top.add(addButton, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);

        
        JPanel tableHeader = new JPanel(
            new GridLayout(1, 5, 5, 0)
        );

        tableHeader.setBackground(LIGHT_GREEN);

        tableHeader.setBorder(
            new EmptyBorder(15, 8, 15, 8)
        );

        String[] headings = {
            "NO.", "ACCOUNT", "DEBIT (₱)",
            "CREDIT (₱)", "ACTION"
        };

        for (String heading : headings) {

            JLabel label = new JLabel(heading);

            label.setFont(
                new Font("Arial", Font.BOLD, 12)
            );

            label.setForeground(DARK_GREEN);

            label.setHorizontalAlignment(
                SwingConstants.CENTER
            );

            tableHeader.add(label);
        }

        
        rowsPanel = new JPanel();

        rowsPanel.setBackground(WHITE);

        rowsPanel.setLayout(new BoxLayout(
            rowsPanel, BoxLayout.Y_AXIS
        ));

        JPanel table = new JPanel(new BorderLayout());

        table.setBackground(WHITE);

        table.setBorder(
            BorderFactory.createLineBorder(BORDER)
        );

        table.add(tableHeader, BorderLayout.NORTH);
        table.add(rowsPanel, BorderLayout.CENTER);

        card.add(table, BorderLayout.CENTER);

        return card;
    }

    

    private void addEntryRow() {

        EntryRow row = new EntryRow();

        rows.add(row);

        JPanel panel = new JPanel(
            new GridLayout(1, 5, 8, 0)
        );

        panel.setBackground(WHITE);

        panel.setBorder(
            new EmptyBorder(10, 8, 10, 8)
        );

        panel.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 65)
        );

        row.numberLabel = new JLabel();
        row.numberLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        row.accountBox = new JComboBox<>(accounts);

        row.debitField = new JTextField();
        row.creditField = new JTextField();

        row.debitField.setText("0.00");
        row.creditField.setText("0.00");

        styleInput(row.debitField);
        styleInput(row.creditField);

        row.debitField.setHorizontalAlignment(
            JTextField.RIGHT
        );

        row.creditField.setHorizontalAlignment(
            JTextField.RIGHT
        );

        JButton deleteButton = new JButton("Delete");

        deleteButton.setForeground(RED);
        deleteButton.setBackground(WHITE);
        deleteButton.setFocusPainted(false);

        row.panel = panel;

        panel.add(row.numberLabel);
        panel.add(row.accountBox);
        panel.add(row.debitField);
        panel.add(row.creditField);
        panel.add(deleteButton);

        
        DocumentListener listener = new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                updateBalance();
            }

            public void removeUpdate(DocumentEvent e) {
                updateBalance();
            }

            public void changedUpdate(DocumentEvent e) {
                updateBalance();
            }
        };

        row.debitField.getDocument().addDocumentListener(listener);
        row.creditField.getDocument().addDocumentListener(listener);

        
        deleteButton.addActionListener(e -> {

            if (rows.size() > 1) {

                rows.remove(row);
                rowsPanel.remove(panel);

                refreshRowNumbers();
                updateBalance();

                rowsPanel.revalidate();
                rowsPanel.repaint();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "At least one entry row is required."
                );
            }
        });

        rowsPanel.add(panel);

        refreshRowNumbers();

        rowsPanel.revalidate();
        rowsPanel.repaint();

        updateBalance();
    }

    

    private void refreshRowNumbers() {

        for (int i = 0; i < rows.size(); i++) {

            rows.get(i).numberLabel.setText(
                String.valueOf(i + 1)
            );
        }
    }

    
    private JPanel createBalanceCard() {

        JPanel card = createCard();

        card.setLayout(new BorderLayout(15, 15));

        JLabel title = new JLabel("BALANCE SUMMARY");

        title.setFont(
            new Font("Arial", Font.BOLD, 16)
        );

        title.setForeground(GREEN);

        JPanel summary = new JPanel(
            new GridLayout(1, 3, 15, 0)
        );

        summary.setBackground(WHITE);

        debitTotal = new JLabel("₱0.00");
        creditTotal = new JLabel("₱0.00");
        differenceLabel = new JLabel("₱0.00");

        balanceStatus = new JLabel("BALANCED");

        summary.add(
            createSummaryItem("Total Debit", debitTotal)
        );

        summary.add(
            createSummaryItem("Total Credit", creditTotal)
        );

        JPanel differencePanel = new JPanel();

        differencePanel.setBackground(WHITE);

        differencePanel.setLayout(new BoxLayout(
            differencePanel, BoxLayout.Y_AXIS
        ));

        JLabel differenceTitle = new JLabel("Difference");

        differenceTitle.setForeground(GRAY);

        differenceLabel.setFont(
            new Font("Arial", Font.BOLD, 25)
        );

        differenceLabel.setForeground(Color.BLACK);

        balanceStatus.setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        balanceStatus.setOpaque(true);
        balanceStatus.setBackground(LIGHT_GREEN);
        balanceStatus.setForeground(DARK_GREEN);

        balanceStatus.setBorder(
            new EmptyBorder(6, 10, 6, 10)
        );

        differencePanel.add(differenceTitle);
        differencePanel.add(Box.createVerticalStrut(8));
        differencePanel.add(differenceLabel);
        differencePanel.add(Box.createVerticalStrut(8));
        differencePanel.add(balanceStatus);

        summary.add(differencePanel);

        card.add(title, BorderLayout.NORTH);
        card.add(summary, BorderLayout.CENTER);

        return card;
    }

    

    private JPanel createSummaryItem(
        String heading, JLabel amount
    ) {

        JPanel panel = new JPanel();

        panel.setBackground(WHITE);

        panel.setLayout(new BoxLayout(
            panel, BoxLayout.Y_AXIS
        ));

        JLabel title = new JLabel(heading);

        title.setForeground(GRAY);

        amount.setFont(
            new Font("Arial", Font.BOLD, 25)
        );

        amount.setForeground(GREEN);

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(amount);

        return panel;
    }

    

    private void updateBalance() {

        double debit = 0;
        double credit = 0;

        for (EntryRow row : rows) {

            debit += getAmount(row.debitField);
            credit += getAmount(row.creditField);
        }

        double difference = Math.abs(debit - credit);

        debitTotal.setText(formatPeso(debit));
        creditTotal.setText(formatPeso(credit));
        differenceLabel.setText(formatPeso(difference));

        if (difference < 0.005) {

            balanceStatus.setText("BALANCED");
            balanceStatus.setForeground(DARK_GREEN);
            balanceStatus.setBackground(LIGHT_GREEN);

        } else {

            balanceStatus.setText("NOT BALANCED");
            balanceStatus.setForeground(RED);
            balanceStatus.setBackground(
                new Color(255, 235, 235)
            );
        }
    }

    

    private double getAmount(JTextField field) {

        try {

            String value = field.getText().trim();

            if (value.isEmpty()) {
                return 0;
            }

            return Double.parseDouble(value);

        } catch (NumberFormatException e) {

            return 0;
        }
    }

    

    private String formatPeso(double amount) {

        return String.format(
            Locale.US, "₱%,.2f", amount
        );
    }

    

    private JPanel createFooter() {

        JPanel footer = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 12, 15)
        );

        footer.setBackground(WHITE);

        footer.setBorder(
            new EmptyBorder(0, 15, 10, 25)
        );

        JButton resetButton = new JButton("↻  Reset");

        JButton saveButton = new JButton("▣  Save Voucher");

        styleButton(resetButton, WHITE, GRAY);
        styleButton(saveButton, GREEN, WHITE);

        resetButton.addActionListener(e -> resetForm());

        saveButton.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "Saving transactions will be connected when we build Feature 4."
            )
        );

        footer.add(resetButton);
        footer.add(saveButton);

        return footer;
    }

    

    private void resetForm() {

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear the form?",
            "Reset Form",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            dateField.setText(
                LocalDate.now().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
            );

            descriptionField.setText("");

            rows.clear();
            rowsPanel.removeAll();

            addEntryRow();

            rowsPanel.revalidate();
            rowsPanel.repaint();

            updateBalance();
        }
    }

    
    private JPanel createCard() {

        JPanel card = new JPanel();

        card.setBackground(WHITE);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(22, 22, 22, 22)
            )
        );

        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        return card;
    }

    private JLabel createSectionTitle(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Arial", Font.BOLD, 18)
        );

        label.setForeground(GREEN);

        return label;
    }

    private JPanel createFieldPanel(
        String title, JTextField field
    ) {

        JPanel panel = new JPanel();

        panel.setBackground(WHITE);

        panel.setLayout(new BoxLayout(
            panel, BoxLayout.Y_AXIS
        ));

        JLabel label = new JLabel(title);

        label.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        label.setForeground(GRAY);

        field.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 45)
        );

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);

        return panel;
    }

    private void styleInput(JTextField field) {

        field.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        field.setBackground(WHITE);

        field.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 12, 10, 12)
            )
        );
    }

    private void styleButton(
        JButton button, Color background, Color foreground
    ) {

        button.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        button.setBackground(background);
        button.setForeground(foreground);

        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setBorderPainted(
            background != GREEN
        );

        button.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    background == GREEN ? GREEN : BORDER
                ),
                new EmptyBorder(11, 17, 11, 17)
            )
        );
    }

    

    private class EntryRow {

        JLabel numberLabel;
        JComboBox<String> accountBox;

        JTextField debitField;
        JTextField creditField;

        JPanel panel;
    }

        public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new JournalVoucher();
        });
    }
}
