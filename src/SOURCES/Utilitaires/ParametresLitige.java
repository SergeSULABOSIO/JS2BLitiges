/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.Interface.InterfacePaiement;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class ParametresLitige {
    private int idFacture;
    private String numero;
    private int idUtilisateur;
    private String nomUtilisateur;
    private InterfaceEntreprise entreprise;
    private InterfaceExercice exercice;
    private InterfaceMonnaie monnaieOutPut;
    
    private Vector<InterfaceMonnaie> listeMonnaies;
    private Vector<InterfaceClasse> listeClasse;
    private Vector<InterfaceArticle> articles;
    

    public ParametresLitige(int idFacture, String numero, int idUtilisateur, String nomUtilisateur, InterfaceEntreprise entreprise, InterfaceExercice exercice, InterfaceMonnaie monnaieOutPut, Vector<InterfaceMonnaie> listeMonnaies, Vector<InterfaceClasse> listeClasse, Vector<InterfaceArticle> articles) {
        this.idFacture = idFacture;
        this.numero = numero;
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.entreprise = entreprise;
        this.exercice = exercice;
        this.monnaieOutPut = monnaieOutPut;
        this.listeMonnaies = listeMonnaies;
        this.listeClasse = listeClasse;
        this.articles = articles;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public Vector<InterfaceArticle> getArticles(int idFrais) {
        if(idFrais == -1){
            return articles;
        }else{
            for(InterfaceArticle Iart: articles){
                if(idFrais == Iart.getId()){
                    Vector<InterfaceArticle> listeFiltree = new Vector<>();
                    listeFiltree.add(Iart);
                    return listeFiltree;
                }
            }
        }
        return null;
    }

    public void setArticles(Vector<InterfaceArticle> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "ParametresLitige{" + "idFacture=" + idFacture + ", numero=" + numero + ", idUtilisateur=" + idUtilisateur + ", nomUtilisateur=" + nomUtilisateur + ", entreprise=" + entreprise + ", exercice=" + exercice + ", monnaieOutPut=" + monnaieOutPut + ", listeMonnaies=" + listeMonnaies + ", listeClasse=" + listeClasse + ", articles=" + articles + '}';
    }
}
