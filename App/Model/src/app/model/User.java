package app.model;

import java.io.Serializable;

public class User implements Serializable {
    
    // ATRIBUTS
    private int id;             // Identificador numéric de l'usuari, OBLIGATORI
    private String username;       // Nom del perfil d'usuari al login, OBLIGATORI
    private String password;    // Contrasenya de l'usuari, OBLIGATORIA
    private String realname;        // Nom real de l'usuari, OBLIGATORI
    private String surname1;    // Primer cognom de l'usuari, OBLIGATORI
    private String surname2;    // Segon cognom de l'usuari, opcional
    private UserType type;      // Tipus d'usuari. OBLIGATORI. Pot ser user, admin o worker
    
    // MÉTODES

    /**
     * retorna el nom complet d'un autor, composat per realname + surname1 + surname 2
     * @return
     */
    
    public String getFullName() {
        String fullName = getRealname();
        if (getSurname1() != null) {
        fullName += " " + getSurname1();
    }
        if (getSurname2() != null) {
            fullName += " " + getSurname2();
        }
        return fullName;
    }
    
    /**
     * Converteix string a l'usertype equivalent
     * @param txt ha de ser igual a "USER" "ADMIN" o "WORKER"
     * @return
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

    /**
     *
     * @param id
     * @param username
     * @param password
     * @param name
     * @param surname1
     * @param surname2
     * @param type
     */
    public User(int id, String username, String password, String name, String surname1, String surname2, UserType type) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setRealname(name);
        setSurname1(surname1);
        setSurname2(surname2);
        setType(type);
    }

    /**
     *
     * @param id
     * @param username
     * @param password
     * @param name
     * @param surname1
     * @param surname2
     * @param type
     */
    public User(int id, String username, String password, String name, String surname1, String surname2, String type) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setRealname(name);
        setSurname1(surname1);
        setSurname2(surname2);
        setType(stringToUserType(type));
    }

    /**
     *
     * @param id
     * @param username
     * @param password
     * @param name
     * @param surname1
     * @param type
     */
    public User(int id, String username, String password, String name, String surname1, UserType type) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setRealname(name);
        setSurname1(surname1);
        setType(type);
    }

    /**
     *
     * @param id
     * @param username
     * @param password
     * @param name
     * @param surname1
     * @param type
     */
    public User(int id, String username, String password, String name, String surname1, String type) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setRealname(name);
        setSurname1(surname1);
        setType(stringToUserType(type));
    }
    
    /**
     *
     * @return
     */
    public String getTypeAsString() {
         if (type == UserType.ADMIN) return "ADMIN";
         else if (type == UserType.USER) return "USER";
         else if (type == UserType.WORKER) return "WORKER";
         else throw new ModelException("UserType incorrecte");
    }
    // GETTERS

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return
     */
    public String getRealname() {
        return realname;
    }

    /**
     *
     * @return
     */
    public String getSurname1() {
        return surname1;
    }

    /**
     *
     * @return
     */
    public String getSurname2() {
        return surname2;
    }

    /**
     *
     * @return
     */
    public UserType getType() {
        return type;
    }

    // SETTERS

    /**
     *
     * @param id
     */
    public void setId(int id) {
        // ID ha de ser positiu , i únic
        if (id < 0) throw new ModelException("ERROR: ID ha de ser positiu");
        this.id = id;
    }

    /**
     *
     * @param alias
     */
    public void setUsername(String alias) {
        // Alias no pot ser null o buit
        if (alias == null) throw new ModelException("ERROR: Username no pot ser nul");
        else if (alias.isBlank()) throw new ModelException("ERROR: Username no pot estar buit");
        this.username = alias;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        // Contrasenya no pot ser null o buit
        if (password == null) throw new ModelException("ERROR: password no pot ser nul");
        else if (password.isBlank()) throw new ModelException("ERROR: password no pot estar buit");
        this.password = password;
    }

    /**
     *
     * @param name
     */
    public void setRealname(String name) {
        // name no pot ser null o buit
        if (name == null) throw new ModelException("ERROR: name no pot ser nul");
        else if (name.isBlank()) throw new ModelException("ERROR: name no pot estar buit");
        this.realname = name;
    }

    /**
     *
     * @param surname1
     */
    public void setSurname1(String surname1) {
        // surname1 no pot ser null o buit
        if (surname1 == null) throw new ModelException("ERROR: surname1 no pot ser nul");
        else if (surname1.isBlank()) throw new ModelException("ERROR: surname1 no pot estar buit");
        this.surname1 = surname1;
    }

    /**
     *
     * @param surname2
     */
    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    /**
     *
     * @param type
     */
    public void setType(UserType type) {
        // type no pot ser null
        if (type == null) throw new ModelException("ERROR : type no pot ser nul");
        this.type = type;
    }
    
    
}
