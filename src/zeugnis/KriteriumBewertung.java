/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

/**
 *
 * @author internet
 */
public class KriteriumBewertung {
    private int idKriterium;
    private int bewertung;

    public KriteriumBewertung(int id, int bew){
        idKriterium = id;
        bewertung   = bew;
    }
    public KriteriumBewertung(){
        idKriterium = 0;
        bewertung   = 0;
    }
    
    /**
     * @return the idKriterium
     */
    public int getIdKriterium() {
        return idKriterium;
    }

    /**
     * @param idKriterium the idKriterium to set
     */
    public void setIdKriterium(int idKriterium) {
        this.idKriterium = idKriterium;
    }

    /**
     * @return the bewertung
     */
    public int getBewertung() {
        return bewertung;
    }

    /**
     * @param bewertung the bewertung to set
     */
    public void setBewertung(int bewertung) {
        this.bewertung = bewertung;
    }
}
