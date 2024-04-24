import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ContactManagerUI extends JFrame {
    private HashMap<String, Contact> contacts;
    private JTextArea contactTextArea;
    private JTextField nameField, phoneField, emailField;

    public ContactManagerUI() {
        super("Contact Manager");
        contacts = new HashMap<>();
        loadContactsFromFile("contacts.txt");
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        JButton viewButton = new JButton("View Contacts");
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewContacts();
            }
        });

        JButton editButton = new JButton("Edit Contact");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editContact();
            }
        });

        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        contactTextArea = new JTextArea(20, 30);
        contactTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(contactTextArea);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
            contacts.put(name, new Contact(name, phone, email));
            saveContactsToFile("contacts.txt");
            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
            JOptionPane.showMessageDialog(this, "Contact added successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
        }
    }

    private void viewContacts() {
        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No contacts found!");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Contact contact : contacts.values()) {
                sb.append(contact.toString()).append("\n");
            }
            contactTextArea.setText(sb.toString());
        }
    }

    private void editContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        if (!name.isEmpty() && contacts.containsKey(name)) {
            Contact contact = contacts.get(name);
            if (!phone.isEmpty()) {
                contact.setPhone(phone);
            }
            if (!email.isEmpty()) {
                contact.setEmail(email);
            }
            saveContactsToFile("contacts.txt");
            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
            JOptionPane.showMessageDialog(this, "Contact edited successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Contact not found!");
        }
    }

    private void deleteContact() {
        String name = nameField.getText();
        if (!name.isEmpty() && contacts.containsKey(name)) {
            contacts.remove(name);
            saveContactsToFile("contacts.txt");
            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
            JOptionPane.showMessageDialog(this, "Contact deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Contact not found!");
        }
    }

    private void loadContactsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    String phone = parts[1].trim();
                    String email = parts[2].trim();
                    contacts.put(name, new Contact(name, phone, email));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveContactsToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Contact contact : contacts.values()) {
                pw.println(contact.getName() + "," + contact.getPhone() + "," + contact.getEmail());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ContactManagerUI();
            }
        });
    }
}

class Contact {
    private String name;
    private String phone;
    private String email;

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "Name: " + name + ", Phone: " + phone + ", Email: " + email;
    }
}
