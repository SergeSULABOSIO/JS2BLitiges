/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

/**
 *
 * @author HP Pavilion
 */
public class LiaisonEleveFrais {
    private long signatureEleve;
    private int idFrais;
    private double montant;
    private int idMonnaie;
    private String monnaie;

    public LiaisonEleveFrais(long signatureEleve, int idFrais, double montant, int idMonnaie, String monnaie) {
        this.signatureEleve = signatureEleve;
        this.idFrais = idFrais;
        this.montant = montant;
        this.idMonnaie = idMonnaie;
        this.monnaie = monnaie;
    }

    
    public long getSignatureEleve() {
        return signatureEleve;
    }

    public void setSignatureEleve(long signatureEleve) {
        this.signatureEleve = signatureEleve;
    }

    public int getIdFrais() {
        return idFrais;
    }

    public void setIdFrais(int idFrais) {
        this.idFrais = idFrais;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getIdMonnaie() {
        return idMonnaie;
    }

    public void setIdMonnaie(int idMonnaie) {
        this.idMonnaie = idMonnaie;
    }

    public String getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }
    
    

    @Override
    public String toString() {
        return "LiaisonEleveFrais{" + "signatureEleve=" + signatureEleve + ", idFrais=" + idFrais + ", montant=" + montant + ", idMonnaie=" + idMonnaie + ", monnaie=" + monnaie + '}';
    }

    
}
