package app.model;

public class User {
    
    // ATRIBUTS
    private int id;             // Identificador numéric de l'usuari, OBLIGATORI
    private String username;       // Nom del perfil d'usuari al login, OBLIGATORI
    private String password;    // Contrasenya de l'usuari, OBLIGATORIA
    private String realname;        // Nom real de l'usuari, OBLIGATORI
    private String surname1;    // Primer cognom de l'usuari, OBLIGATORI
    private String surname2;    // Segon cognom de l'usuari, opcional
    private UserType type;      // Tipus d'usuari. OBLIGATORI. Pot ser user, admin o worker
    
    // MÉTODES
    
    public String getFullName() {
        String fullName = getRealname() + " " + getSurname1();
        if (getSurname2() != null) {
            fullName += " " + getSurname2();
        }
        return fullName;
    }
    /*
    Converteix string txt a UserType
    */
    public static UserType stringToUserType(String txt) {
        if (txt.isBlank()) throw new ModelException("Error: no es pot convertir l'string a usertype");
        switch(txt) {
            case "USER": return UserType.USER;
            case "ADMIN": return UserType.ADMIN;
            case "WORKER": return UserType.WORKER;
        }
        throw new ModelException("Error: no es pot convertir l'string a usertype");
    }
    
    // CONSTRUCTORS
    public User(int id, String username, String password, String name, String surname1, String surname2, UserType type) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setRealname(name);
        setSurname1(surname1);
        setSurname2(surname2);
        setType(type);
    }
    public User(int id, String username, String password, String name, String surname1, String surname2, String type) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setRealname(name);
        setSurname1(surname1);
        setSurname2(surname2);
        setType(stringToUserType(type));
    }

    public User(int id, String username, String password, String name, String surname1, UserType type) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setRealname(name);
        setSurname1(surname1);
        setType(type);
    }
    public User(int id, String username, String password, String name, String surname1, String type) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setRealname(name);
        setSurname1(surname1);
        setType(stringToUserType(type));
    }
    
    // GETTERS
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRealname() {
        return realname;
    }

    public String getSurname1() {
        return surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public UserType getType() {
        return type;
    }

    // SETTERS
    public void setId(int id) {
        // ID ha de ser positiu , i únic
        if (id < 0) throw new ModelException("ERROR: ID ha de ser positiu");
        this.id = id;
    }

    public void setUsername(String alias) {
        // Alias no pot ser null o buit
        if (alias == null) throw new ModelException("ERROR: Alies no pot ser nul");
        else if (alias.isBlank()) throw new ModelException("ERROR: Alies no pot estar buit");
        this.username = alias;
    }

    public void setPassword(String password) {
        // Contrasenya no pot ser null o buit
        if (password == null) throw new ModelException("ERROR: password no pot ser nul");
        else if (password.isBlank()) throw new ModelException("ERROR: password no pot estar buit");
        this.password = password;
    }

    public void setRealname(String name) {
        // name no pot ser null o buit
        if (name == null) throw new ModelException("ERROR: name no pot ser nul");
        else if (name.isBlank()) throw new ModelException("ERROR: name no pot estar buit");
        this.realname = name;
    }

    public void setSurname1(String surname1) {
        // surname1 no pot ser null o buit
        if (surname1 == null) throw new ModelException("ERROR: surname1 no pot ser nul");
        else if (surname1.isBlank()) throw new ModelException("ERROR: surname1 no pot estar buit");
        this.surname1 = surname1;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public void setType(UserType type) {
        // type no pot ser null
        if (type == null) throw new ModelException("ERROR : type no pot ser nul");
        this.type = type;
    }
    
    
}
