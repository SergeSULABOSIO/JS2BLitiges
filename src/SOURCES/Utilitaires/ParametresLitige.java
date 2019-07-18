/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;


import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Interface.InterfaceFrais;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.Interface.InterfacePeriode;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class ParametresLitige {
    //private int idFacture;
    //private String numero;
    private int idUtilisateur;
    private String nomUtilisateur;
    private InterfaceEntreprise entreprise;
    private InterfaceExercice exercice;
    private InterfaceMonnaie monnaieOutPut;
    
    private Vector<InterfaceMonnaie> listeMonnaies;
    private Vector<InterfaceClasse> listeClasse;
    private Vector<InterfaceFrais> listeFraises;
    private Vector<InterfacePeriode> listePeriodes;
    

    public ParametresLitige(int idUtilisateur, String nomUtilisateur, InterfaceEntreprise entreprise, InterfaceExercice exercice, InterfaceMonnaie monnaieOutPut, Vector<InterfaceMonnaie> listeMonnaies, Vector<InterfaceClasse> listeClasse, Vector<InterfaceFrais> listeFraises, Vector<InterfacePeriode> listePeriodes) {
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.entreprise = entreprise;
        this.exercice = exercice;
        this.monnaieOutPut = monnaieOutPut;
        this.listeMonnaies = listeMonnaies;
        this.listeClasse = listeClasse;
        this.listeFraises = listeFraises;
        this.listePeriodes = listePeriodes;
    }
    

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public InterfaceEntreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(InterfaceEntreprise entreprise) {
        this.entreprise = entreprise;
    }

    public InterfaceExercice getExercice() {
        return exercice;
    }

    public void setExercice(InterfaceExercice exercice) {
        this.exercice = exercice;
    }

    public InterfaceMonnaie getMonnaieOutPut() {
        return monnaieOutPut;
    }

    public void setMonnaieOutPut(InterfaceMonnaie monnaieOutPut) {
        this.monnaieOutPut = monnaieOutPut;
    }

    public Vector<InterfaceMonnaie> getListeMonnaies() {
        return listeMonnaies;
    }

    public void setListeMonnaies(Vector<InterfaceMonnaie> listeMonnaies) {
        this.listeMonnaies = listeMonnaies;
    }

    public Vector<InterfaceClasse> getListeClasse() {
        return listeClasse;
    }

    public void setListeClasse(Vector<InterfaceClasse> listeClasse) {
        this.listeClasse = listeClasse;
    }

    public Vector<InterfaceFrais> getFrais(int idFrais) {
        if(idFrais == -1){
            return listeFraises;
        }else{
            for(InterfaceFrais Iart: listeFraises){
                if(idFrais == Iart.getId()){
                    Vector<InterfaceFrais> listeFiltree = new Vector<>();
                    listeFiltree.add(Iart);
                    return listeFiltree;
                }
            }
        }
        return new Vector<>();
    }
    
    public Vector<InterfacePeriode> getPeriode(int idPeriode) {
        if(idPeriode == -1){
            return listePeriodes;
        }else{
            for(InterfacePeriode Iper: listePeriodes){
                if(idPeriode == Iper.getId()){
                    Vector<InterfacePeriode> listeFiltree = new Vector<>();
                    listeFiltree.add(Iper);
                    return listeFiltree;
                }
            }
        }
        return null;
    }


    public void setListeFrais(Vector<InterfaceFrais> listeFraises) {
        this.listeFraises = listeFraises;
    }

    public Vector<InterfacePeriode> getListePeriodes(int idPeriodeFiltre) {
        if(idPeriodeFiltre == -1){
            return listePeriodes;
        }else{
            for(InterfacePeriode Iperiode: listePeriodes){
                if(idPeriodeFiltre == Iperiode.getId()){
                    Vector<InterfacePeriode> listeFiltree = new Vector<>();
                    listeFiltree.add(Iperiode);
                    return listeFiltree;
                }
            }
        }
        return new Vector<>();
    }

    public void setListePeriodes(Vector<InterfacePeriode> listePeriodes) {
        this.listePeriodes = listePeriodes;
    }

    @Override
    public String toString() {
        return "ParametresLitige{" + "idUtilisateur=" + idUtilisateur + ", nomUtilisateur=" + nomUtilisateur + ", entreprise=" + entreprise + ", exercice=" + exercice + ", monnaieOutPut=" + monnaieOutPut + ", listeMonnaies=" + listeMonnaies + ", listeClasse=" + listeClasse + ", listeFraises=" + listeFraises + ", listePeriodes=" + listePeriodes + '}';
    }
}
