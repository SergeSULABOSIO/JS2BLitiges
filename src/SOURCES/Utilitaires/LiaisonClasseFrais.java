/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceClasse;



/**
 *
 * @author HP Pavilion
 */
public class LiaisonClasseFrais {
    private InterfaceClasse classe;
    private double montant;

    public LiaisonClasseFrais(InterfaceClasse classe, double montant) {
        this.classe = classe;
        this.montant = montant;
    }

    public InterfaceClasse getClasse() {
        return classe;
    }

    public void setClasse(InterfaceClasse classe) {
        this.classe = classe;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "Liaison{" + "classe=" + classe + ", montant=" + montant + '}';
    }
    
    
}
