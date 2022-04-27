package BruteF;

public class Koord {
    private Koord child, parent;
    public Koord(Koord parent){
        this.parent=parent;
        this.child=new Koord(this);
    }

    public Koord(){

    }

}
