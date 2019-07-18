/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;


import SOURCES.Interface.InterfaceAyantDroit;
import java.util.Vector;
import SOURCES.Interface.InterfaceEleve;
import SOURCES.Interface.InterfacePaiement;

/**
 *
 * @author HP Pavilion
 */
public class DonneesLitige {
    private Vector<InterfaceEleve> eleves;
    private Vector<InterfaceAyantDroit> listeAyantDroits;
    private Vector<InterfacePaiement> listePaiements;

    public DonneesLitige(Vector<InterfaceEleve> eleves, Vector<InterfaceAyantDroit> listeAyantDroits, Vector<InterfacePaiement> listePaiements) {
        this.eleves = eleves;
        this.listeAyantDroits = listeAyantDroits;
        this.listePaiements = listePaiements;
    }

    public Vector<InterfaceEleve> getEleves() {
        return eleves;
    }

    public void setEleves(Vector<InterfaceEleve> eleves) {
        this.eleves = eleves;
    }

    public Vector<InterfaceAyantDroit> getListeAyantDroits() {
        return listeAyantDroits;
    }

    public void setListeAyantDroits(Vector<InterfaceAyantDroit> listeAyantDroits) {
        this.listeAyantDroits = listeAyantDroits;
    }

    public Vector<InterfacePaiement> getListePaiements(int idArticle) {
        if(idArticle == -1){
            return listePaiements;
        }else{
            for(InterfacePaiement Ipaie: listePaiements){
                if(idArticle == Ipaie.getIdArticle()){
                    Vector<InterfacePaiement> listeFiltree = new Vector<>();
                    listeFiltree.add(Ipaie);
                    return listeFiltree;
                }
            }
        }
        return new Vector<>();
    }

    public void setListePaiements(Vector<InterfacePaiement> listePaiements) {
        this.listePaiements = listePaiements;
    }

    @Override
    public String toString() {
        return "DonneesLitige{" + "eleves=" + eleves + ", listeAyantDroits=" + listeAyantDroits + ", listePaiements=" + listePaiements + '}';
    }
}
