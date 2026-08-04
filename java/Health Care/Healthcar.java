import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

// ==================== MODEL CLASSES ====================
class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String username, password;
    public User(String u, String p) { username = u; password = p; }
    public String getUsername() { return username; }
    public boolean checkPassword(String p) { return password.equals(p); }
}

class Admin extends User {
    public Admin(String u, String p) { super(u, p); }
}

class Doctor extends User {
    private String name, specialization, contact;
    public Doctor(String u, String p, String n, String s, String c) { 
        super(u, p); name = n; specialization = s; contact = c; 
    }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getContact() { return contact; }
}

class Patient implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, name, gender, contact;
    private int age;
    public Patient(String id, String n, int a, String g, String c) { 
        this.id = id; name = n; age = a; gender = g; contact = c; 
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getContact() { return contact; }
}

class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, patientId, doctorUsername, date, time;
    public Appointment(String id, String pid, String du, String d, String t) { 
        this.id = id; patientId = pid; doctorUsername = du; date = d; time = t; 
    }
    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorUsername() { return doctorUsername; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}

// ==================== DATA STORE ====================
class SimpleDataStore {
    static java.util.List<Admin> admins = new java.util.ArrayList<>();
    static java.util.List<Doctor> doctors = new java.util.ArrayList<>();
    static java.util.List<Patient> patients = new java.util.ArrayList<>();
    static java.util.List<Appointment> appointments = new java.util.ArrayList<>();
    
    static {
        admins.add(new Admin("admin", "admin123"));
    }
    
    static String newPatientId() { return "P" + (patients.size() + 1); }
    static String newAppointmentId() { return "A" + (appointments.size() + 1); }
    
    static Patient findPatientById(String id) { 
        for(Patient p : patients) if(p.getId().equals(id)) return p; 
        return null; 
    }
    
    static Doctor findDoctorByUsername(String u) { 
        for(Doctor d : doctors) if(d.getUsername().equals(u)) return d; 
        return null; 
    }
}

// ==================== FULL SCREEN GUI ====================
class FullScreenLogin extends JFrame {
    
    public FullScreenLogin() {
        setTitle("🏥 Hospital Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // FULL SCREEN
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));
        
        // Main Container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel title = new JLabel("🏥 HOSPITAL SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(title, gbc);
        
        // Subtitle
        JLabel subtitle = new JLabel("Please Login to Continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        mainPanel.add(subtitle, gbc);
        
        // Role Selection
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(roleLabel, gbc);
        
        String[] roles = {"Admin", "Doctor"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleCombo.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(roleCombo, gbc);
        
        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(userLabel, gbc);
        
        JTextField userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userField.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(userField, gbc);
        
        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(passLabel, gbc);
        
        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passField.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passField, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        JButton loginBtn = createColorfulButton("LOGIN", new Color(76, 175, 80));
        loginBtn.setPreferredSize(new Dimension(120, 45));
        
        JButton exitBtn = createColorfulButton("EXIT", new Color(244, 67, 54));
        exitBtn.setPreferredSize(new Dimension(120, 45));
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(exitBtn);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        // Event Listeners
        loginBtn.addActionListener(e -> {
            String role = (String)roleCombo.getSelectedItem();
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());
            
            if(role.equals("Admin")) {
                if(user.equals("admin") && pass.equals("admin123")) {
                    new FullScreenAdminDashboard();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid admin credentials!");
                }
            } else {
                Doctor d = SimpleDataStore.findDoctorByUsername(user);
                if(d != null && d.checkPassword(pass)) {
                    JOptionPane.showMessageDialog(this, "Doctor login successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid doctor credentials!");
                }
            }
        });
        
        exitBtn.addActionListener(e -> System.exit(0));
        
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private JButton createColorfulButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
                button.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
            }
        });
        
        return button;
    }
}

class FullScreenAdminDashboard extends JFrame {
    
    public FullScreenAdminDashboard() {
        setTitle("Admin Dashboard - Hospital System");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // FULL SCREEN
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        
        // Main Panel with BorderLayout
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 25, 112));
        headerPanel.setPreferredSize(new Dimension(100, 80));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel headerTitle = new JLabel("👨‍💼 ADMIN DASHBOARD", SwingConstants.CENTER);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerTitle.setForeground(Color.WHITE);
        headerPanel.add(headerTitle, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel - Grid of colorful buttons
        JPanel mainPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Define button texts and colors
        String[] buttonTexts = {
            "ADD PATIENT", 
            "VIEW PATIENTS", 
            "ADD DOCTOR",
            "VIEW DOCTORS", 
            "BOOK APPOINTMENT", 
            "VIEW APPOINTMENTS",
            "PATIENT REPORTS", 
            "DOCTOR SCHEDULE", 
            "LOGOUT"
        };
        
        Color[] buttonColors = {
            new Color(33, 150, 243),   // Blue
            new Color(76, 175, 80),    // Green
            new Color(156, 39, 176),   // Purple
            new Color(255, 152, 0),    // Orange
            new Color(0, 188, 212),    // Cyan
            new Color(233, 30, 99),    // Pink
            new Color(121, 85, 72),    // Brown
            new Color(96, 125, 139),   // Blue Gray
            new Color(244, 67, 54)     // Red
        };
        
        // Create and add buttons
        for(int i = 0; i < buttonTexts.length; i++) {
            JPanel buttonContainer = new JPanel(new GridBagLayout());
            buttonContainer.setBackground(new Color(245, 245, 245));
            
            JButton button = createDashboardButton(buttonTexts[i], buttonColors[i]);
            button.setPreferredSize(new Dimension(200, 80));
            
            final String action = buttonTexts[i];
            button.addActionListener(e -> handleButtonClick(action));
            
            buttonContainer.add(button);
            mainPanel.add(buttonContainer);
        }
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setPreferredSize(new Dimension(100, 60));
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel footerLabel = new JLabel("© 2025 Hospital Management System | Developed with ❤️");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footerLabel.setForeground(new Color(100, 100, 100));
        footerPanel.add(footerLabel);
        
        add(footerPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private JButton createDashboardButton(String text, Color color) {
        JButton button = new JButton("<html><center>" + text.replace(" ", "<br>") + "</center></html>");
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 3D hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.darker().darker(), 3),
                    BorderFactory.createEmptyBorder(20, 10, 20, 10)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.darker(), 2),
                    BorderFactory.createEmptyBorder(20, 10, 20, 10)
                ));
            }
        });
        
        return button;
    }
    
    private void handleButtonClick(String action) {
        switch(action) {
            case "ADD PATIENT":
                showAddPatientDialog();
                break;
            case "VIEW PATIENTS":
                showPatientsFrame();
                break;
            case "ADD DOCTOR":
                showAddDoctorDialog();
                break;
            case "VIEW DOCTORS":
                showDoctorsFrame();
                break;
            case "BOOK APPOINTMENT":
                showAppointmentFrame();
                break;
            case "VIEW APPOINTMENTS":
                showAppointmentsFrame();
                break;
            case "PATIENT REPORTS":
                JOptionPane.showMessageDialog(this, "Patient Reports Feature");
                break;
            case "DOCTOR SCHEDULE":
                JOptionPane.showMessageDialog(this, "Doctor Schedule Feature");
                break;
            case "LOGOUT":
                new FullScreenLogin();
                dispose();
                break;
        }
    }
    
    private void showAddPatientDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField contactField = new JTextField();
        
        panel.add(createStyledLabel("Name:"));
        panel.add(nameField);
        panel.add(createStyledLabel("Age:"));
        panel.add(ageField);
        panel.add(createStyledLabel("Gender:"));
        panel.add(genderField);
        panel.add(createStyledLabel("Contact:"));
        panel.add(contactField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "➕ Add New Patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if(result == JOptionPane.OK_OPTION) {
            try {
                Patient p = new Patient(
                    SimpleDataStore.newPatientId(),
                    nameField.getText().trim(),
                    Integer.parseInt(ageField.getText().trim()),
                    genderField.getText().trim(),
                    contactField.getText().trim()
                );
                SimpleDataStore.patients.add(p);
                JOptionPane.showMessageDialog(this, "✅ Patient added successfully!");
            } catch(Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Invalid input!");
            }
        }
    }
    
    private void showPatientsFrame() {
        JFrame frame = new JFrame("👥 All Patients");
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Name", "Age", "Gender", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        
        // Add data
        for(Patient p : SimpleDataStore.patients) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getAge(), p.getGender(), p.getContact()});
        }
        
        // Style the table
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(33, 150, 243));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton closeBtn = createColorfulButton("CLOSE", new Color(244, 67, 54));
        closeBtn.setPreferredSize(new Dimension(120, 40));
        closeBtn.addActionListener(e -> frame.dispose());
        buttonPanel.add(closeBtn);
        
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    private void showAddDoctorDialog() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JTextField userField = new JTextField();
        JTextField passField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField specField = new JTextField();
        JTextField contactField = new JTextField();
        
        panel.add(createStyledLabel("Username:"));
        panel.add(userField);
        panel.add(createStyledLabel("Password:"));
        panel.add(passField);
        panel.add(createStyledLabel("Name:"));
        panel.add(nameField);
        panel.add(createStyledLabel("Specialization:"));
        panel.add(specField);
        panel.add(createStyledLabel("Contact:"));
        panel.add(contactField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "👨‍⚕️ Add New Doctor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if(result == JOptionPane.OK_OPTION) {
            Doctor d = new Doctor(
                userField.getText().trim(),
                passField.getText().trim(),
                nameField.getText().trim(),
                specField.getText().trim(),
                contactField.getText().trim()
            );
            SimpleDataStore.doctors.add(d);
            JOptionPane.showMessageDialog(this, "✅ Doctor added successfully!");
        }
    }
    
    private void showDoctorsFrame() {
        JFrame frame = new JFrame("👨‍⚕️ All Doctors");
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        
        String[] columns = {"Username", "Name", "Specialization", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        
        // Add data
        for(Doctor d : SimpleDataStore.doctors) {
            model.addRow(new Object[]{d.getUsername(), d.getName(), d.getSpecialization(), d.getContact()});
        }
        
        // Style the table
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(156, 39, 176));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton closeBtn = createColorfulButton("CLOSE", new Color(244, 67, 54));
        closeBtn.setPreferredSize(new Dimension(120, 40));
        closeBtn.addActionListener(e -> frame.dispose());
        buttonPanel.add(closeBtn);
        
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    private void showAppointmentFrame() {
        JFrame frame = new JFrame("📅 Book Appointment");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Patient dropdown
        JComboBox<String> patientCombo = new JComboBox<>();
        for(Patient p : SimpleDataStore.patients) {
            patientCombo.addItem(p.getId() + " - " + p.getName());
        }
        patientCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        patientCombo.setPreferredSize(new Dimension(300, 35));
        
        // Doctor dropdown
        JComboBox<String> doctorCombo = new JComboBox<>();
        for(Doctor d : SimpleDataStore.doctors) {
            doctorCombo.addItem(d.getUsername() + " - " + d.getName());
        }
        doctorCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        doctorCombo.setPreferredSize(new Dimension(300, 35));
        
        JTextField dateField = new JTextField();
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setPreferredSize(new Dimension(300, 35));
        
        JTextField timeField = new JTextField();
        timeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeField.setPreferredSize(new Dimension(300, 35));
        
        // Labels and fields
        addLabelAndField(frame, "Patient:", patientCombo, gbc, 0);
        addLabelAndField(frame, "Doctor:", doctorCombo, gbc, 1);
        addLabelAndField(frame, "Date (YYYY-MM-DD):", dateField, gbc, 2);
        addLabelAndField(frame, "Time (HH:MM):", timeField, gbc, 3);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        JButton bookBtn = createColorfulButton("BOOK", new Color(76, 175, 80));
        bookBtn.setPreferredSize(new Dimension(120, 45));
        
        JButton cancelBtn = createColorfulButton("CANCEL", new Color(244, 67, 54));
        cancelBtn.setPreferredSize(new Dimension(120, 45));
        
        bookBtn.addActionListener(e -> {
            if(patientCombo.getItemCount() == 0 || doctorCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(frame, "No patients or doctors available");
                return;
            }
            
            String pid = ((String)patientCombo.getSelectedItem()).split(" - ")[0];
            String du = ((String)doctorCombo.getSelectedItem()).split(" - ")[0];
            
            Appointment a = new Appointment(
                SimpleDataStore.newAppointmentId(),
                pid,
                du,
                dateField.getText().trim(),
                timeField.getText().trim()
            );
            
            SimpleDataStore.appointments.add(a);
            JOptionPane.showMessageDialog(frame, "✅ Appointment booked successfully!");
            frame.dispose();
        });
        
        cancelBtn.addActionListener(e -> frame.dispose());
        
        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(buttonPanel, gbc);
        
        frame.setVisible(true);
    }
    
    private void addLabelAndField(JFrame frame, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        frame.add(field, gbc);
    }
    
    private void showAppointmentsFrame() {
        JFrame frame = new JFrame("📋 All Appointments");
        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Patient", "Doctor", "Date", "Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        
        // Add data
        for(Appointment a : SimpleDataStore.appointments) {
            Patient p = SimpleDataStore.findPatientById(a.getPatientId());
            Doctor d = SimpleDataStore.findDoctorByUsername(a.getDoctorUsername());
            
            model.addRow(new Object[]{
                a.getId(),
                p != null ? p.getName() : "Unknown",
                d != null ? d.getName() : "Unknown",
                a.getDate(),
                a.getTime()
            });
        }
        
        // Style the table
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(255, 152, 0));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton closeBtn = createColorfulButton("CLOSE", new Color(244, 67, 54));
        closeBtn.setPreferredSize(new Dimension(120, 40));
        closeBtn.addActionListener(e -> frame.dispose());
        buttonPanel.add(closeBtn);
        
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    private JButton createColorfulButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
}

// ==================== MAIN ====================
public class Healthcare{
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new FullScreenLogin());
    }
}
