package mbds;

public class Person {

    private String nom;
    private String prenom;
    private String contact;

    public Person(String nom, String prenom){
        this.nom=nom;
        this.prenom=prenom;
        this.contact=contact;
    }

    public String getNom(){
        return this.nom+"   "+this.prenom;
    }

    public String getContact(){
        return this.contact;
    }
}

