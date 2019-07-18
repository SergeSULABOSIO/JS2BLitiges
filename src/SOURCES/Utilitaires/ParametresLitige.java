/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;


import Source.Objet.Classe;
import Source.Objet.Entreprise;
import Source.Objet.Exercice;
import Source.Objet.Frais;
import Source.Objet.Monnaie;
import Source.Objet.Periode;
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
    private Entreprise entreprise;
    private Exercice exercice;
    private Monnaie monnaieOutPut;
    
    private Vector<Monnaie> listeMonnaies;
    private Vector<Classe> listeClasse;
    private Vector<Frais> listeFraises;
    private Vector<Periode> listePeriodes;
    

    public ParametresLitige(int idUtilisateur, String nomUtilisateur, Entreprise entreprise, Exercice exercice, Monnaie monnaieOutPut, Vector<Monnaie> listeMonnaies, Vector<Classe> listeClasse, Vector<Frais> listeFraises, Vector<Periode> listePeriodes) {
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

    

    public Vector<Frais> getFrais(int idFrais) {
        if(idFrais == -1){
            return listeFraises;
        }else{
            for(Frais Iart: listeFraises){
                if(idFrais == Iart.getId()){
                    Vector<Frais> listeFiltree = new Vector<>();
                    listeFiltree.add(Iart);
                    return listeFiltree;
                }
            }
        }
        return new Vector<>();
    }
    
    public Vector<Periode> getPeriode(int idPeriode) {
        if(idPeriode == -1){
            return listePeriodes;
        }else{
            for(Periode Iper: listePeriodes){
                if(idPeriode == Iper.getId()){
                    Vector<Periode> listeFiltree = new Vector<>();
                    listeFiltree.add(Iper);
                    return listeFiltree;
                }
            }
        }
        return null;
    }


    public void setListeFrais(Vector<Frais> listeFraises) {
        this.listeFraises = listeFraises;
    }

    public Vector<Periode> getListePeriodes(int idPeriodeFiltre) {
        if(idPeriodeFiltre == -1){
            return listePeriodes;
        }else{
            for(Periode Iperiode: listePeriodes){
                if(idPeriodeFiltre == Iperiode.getId()){
                    Vector<Periode> listeFiltree = new Vector<>();
                    listeFiltree.add(Iperiode);
                    return listeFiltree;
                }
            }
        }
        return new Vector<>();
    }

    public void setListePeriodes(Vector<Periode> listePeriodes) {
        this.listePeriodes = listePeriodes;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Monnaie getMonnaieOutPut() {
        return monnaieOutPut;
    }

    public void setMonnaieOutPut(Monnaie monnaieOutPut) {
        this.monnaieOutPut = monnaieOutPut;
    }

    public Vector<Monnaie> getListeMonnaies() {
        return listeMonnaies;
    }

    public void setListeMonnaies(Vector<Monnaie> listeMonnaies) {
        this.listeMonnaies = listeMonnaies;
    }

    public Vector<Classe> getListeClasse() {
        return listeClasse;
    }

    public void setListeClasse(Vector<Classe> listeClasse) {
        this.listeClasse = listeClasse;
    }

    public Vector<Frais> getListeFraises() {
        return listeFraises;
    }

    public void setListeFraises(Vector<Frais> listeFraises) {
        this.listeFraises = listeFraises;
    }
    
    @Override
    public String toString() {
        return "ParametresLitige{" + "idUtilisateur=" + idUtilisateur + ", nomUtilisateur=" + nomUtilisateur + ", entreprise=" + entreprise + ", exercice=" + exercice + ", monnaieOutPut=" + monnaieOutPut + ", listeMonnaies=" + listeMonnaies + ", listeClasse=" + listeClasse + ", listeFraises=" + listeFraises + ", listePeriodes=" + listePeriodes + '}';
    }
}
