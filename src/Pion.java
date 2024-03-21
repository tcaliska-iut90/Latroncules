package src;

public class Pion {
    private char couleur;
    private int posX;
    private int posY;

    public int getPosX(){
        return this.posX;
    }

    public int getPosY(){
        return this.posY;
    }

    public char getCouleur(){
        return this.couleur;
    }

    public Pion(char coul,int X, int Y){
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
