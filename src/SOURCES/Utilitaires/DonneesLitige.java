/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;


import java.util.Vector;
import Source.Objet.Ayantdroit;
import Source.Objet.Eleve;
import Source.Objet.Paiement;

/**
 *
 * @author HP Pavilion
 */
public class DonneesLitige {
    private Vector<Eleve> listeEleves;
    private Vector<Ayantdroit> listeAyantDroits;
    private Vector<Paiement> listePaiements;

    public DonneesLitige(Vector<Eleve> listeEleves, Vector<Ayantdroit> listeAyantDroits, Vector<Paiement> listePaiements) {
        this.listeEleves = listeEleves;
        this.listeAyantDroits = listeAyantDroits;
        this.listePaiements = listePaiements;
    }

    

    public Vector<Paiement> getListePaiements(int idArticle) {
        if(idArticle == -1){
            return listePaiements;
        }else{
            for(Paiement Ipaie: listePaiements){
                if(idArticle == Ipaie.getIdFrais()){
                    Vector<Paiement> listeFiltree = new Vector<>();
                    listeFiltree.add(Ipaie);
                    return listeFiltree;
                }
            }
        }
        return new Vector<>();
    }

    public Vector<Eleve> getListeEleves() {
        return listeEleves;
    }

    public void setListeEleves(Vector<Eleve> listeEleves) {
        this.listeEleves = listeEleves;
    }


    public Vector<Ayantdroit> getListeAyantDroits() {
        return listeAyantDroits;
    }

    public void setListeAyantDroits(Vector<Ayantdroit> listeAyantDroits) {
        this.listeAyantDroits = listeAyantDroits;
    }

    public Vector<Paiement> getListePaiements() {
        return listePaiements;
    }

    public void setListePaiements(Vector<Paiement> listePaiements) {
        this.listePaiements = listePaiements;
    }

    @Override
    public String toString() {
        return "DonneesLitige{" + "listeEleves=" + listeEleves + ", listeAyantDroits=" + listeAyantDroits + ", listePaiements=" + listePaiements + '}';
    }

    
}
