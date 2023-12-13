import java.util.Calendar;

//Skapar en Student klass
public class Student {

    //Deklarerar 5st Instansvariabler
    private String fName;
    private String lName;
    private String semester;
    private String username;
    private String email;
    private UsernameChecker usernameChecker;
    //Konstruktorn för Student klassen. Den hämtar 3 parametrar. fName, lName och usernameChecker.
    public Student(String fName, String lName, UsernameChecker usernameChecker) {

        //Använder this för att tilldela listan med respektive värden.
        this.fName = formatName(fName);
        this.lName = formatName(lName);
        this.usernameChecker = usernameChecker;
        this.semester = genSemester();
        this.username = genUserName();
        this.email = username + "@du.se";

    }
    // Metoden formatName är utformad för att ta en sträng name och formatera den så att varje ord i strängen börjar med en stor bokstav följt av små bokstäver,
    // oavsett hur inmatningen såg ut från början. Metoden hanterar även specialfall där name är null eller tom och när det finns icke-bokstäver i strängen.
    private String formatName(String name) {
        // Kontrollerar om den inmatade strängen 'name' är null eller tom. Om så är fallet, returnera en tom sträng.
        if (name == null || name.isEmpty()) {
            return "";
        }
        // Skapar en StringBuilder för att bygga den formaterade namnsträngen. Initialstorleken är lika med längden av 'name'.
        StringBuilder formattedName = new StringBuilder(name.length());
        // En boolean-flagga som indikerar om nästa tecken ska vara stort (stor bokstav).
        boolean capitalizeNext = true;
        // Loopar genom varje tecken i strängen 'name'.
        for (char ch : name.toCharArray()) {
            // Kontrollerar om tecknet är en bokstav.
            if (Character.isLetter(ch)) {
                // Om 'capitalizeNext' är true, lägg till tecknet som stor bokstav och sätt 'capitalizeNext' till false.
                if (capitalizeNext) {
                    formattedName.append(Character.toUpperCase(ch));
                    capitalizeNext = false;
                    // Om 'capitalizeNext' är false, lägg till tecknet som liten bokstav.
                } else {
                    formattedName.append(Character.toLowerCase(ch));
                }
            } else {
                // Om tecknet inte är en bokstav, lägg till det som det är och sätt 'capitalizeNext' till true.
                formattedName.append(ch);
                capitalizeNext = true;
            }
        }
        // Konverterar StringBuilder till en sträng och returnerar den formaterade namnsträngen.
        return formattedName.toString();
    }
    // En privat metod som genererar och returnerar en sträng som representerar studentens termin.
    private String genSemester() {
        // Skapar en instans av Calendar-klassen som innehåller nuvarande datum och tid.
        Calendar nu = Calendar.getInstance();
        // Hämtar det aktuella året från Calendar-instansen och använder modulusoperatorn (%) för att få de två sista siffrorna av året.
        // För 2023 blir det då 20 kvot och 23 rest.
        int ar = nu.get(Calendar.YEAR) % 100; // Två sista siffrorna i året
        // Hämtar den aktuella månaden från Calendar-instansen.
        int manad = nu.get(Calendar.MONTH);
        // Returnerar en sträng som representerar terminen.
        // Om månaden är mindre än 6 (dvs. från januari till juni), returneras "vt" (vårtermin) följt av årets två sista siffror.
        // Annars (från juli till december), returneras "ht" (hösttermin) följt av årets två sista siffror.
        return (manad < 6 ? "Vt" : "Ht") + ar;
    }
    // En privat metod som genererar och returnerar ett användarnamn för en student.
    private String genUserName() {
        // Tar bort alla icke-alfabetiska tecken från förnamnet och sparar det i 'cleanFName'.
        String cleanFName = fName.replaceAll("[^a-zA-Z]", "");
        // Tar bort alla icke-alfabetiska tecken från efternamnet och sparar det i 'cleanLName'.
        String cleanLName = lName.replaceAll("[^a-zA-Z]", "");
        // Skapar en bas för användarnamnet genom att kombinera 'semester', de första tre tecknen (eller färre om namnet är kortare) av 'cleanFName',
        // och de första tre tecknen (eller färre om namnet är kortare) av 'cleanLName', konverterar allt till små bokstäver.
        String baseUsername = semester +
                cleanFName.substring(0, Math.min(3, cleanFName.length())) +
                cleanLName.substring(0, Math.min(3, cleanLName.length())).toLowerCase();
        // En loop som säkerställer att den basanvändarnamnet är minst 10 tecken långt.
        // Om det är kortare än 10 tecken, läggs bokstaven 'x' till tills det når 10 tecken.
        while (baseUsername.length() < 10) {
            baseUsername = baseUsername + "x";
        }

        // Anropar 'uniqueUsername' metoden för att generera ett unikt användarnamn baserat på 'baseUsername',
        // 'cleanFName', 'cleanLName' och sammanlagda längden av de tre tecken (eller färre) från både 'cleanFName' och 'cleanLName'.
        return uniqueUsername(baseUsername, cleanFName, cleanLName, Math.min(3, cleanFName.length()) + Math.min(3, cleanLName.length()));

    }
    private String uniqueUsername(String base, String fName, String lName, int index) {
        // Initialiserar 'username' med en bassträng.
        String username = base;
        // Initialiserar en suffixräknare för att använda om namnet inte är unikt.
        int suffix = 0;
        // En while-loop som körs så länge det genererade användarnamnet inte är unikt.
        while (!usernameChecker.usernameIsUnique(username)) {
            // Väljer nästa tecken från förnamnet eller efternamnet beroende på index.
            if (index < fName.length() + lName.length()) {
                // Väljer nästa tecken från förnamnet eller efternamnet beroende på index.
                char nextChar = index < fName.length() ? fName.charAt(index) : lName.charAt(index - fName.length());
                // Bygger en ny användarnamnsträng genom att lägga till nästa tecken till basen.
                username = base.substring(0, base.length() - 1) +nextChar;
                // Ökar indexet för nästa iteration.
                index++;
            } else {
                // Ökar suffixet om indexet är lika med eller större än sammanlagd längd av förnamn och efternamn.
                suffix++;
                // Konverterar suffixet till en sträng.
                String suffixString = Integer.toString(suffix);
                // Bygger en ny användarnamnsträng genom att lägga till suffixet till basen.
                username = base.substring(0, base.length() - suffixString.length()) + suffix;
            }
        }
        // Returnerar det unika användarnamnet.
        return username;
    }

    // Kommentar: '@Override' är en annotation som indikerar att denna metod överskriver en metod från en superklass.
    @Override

    // Metod för att få en strängrepresentation av ett objekt.
    public String toString() {
        // Denna sträng sätts samman genom att använda strängkonkatenering med '+' operatorn.
        return "Namn: " + fName + " " + lName + " Termin: " + semester + " Användarnamn: " + username + " E-Post: " + email;
    }
    //Getters
    public String getFName() {
        return fName;
    }

    public String getLName() {
        return lName;
    }

    public String getSemester() {
        return semester;
    }

    public String getUserName() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    //Setters
    public void setFName(String firstName) {
        this.fName = formatName(firstName);
    }
    public void setLName(String lastName) {
        this.lName = formatName(lastName);
    }
}
