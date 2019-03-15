/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Interface;


import SOURCES.Utilitaires.LiaisonClasseFrais;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public interface InterfaceFrais {
    
    public abstract int getId();
    public abstract int getIdEntreprise();
    public abstract int getIdUtilisateur();
    public abstract int getIdExercice();
    public abstract int getIdMonnaie();
    public abstract String getNom();
    public abstract String getMonnaie();
    public abstract long getSignatureMonnaie();
    public abstract int getNbTranches();
    public abstract double getMontant_default();
    public abstract Vector<LiaisonClasseFrais> getLiaisons();
    public abstract LiaisonClasseFrais getLiaison(InterfaceClasse classe);
    
    public abstract void setId(int id);
    public abstract void setIdEntreprise(int idEntreprise);
    public abstract void setIdUtilisateur(int idUtilisateur);
    public abstract void setIdExercice(int idExercice);
    public abstract void setIdMonnaie(int idMonnaie);
    public abstract void setNom(String nom);
    public abstract void setMonnaie(String monnaie);
    public abstract void setSignatureMonnaie(long signatureMonnaie);
    public abstract void setNbTranches(int nbTranches);
    public abstract void setMontant_default(double montant);
    public abstract void setLiaisons(Vector<LiaisonClasseFrais> liaison);
    public abstract void ajouterLiaisons(LiaisonClasseFrais liaison);
    public abstract void viderLiaisons();
    
    
    
}
