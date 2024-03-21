package src;

import java.util.Random;

public class Grille {

    private Pion[][] grid = new Pion[8][8];
    Random r = new Random();

    public int tirageJoueur(){
        return r.nextInt(2);
    }

    public Joueur joueur(){
        int numeroJoueur = tirageJoueur();
        String couleurJoueur = (numeroJoueur == 0) ? "Rouge" : "Bleu";
        return new Joueur(couleurJoueur);
    }
}