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
public class TableItem {
    private String text;
    private int bewertung;

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
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
