//Importerar alla nödvändiga klasser från respektive toolbox.
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

// Huvudklassen för applikationen som implementerar gränssnittet UsernameChecker.
public class ClassApp implements UsernameChecker {
    // Deklaration av GUI-komponenter.
    private JFrame frame;
    private JPanel panel;
    private JTextField userName;
    private JTextField email;
    private JTextField firstName;
    private JTextField lastName;
    private JTextField semester;
    private JButton save;
    private JButton showAll;
    private JButton editButton;
    private JButton quitButton;
    private JList<String> listOutPut;
    private JScrollPane scrollPane;
    private List<Student> studentList;

    // Metod som kontrollerar om ett användarnamn är unikt i studentList.
    @Override
    public boolean usernameIsUnique(String username) {
        for (Student student : studentList) {
            if (student.getUserName().equals(username)) {
                return false;
            }
        }
        return true;
    }
    DefaultListModel<String> outPutContainer = new DefaultListModel<>();
    //metod för att kontrollera textfälten efter symboler som ej förekommer i namn.
    private boolean fieldsAreValid() {
        String fName = firstName.getText();
        String lName = lastName.getText();
        String regex = "(?i)(?:[a-z'-]*[a-z]){2,}[a-z'-]*";

        return fName.matches(regex) && lName.matches(regex);
    }
    private void redigeraButtonUpdater() {
        int selectedIndex = listOutPut.getSelectedIndex();
        boolean isSelected = selectedIndex != -1;
        editButton.setEnabled(isSelected && fieldsAreValid());
    }
    private void addValidationListeners() {
        DocumentListener documentListener = new DocumentListener() {
            public void update(DocumentEvent e) {
                save.setEnabled(fieldsAreValid());
                redigeraButtonUpdater();
            }

            @Override
            public void insertUpdate(DocumentEvent e) { update(e); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(e); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(e); }
        };

        firstName.getDocument().addDocumentListener(documentListener);
        lastName.getDocument().addDocumentListener(documentListener);
    }

    //Konstruktor för ClassApp-klassen
    public ClassApp() {
        addValidationListeners();
        frame = new JFrame("ClassApp");
        frame.setVisible(true);
        frame.add(panel);
        frame.pack();
        // Inaktiverar knappar initialt tills villkor är uppfyllda.
        editButton.setEnabled(false);
        save.setEnabled(false);
        showAll.setEnabled(false);

        // Initierar studentList som en ny ArrayList.
        studentList = new ArrayList<>();

        if (fieldsAreValid()) {
            save.setToolTipText("Generera en användare");
            editButton.setToolTipText("Redigera förnamn och/eller efternamn.");
        }
        else {
            save.setToolTipText("Ogiltigt namn, ange minst 2 bokstäver");
            editButton.setToolTipText("Ogiltigt namn, ange minst 2 bokstäver");
        }
        //ActionListener till save knappen.
        save.addActionListener(new ActionListener() {
            @Override
            //Metod för när knappen trycks.
            public void actionPerformed(ActionEvent e) {
                //Hämtar texten som användaren matat in i firstName.
                String fName = firstName.getText();
                //Hämtar texten som användaren matat in i lastName.
                String lName = lastName.getText();
                //Skapar ett nytt student objekt med parametrarna fnamn och lnamn.
                Student student = new Student(fName, lName, ClassApp.this);
                //Kontrollerar om listan innehållar mindre än 20 objekt.
                if (studentList.size() < 20) {
                    //Lägger till det nya objektet student i studentList
                    studentList.add(student);
                    // Sätter texten i 'userName'-textfältet till användarnamnet för den nyss skapade studenten.
                    userName.setText(student.getUserName());
                    // Sätter texten i 'email'-textfältet till e-postadressen för den nyss skapade studenten.
                    email.setText(student.getEmail());
                    // Sätter texten i 'termin'-textfältet till terminen för den nyss skapade studenten.
                    semester.setText(student.getSemester()); // Visa terminen i termin-fältet
                    if (studentList.size() == 1){
                        showAll.setEnabled(true);
                    }
                }
                else {
                    save.setEnabled(false);
                    save.setToolTipText("You cannot add more than 20 users.");
                }
            }
        });

        // Lägger till en ActionListener till 'showAll'-knappen.

        showAll.addActionListener(new ActionListener() {
            @Override
            // metod för när en knapp trycks in
            public void actionPerformed(ActionEvent e) {
                refreshList();
            }
        });
        // Lägger till en ListSelectionListener på listOutPut.

        listOutPut.addListSelectionListener(e -> {
            // Kontrollerar om listvalets status justerar (ändras).
            if (!e.getValueIsAdjusting()) {
                redigeraButtonUpdater();
                // Kontrollerar om ett objekt är valt i listan och lagrar detta som en boolean.
                boolean isSelected = listOutPut.getSelectedIndex() != -1;
                // Aktiverar eller inaktiverar 'redigeraButton' beroende på om ett element är valt eller inte.
                editButton.setEnabled(isSelected);
                // Om ett objekt är valt...
                if (isSelected) {
                    // Hämtar det valda Student-objektet från 'studentList' baserat på indexet av det valda objektet i listOutPut.
                    Student selectedStudent = studentList.get(listOutPut.getSelectedIndex());
                    // Sätter texten i 'firstName' textfältet till det valda Student-objektets förnamn.
                    firstName.setText(selectedStudent.getFName());
                    // Sätter texten i 'lastName' textfältet till det valda Student-objektets efternamn.
                    lastName.setText(selectedStudent.getLName());
                }
            }
        });
        // Lägger till en ActionListener på 'editButton'.
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hämtar indexet för det valda objektet i listOutPut.
                int selectedIndex = listOutPut.getSelectedIndex();
                // Kontrollerar om ett objekt faktiskt är valt (dvs. selectedIndex är inte -1).
                if (selectedIndex != -1 ) {
                    // Hämtar det valda Student-objektet från 'studentList' baserat på det valda indexet.
                    Student selectedStudent = studentList.get(selectedIndex);
                    // Uppdaterar det valda Student-objektets förnamn med texten från 'firstName' textfältet.
                    selectedStudent.setFName(firstName.getText());
                    // Uppdaterar det valda Student-objektets efternamn med texten från 'lastName' textfältet.
                    selectedStudent.setLName(lastName.getText());
                    // Uppdaterar representationen av det valda objektet i 'outPutContainer'.
                    outPutContainer.setElementAt(selectedStudent.toString(), selectedIndex);
                    // Anropar en metod för att uppdatera eller "refresha" listan.
                    refreshList();
                }
            }
        });
        // ActionListener för 'avsluta'-knappen.
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }
    // Metod för att uppdatera listan med studentinformation.
    private void refreshList() {
        outPutContainer.clear();
        for (Student student : studentList) {
            // Använder 'toString()' metoden från 'Student'-objektet för att få studentens information.
            outPutContainer.addElement(student.toString());
        }
        listOutPut.setModel(outPutContainer);
    }
    // Huvudmetod för att starta applikationen.
    public static void main(String[] args) {
        new ClassApp();
    }

}
