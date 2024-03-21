package src;
import java.util.List;

public class Joueur {
    public String couleur;
    public String nom;
    public String prenom;


    public Joueur(String c_couleur) {
        this.couleur = c_couleur;
    }

    public Joueur(String c_couleur, String n_nom, String p_prenom) {
        this.couleur = c_couleur;
        this.nom = n_nom;
        this.prenom = p_prenom;
    }

    public void setNom(String n_nom){
        this.nom = n_nom;
    }

    public void setPrenom(String p_prenom){
        this.prenom = p_prenom;
    }

    public void setCouleur(String c_couleur){
        this.couleur = c_couleur;
    }

}