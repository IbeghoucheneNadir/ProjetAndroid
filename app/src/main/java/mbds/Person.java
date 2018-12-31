package mbds;

class Person {

    private String nom;
    private String password = "";

    public Person(String nom, String password){
        this(nom);
        this.password = password;
    }

    public Person(String nom){
        this.nom=nom;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    public String getNom(){ return this.nom; }

}

