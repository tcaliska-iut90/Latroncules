package src;

public class Pion {
    private char couleur;
    private int posX;
    private int posX;
    private char[] mouvement = new char[8];

    public char[] getMouvement() {
        return mouvement;
    }

    public void setMouvement(char[] mouvement) {
        this.mouvement = mouvement;
    }
=======
    private int posY;
>>>>>>> origin/main

    public int getPosX(){
        return this.posX;
    }

    public int getPosY(){
        return this.posY;
    }

    public char getCouleur(){
        return this.couleur;
    }

    public void setCouleur(char couleur) {
        this.couleur = couleur;
    }

    public Pion(char coul, int X, int Y){
        this.couleur = coul;
        this.posX = X;
        this.posY = Y;
    }

    public void setPos(int x, int y){
        this.posX = X;
        this.posY = Y;
    }
    public void setPosX(int x){
        this.posX = X;
    }

    public void setPosY(int y){
        this.posY = y;
    }

}
