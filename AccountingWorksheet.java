import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;

public class AccountingWorksheet extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JPanel headerPanel;

    private final int SUMMARY_ROWS = 4;

    
    private final String[] columns = {
        "#", "Date", "Account Name",
        "TB Dr", "TB Cr",
        "Adj Dr", "Adj Cr",
        "Adjusted Dr", "Adjusted Cr",
        "Income Dr", "Income Cr",
        "Equity Dr", "Equity Cr",
        "Balance Dr", "Balance Cr"
    };

    private final int[] widths = {
         40, 65, 135,
    60, 60,
    60, 60,
    70, 70,
    60, 60,
    60, 60,
    60, 60   
     };

    private final String[] groups = {
        "TRIAL\nBALANCE",
        "Adjustments",
        "Adjusted\nTrial Balance",
        "Income\nStatement",
        "Owner's\nEquity",
        "Balance\nSheet"
    };

    public AccountingWorksheet() {

        setTitle("Accounting Worksheet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);
        setMinimumSize(new Dimension(850, 500));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);

        model = new DefaultTableModel(0, columns.length) {

            @Override
            public String getColumnName(int column) {
                return columns[column];
            }

            @Override
            public boolean isCellEditable(int row, int column) {

                int accountRows = getRowCount() - SUMMARY_ROWS;

                             return row < accountRows && column != 0;
            }
        };

        table = new JTable(model);

       table = new JTable(model);
table.setTableHeader(null);
table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
table.setRowHeight(25);


     
        for (int i = 0; i < widths.length; i++) {

            TableColumn column =
                    table.getColumnModel().getColumn(i);

            column.setPreferredWidth(widths[i]);
            column.setMinWidth(widths[i]);
            column.setMaxWidth(widths[i]);
        }


        table.setDefaultRenderer(Object.class,
                new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean selected,
                    boolean focused,
                    int row,
                    int column) {

                JLabel label = (JLabel)
                    super.getTableCellRendererComponent(
                        table, value, selected,
                        focused, row, column
                    );

                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
                label.setBorder(
                    new MatteBorder(0, 0, 1, 1, Color.BLACK)
                );

                int accountRows =
                        model.getRowCount() - SUMMARY_ROWS;

                if (column == 0 || column == 1) {
                    label.setHorizontalAlignment(
                            SwingConstants.CENTER);
                } else {
                    label.setHorizontalAlignment(
                            SwingConstants.LEFT);
                }


                if (row >= accountRows) {
                    label.setFont(
                        new Font("Arial", Font.BOLD, 10)
                    );

                    if (column == 2) {
                        label.setHorizontalAlignment(
                                SwingConstants.LEFT);
                    }
                } else {
                    label.setFont(
                        new Font("Arial", Font.PLAIN, 11)
                    );
                }

                return label;
            }
        });

       
        headerPanel = new WorksheetHeader();

        headerPanel.setPreferredSize(
                new Dimension(getTotalWidth(), 50));

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setColumnHeaderView(headerPanel);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1));

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getViewport().setBackground(Color.WHITE);

      
        JButton addRowButton = new JButton("+ Add Row");
        JButton removeRowButton = new JButton("- Remove Last Row");

        addRowButton.setFocusPainted(false);
        removeRowButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 8));

        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addRowButton);
        buttonPanel.add(removeRowButton);

        addRowButton.addActionListener(e -> addAccountRow());

   
        removeRowButton.addActionListener(e -> removeAccountRow());

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);

        
        for (int i = 1; i <= 15; i++) {
            addAccountRow();
        }

        addSummaryRows();

        
        renumberRows();
    }

    private int getTotalWidth() {

        int total = 0;

        for (int width : widths) {
            total += width;
        }

        return total;
    }

    private void addAccountRow() {

        int summaryStart = Math.max(
                0, model.getRowCount() - SUMMARY_ROWS);

        Object[] newRow = new Object[columns.length];

        model.insertRow(summaryStart, newRow);

        renumberRows();

        table.setRowHeight(summaryStart, 25);
    }

    
    private void removeAccountRow() {

        int accountRows =
                model.getRowCount() - SUMMARY_ROWS;

        if (accountRows <= 1) {
            JOptionPane.showMessageDialog(
                this,
                "At least one account row must remain.",
                "Cannot Remove Row",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        model.removeRow(accountRows - 1);

        renumberRows();
    }

    private void addSummaryRows() {

        Object[] total = new Object[columns.length];
        total[2] = "TOTAL";
        model.addRow(total);

        Object[] netIncome = new Object[columns.length];
        netIncome[2] = "NET INCOME";
        model.addRow(netIncome);

        Object[] endingEquity = new Object[columns.length];
        endingEquity[2] = "<html>ENDING OWNER'S<br>EQUITY</html>";
        model.addRow(endingEquity);

        Object[] grandTotal = new Object[columns.length];
        grandTotal[2] = "GRAND TOTAL";
        model.addRow(grandTotal);

        int lastRows = model.getRowCount();

        table.setRowHeight(lastRows - 4, 25);
        table.setRowHeight(lastRows - 3, 25);
        table.setRowHeight(lastRows - 2, 25);
        table.setRowHeight(lastRows - 1, 25);
    }

    
    private void renumberRows() {

        int accountRows =
                model.getRowCount() - SUMMARY_ROWS;

        for (int i = 0; i < accountRows; i++) {
            model.setValueAt(i + 1, i, 0);
        }
    }

   
    private class WorksheetHeader extends JPanel {

        public WorksheetHeader() {
            setBackground(Color.WHITE);

            setPreferredSize(
                    new Dimension(getTotalWidth(), 50));
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));

            int groupHeight = 29;
            int subHeight = 21;

            drawHeader(g2, 0, 0,
                    widths[0], groupHeight + subHeight, "#");

            drawHeader(g2, widths[0], 0,
                    widths[1], groupHeight + subHeight, "Date");

            drawHeader(g2, widths[0] + widths[1], 0,
                    widths[2], groupHeight + subHeight,
                    "Account\nName");

          
            int x = widths[0] + widths[1] + widths[2];
            int col = 3;

            for (String group : groups) {

                int debitWidth = widths[col];
                int creditWidth = widths[col + 1];

                int groupWidth = debitWidth + creditWidth;

            
                drawHeader(
                    g2, x, 0,
                    groupWidth, groupHeight, group
                );

                drawHeader(
                    g2, x, groupHeight,
                    debitWidth, subHeight, "Dr"
                );

                drawHeader(
                    g2, x + debitWidth, groupHeight,
                    creditWidth, subHeight, "Cr"
                );

                x += groupWidth;
                col += 2;
            }

            g2.dispose();
        }

        private void drawHeader(
                Graphics2D g2,
                int x,
                int y,
                int width,
                int height,
                String text) {

            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, width, height);

            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, width, height);

            g2.setFont(new Font("Arial", Font.BOLD, 9));

            FontMetrics fm = g2.getFontMetrics();

            String[] lines = text.split("\n");

            int totalHeight = lines.length * fm.getHeight();

            int textY = y
                    + (height - totalHeight) / 2
                    + fm.getAscent();

            for (String line : lines) {

                int textX = x
                        + (width - fm.stringWidth(line)) / 2;

                g2.drawString(line, textX, textY);

                textY += fm.getHeight();
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new AccountingWorksheet().setVisible(true);
        });
    }
}
