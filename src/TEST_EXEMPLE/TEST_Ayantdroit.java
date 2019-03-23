/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.Interface.InterfaceAyantDroit;
import SOURCES.Utilitaires.*;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class TEST_Ayantdroit implements InterfaceAyantDroit{
    public int id;
    public int idEntreprise;
    public int idUtilisateur;
    public int idExercice;
    public int idEleve;
    public String eleve;
    public Vector<LiaisonEleveFrais> listeLiaisons;
    public long signature;
    public long signatureEleve;
    public int beta;
    

    public TEST_Ayantdroit(int id, int idEntreprise, int idUtilisateur, int idExercice, int idEleve, String eleve, Vector<LiaisonEleveFrais> listeLiaisons, long signature, long signatureEleve, int beta) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.idUtilisateur = idUtilisateur;
        this.idExercice = idExercice;
        this.idEleve = idEleve;
        this.listeLiaisons = listeLiaisons;
        this.signature = signature;
        this.signatureEleve = signatureEleve;
        this.eleve = eleve;
        this.beta = beta;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }
    
    

    public String getEleve() {
        return eleve;
    }

    public void setEleve(String eleve) {
        this.eleve = eleve;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(int idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
    }

    public int getIdEleve() {
        return idEleve;
    }

    public void setIdEleve(int idEleve) {
        this.idEleve = idEleve;
    }

    public Vector<LiaisonEleveFrais> getListeLiaisons() {
        return listeLiaisons;
    }

    public void setListeLiaisons(Vector<LiaisonEleveFrais> listeLiaisons) {
        this.listeLiaisons = listeLiaisons;
    }

    public long getSignature() {
        return signature;
    }

    public void setSignature(long signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "XX_Ayantdroit{" + "id=" + id + ", idEntreprise=" + idEntreprise + ", idUtilisateur=" + idUtilisateur + ", idExercice=" + idExercice + ", idEleve=" + idEleve + ", eleve=" + eleve + ", listeLiaisons=" + listeLiaisons + ", signature=" + signature + ", signatureEleve=" + signatureEleve + ", beta=" + beta + '}';
    }

    @Override
    public void ajouterLiaisons(LiaisonEleveFrais newLiaison) {
        if(this.listeLiaisons != null){
            this.listeLiaisons.add(newLiaison);
        }
    }

    @Override
    public void viderLiaisons() {
        if(this.listeLiaisons != null){
            this.listeLiaisons.removeAllElements();
        }
    }

    @Override
    public void setLiaisons(int idFrais, double newMontant) {
        if(this.listeLiaisons != null){
            for(LiaisonEleveFrais liaison : listeLiaisons){
                if(liaison.getIdFrais() == idFrais){
                    liaison.setMontant(newMontant);
                }
            }
        }
    }

    @Override
    public long getSignatureEleve() {
        return this.signatureEleve;
    }

    @Override
    public void setSignatureEleve(long signature) {
        this.signatureEleve = signature;
    }
    
    
    
}
