import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.text.DecimalFormat;

public class LedgerlyAccounting extends JFrame {
    static final Color NAVY = new Color(23, 63, 60), GREEN = new Color(23, 102, 91);
    static final Color CREAM = new Color(247, 244, 233), INK = new Color(32, 59, 58);
    static final Color MUTED = new Color(107, 127, 126), LINE = new Color(215, 224, 216);
    final Map<String, User> users = new LinkedHashMap<>();
    final Map<String, java.util.List<Entry>> entries = new HashMap<>();
    final LinkedHashMap<String, CurrencyInfo> countries = new LinkedHashMap<>();
    User current;
    String activeTab = "Journal";
    final DecimalFormat fmt = new DecimalFormat("#,##0.00");

    static class CurrencyInfo {
        String code, symbol, name;
        CurrencyInfo(String c, String s, String n) { code=c; symbol=s; name=n; }
        public String toString() { return code + " (" + symbol + ") — " + name; }
    }
    static class User {
        String name,email,hash,country,code,symbol,currencyName;
        User(String n,String e,String h,String c,CurrencyInfo i) {
            name=n; email=e; hash=h; country=c; code=i.code; symbol=i.symbol; currencyName=i.name;
        }
    }
    static class Entry {
        String date,description,account,type;
        double amount;
        Entry(String d,String desc,String a,String t,double v) {
            date=d; description=desc; account=a; type=t; amount=v;
        }
    }

    public LedgerlyAccounting() {
        super("Ledgerly | Accounting Workspace");
        countries.put("Philippines",new CurrencyInfo("PHP","₱","Philippine Peso"));
        countries.put("United States",new CurrencyInfo("USD","$","US Dollar"));
        countries.put("Canada",new CurrencyInfo("CAD","$","Canadian Dollar"));
        countries.put("United Kingdom",new CurrencyInfo("GBP","£","British Pound"));
        countries.put("Australia",new CurrencyInfo("AUD","$","Australian Dollar"));
        countries.put("Singapore",new CurrencyInfo("SGD","$","Singapore Dollar"));
        countries.put("India",new CurrencyInfo("INR","₹","Indian Rupee"));
        countries.put("Japan",new CurrencyInfo("JPY","¥","Japanese Yen"));
        countries.put("Indonesia",new CurrencyInfo("IDR","Rp","Indonesian Rupiah"));
        countries.put("Malaysia",new CurrencyInfo("MYR","RM","Malaysian Ringgit"));
        countries.put("United Arab Emirates",new CurrencyInfo("AED","د.إ","UAE Dirham"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050,680); setMinimumSize(new Dimension(820,580)); setLocationRelativeTo(null);
        showLogin();
    }

    JPanel panel(Color bg,int pad) {
        JPanel p=new JPanel(); p.setBackground(bg);
        p.setBorder(new EmptyBorder(pad,pad,pad,pad)); return p;
    }
    JLabel label(String text,int size,boolean bold,Color fg) {
        JLabel l=new JLabel(text); l.setFont(new Font(size>=18?"Georgia":"Arial",bold?Font.BOLD:Font.PLAIN,size));
        l.setForeground(fg); return l;
    }
    JButton button(String text,Runnable action,boolean primary) {
        JButton b=new JButton(text); b.setFont(new Font("Arial",Font.BOLD,12));
        b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBackground(primary?GREEN:Color.WHITE); b.setForeground(primary?Color.WHITE:GREEN);
        b.setBorder(new EmptyBorder(9,14,9,14)); b.addActionListener(e->action.run()); return b;
    }
    JTextField field() {
        JTextField f=new JTextField(22); f.setFont(new Font("Arial",Font.PLAIN,13));
        f.setBorder(new CompoundBorder(new LineBorder(LINE),new EmptyBorder(8,8,8,8))); return f;
    }
    JPasswordField passwordField() {
        JPasswordField f=new JPasswordField(22); f.setFont(new Font("Arial",Font.PLAIN,13));
        f.setBorder(new CompoundBorder(new LineBorder(LINE),new EmptyBorder(8,8,8,8))); return f;
    }
    JPanel formField(String title,JComponent input) {
        JPanel p=new JPanel(new BorderLayout(0,5)); p.setBackground(Color.WHITE);
        p.add(label(title,12,true,INK),BorderLayout.NORTH); p.add(input,BorderLayout.CENTER);
        p.setBorder(new EmptyBorder(6,0,5,0)); return p;
    }
    void screen(Component c) { setContentPane((Container)c); revalidate(); repaint(); }
    String hash(String value) {
        try {
            byte[] bytes=MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder out=new StringBuilder(); for(byte b:bytes) out.append(String.format("%02x",b)); return out.toString();
        } catch(Exception e) { throw new RuntimeException(e); }
    }
    void error(String s) { JOptionPane.showMessageDialog(this,s,"Please check",JOptionPane.ERROR_MESSAGE); }

    void showLogin() {
        JPanel outer=new JPanel(new GridLayout(1,2)); outer.setBackground(CREAM);
        JPanel brand=panel(NAVY,32); brand.setLayout(new BoxLayout(brand,BoxLayout.Y_AXIS));
        brand.add(Box.createVerticalStrut(70)); brand.add(label("▥ Ledgerly",30,true,Color.WHITE));
        brand.add(Box.createVerticalStrut(12)); brand.add(label("Simple • Accurate • Yours",13,false,new Color(228,237,231)));
        brand.add(Box.createVerticalStrut(24)); brand.add(label("Journal / Ledger / Columnar",11,false,new Color(201,217,209)));
        brand.add(Box.createVerticalGlue()); brand.add(label("ACCOUNTING WORKSPACE",10,true,new Color(201,163,78)));
        outer.add(brand);
        JPanel right=new JPanel(new GridBagLayout()); right.setBackground(CREAM);
        JPanel card=panel(Color.WHITE,28); card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(new LineBorder(LINE),new EmptyBorder(25,28,25,28)));
        card.setPreferredSize(new Dimension(390,390));
        card.add(label("Welcome Back",25,true,INK)); card.add(Box.createVerticalStrut(6));
        card.add(label("Log in to continue to your account.",12,false,MUTED)); card.add(Box.createVerticalStrut(15));
        JTextField email=field(); JPasswordField pass=passwordField();
        card.add(formField("Email address",email)); card.add(formField("Password",pass));
        card.add(Box.createVerticalStrut(15));
        JButton login=button("Log in",()->{
            User u=users.get(email.getText().trim().toLowerCase(Locale.ROOT));
            if(u!=null && u.hash.equals(hash(new String(pass.getPassword())))){current=u;activeTab="Journal";showMain();}
            else error("Email or password is incorrect. Sign up first if needed.");
        },true);
        login.setAlignmentX(Component.LEFT_ALIGNMENT); login.setMaximumSize(new Dimension(Integer.MAX_VALUE,42)); card.add(login);
        JPanel links=new JPanel(new FlowLayout(FlowLayout.LEFT,0,5)); links.setBackground(Color.WHITE);
        links.add(button("Sign up",this::showSignup,false));
        links.add(button("Forgot password?",()->JOptionPane.showMessageDialog(this,
                "Password reset email is not available in this prototype."),false));
        card.add(links); card.add(label("Demo: data resets when the app closes.",9,false,MUTED));
        right.add(card); outer.add(right); screen(outer);
    }

    void showSignup() {
        JPanel outer=new JPanel(new GridBagLayout()); outer.setBackground(CREAM);
        JPanel card=panel(Color.WHITE,22); card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(new LineBorder(LINE),new EmptyBorder(20,25,20,25)));
        card.setPreferredSize(new Dimension(470,570));
        card.add(label("Create Your Account",23,true,INK));
        card.add(label("Select your country to set your default currency.",11,false,MUTED));
        JTextField name=field(),email=field(); JPasswordField pass=passwordField(),confirm=passwordField();
        card.add(formField("Full name",name)); card.add(formField("Email address",email));
        card.add(formField("Password (minimum 8 characters)",pass));
        card.add(formField("Confirm password",confirm));
        JComboBox<String> country=new JComboBox<>(countries.keySet().toArray(new String[0]));
        country.setSelectedItem("Philippines"); country.setBackground(Color.WHITE);
        card.add(formField("Country",country));
        JLabel hint=label("",11,true,GREEN); country.addActionListener(e->{
            CurrencyInfo ci=countries.get(country.getSelectedItem()); hint.setText("Default currency: "+ci.code+" ("+ci.symbol+") — "+ci.name);
        });
        CurrencyInfo initial=countries.get("Philippines"); hint.setText("Default currency: "+initial.code+" ("+initial.symbol+") — "+initial.name);
        card.add(hint); card.add(Box.createVerticalStrut(10));
        JButton signup=button("Sign up",()->{
            String n=name.getText().trim(), em=email.getText().trim().toLowerCase(Locale.ROOT);
            String pw=new String(pass.getPassword()), cpw=new String(confirm.getPassword());
            String c=(String)country.getSelectedItem();
            if(n.isEmpty()||em.isEmpty()||pw.isEmpty()||cpw.isEmpty()){error("Please complete all fields.");return;}
            if(!em.matches("^[^@\\\\s]+@[^@\\\\s]+\\\\.[^@\\\\s]+$")){error("Enter a valid email address.");return;}
            if(pw.length()<8){error("Password must contain at least 8 characters.");return;}
            if(!pw.equals(cpw)){error("Passwords do not match.");return;}
            if(users.containsKey(em)){error("This email is already registered.");return;}
            User u=new User(n,em,hash(pw),c,countries.get(c)); users.put(em,u); entries.put(em,new ArrayList<>());
            JOptionPane.showMessageDialog(this,"Account created!\\nCountry: "+c+"\\nCurrency: "+u.code+" ("+u.symbol+")");
            showLogin();
        },true);
        signup.setAlignmentX(Component.LEFT_ALIGNMENT); signup.setMaximumSize(new Dimension(Integer.MAX_VALUE,40)); card.add(signup);
        card.add(button("Back to log in",this::showLogin,false)); outer.add(card); screen(outer);
    }

    void showMain() {
        if(current==null){showLogin();return;}
        JPanel root=new JPanel(new BorderLayout()); root.setBackground(CREAM);
        JPanel header=new JPanel(new BorderLayout(12,0)); header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0,LINE),new EmptyBorder(12,16,12,16)));
        header.add(label("▥ Ledgerly",19,true,NAVY),BorderLayout.WEST);
        JPanel tabs=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0)); tabs.setBackground(Color.WHITE);
        for(String t:new String[]{"Journal","Ledger","Columnar"}) tabs.add(button(t,()->{activeTab=t;showMain();},activeTab.equals(t)));
        header.add(tabs,BorderLayout.CENTER);
        header.add(button(current.name+"  ▾",()->accountMenu(),false),BorderLayout.EAST); root.add(header,BorderLayout.NORTH);
        JPanel body=panel(CREAM,22); body.setLayout(new BorderLayout(0,16));
        JPanel title=new JPanel(new BorderLayout()); title.setBackground(CREAM);
        title.add(label(activeTab,25,true,INK),BorderLayout.WEST);
        title.add(label("Currency: "+current.code+" ("+current.symbol+")",12,true,MUTED),BorderLayout.EAST);
        body.add(title,BorderLayout.NORTH);
        if(activeTab.equals("Journal"))body.add(journal(),BorderLayout.CENTER);
        else if(activeTab.equals("Ledger"))body.add(ledger(),BorderLayout.CENTER);
        else body.add(columnar(),BorderLayout.CENTER);
        root.add(body,BorderLayout.CENTER); screen(root);
    }
    void accountMenu() {
        JPopupMenu menu=new JPopupMenu();
        JMenuItem c=new JMenuItem("Country: "+current.country); c.setEnabled(false); menu.add(c);
        JMenuItem cur=new JMenuItem("Currency: "+current.code+" ("+current.symbol+")"); cur.setEnabled(false); menu.add(cur);
        menu.addSeparator(); JMenuItem logout=new JMenuItem("Log out");
        logout.addActionListener(e->{current=null;showLogin();});menu.add(logout);menu.show(getContentPane(),getWidth()-220,50);
    }
    JPanel card() {
        JPanel p=new JPanel(new BorderLayout(0,12));p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(LINE),new EmptyBorder(15,15,15,15)));return p;
    }
    JTable table(String[] cols,DefaultTableModel model) {
        JTable t=new JTable(model);t.setRowHeight(29);t.setFont(new Font("Arial",0,12));
        t.getTableHeader().setFont(new Font("Arial",Font.BOLD,12));t.getTableHeader().setBackground(new Color(238,242,233));
        t.setGridColor(LINE);t.setFillsViewportHeight(true);return t;
    }
    DefaultTableModel model(String... cols) {
        return new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
    }
    JPanel journal() {
        JPanel p=card(); JPanel top=new JPanel(new BorderLayout());top.setBackground(Color.WHITE);
        top.add(label("Record and review your financial entries.",12,false,MUTED),BorderLayout.WEST);
        top.add(button("+ Add Entry",this::addEntry,true),BorderLayout.EAST);p.add(top,BorderLayout.NORTH);
        DefaultTableModel m=model("Date","Description","Account","Type","Amount");
        for(Entry e:entries.get(current.email))m.addRow(new Object[]{e.date,e.description,e.account,e.type,current.symbol+fmt.format(e.amount)});
        if(m.getRowCount()==0)m.addRow(new Object[]{"—","No entries yet","Add your first entry","—","—"});
        p.add(new JScrollPane(table(new String[]{"Date","Description","Account","Type","Amount"},m)),BorderLayout.CENTER);return p;
    }
    void addEntry() {
        JTextField date=field();date.setText(LocalDate.now().toString());
        JTextField desc=field(),account=field(),amount=field();
        JComboBox<String> type=new JComboBox<>(new String[]{"Debit","Credit"});
        JPanel form=new JPanel(new GridLayout(0,1,3,3));form.setPreferredSize(new Dimension(350,300));
        form.add(formField("Date (YYYY-MM-DD)",date));form.add(formField("Description",desc));
        form.add(formField("Account name",account));form.add(formField("Entry type",type));
        form.add(formField("Amount ("+current.code+")",amount));
        if(JOptionPane.showConfirmDialog(this,form,"Add Journal Entry",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;
        double value;try{value=Double.parseDouble(amount.getText().trim());if(value<=0)throw new NumberFormatException();}
        catch(NumberFormatException ex){error("Enter an amount greater than zero.");return;}
        if(date.getText().trim().isEmpty()||desc.getText().trim().isEmpty()||account.getText().trim().isEmpty()){error("Complete all fields.");return;}
        entries.get(current.email).add(new Entry(date.getText().trim(),desc.getText().trim(),account.getText().trim(),(String)type.getSelectedItem(),value));showMain();
    }
    JPanel ledger() {
        JPanel p=card();p.add(label("Accounts grouped by account name",14,true,INK),BorderLayout.NORTH);
        DefaultTableModel m=model("Account","Debit","Credit","Net (Debit - Credit)");
        Map<String,double[]> totals=new TreeMap<>();
        for(Entry e:entries.get(current.email)){double[] a=totals.computeIfAbsent(e.account,k->new double[2]);a[e.type.equals("Debit")?0:1]+=e.amount;}
        for(Map.Entry<String,double[]> e:totals.entrySet())m.addRow(new Object[]{e.getKey(),current.symbol+fmt.format(e.getValue()[0]),current.symbol+fmt.format(e.getValue()[1]),current.symbol+fmt.format(e.getValue()[0]-e.getValue()[1])});
        if(m.getRowCount()==0)m.addRow(new Object[]{"No accounts yet","—","—","—"});
        p.add(new JScrollPane(table(new String[]{"Account","Debit","Credit","Net (Debit - Credit)"},m)),BorderLayout.CENTER);return p;
    }
    JPanel columnar() {
        JPanel p=card();p.add(label("Debit and credit worksheet",14,true,INK),BorderLayout.NORTH);
        DefaultTableModel m=model("Date","Description","Account","Debit","Credit");double d=0,c=0;
        for(Entry e:entries.get(current.email)){double debit=e.type.equals("Debit")?e.amount:0,credit=e.type.equals("Credit")?e.amount:0;d+=debit;c+=credit;
            m.addRow(new Object[]{e.date,e.description,e.account,debit==0?"—":current.symbol+fmt.format(debit),credit==0?"—":current.symbol+fmt.format(credit)});}
        m.addRow(new Object[]{"","TOTAL","",current.symbol+fmt.format(d),current.symbol+fmt.format(c)});
        p.add(new JScrollPane(table(new String[]{"Date","Description","Account","Debit","Credit"},m)),BorderLayout.CENTER);return p;
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(()->new LedgerlyAccounting().setVisible(true)); }
}
