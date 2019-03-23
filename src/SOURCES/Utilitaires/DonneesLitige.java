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
    private Vector<InterfaceAyantDroit> ayantDroits;
    private Vector<InterfacePaiement> paiements;

    public DonneesLitige(Vector<InterfaceEleve> eleves, Vector<InterfaceAyantDroit> ayantDroits, Vector<InterfacePaiement> paiements) {
        this.eleves = eleves;
        this.ayantDroits = ayantDroits;
        this.paiements = paiements;
    }

    public Vector<InterfaceEleve> getEleves() {
        return eleves;
    }

    public void setEleves(Vector<InterfaceEleve> eleves) {
        this.eleves = eleves;
    }

    public Vector<InterfaceAyantDroit> getAyantDroits() {
        return ayantDroits;
    }

    public void setAyantDroits(Vector<InterfaceAyantDroit> ayantDroits) {
        this.ayantDroits = ayantDroits;
    }

    public Vector<InterfacePaiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(Vector<InterfacePaiement> paiements) {
        this.paiements = paiements;
    }

    @Override
    public String toString() {
        return "DonneesLitige{" + "eleves=" + eleves + ", ayantDroits=" + ayantDroits + ", paiements=" + paiements + '}';
    }
}
