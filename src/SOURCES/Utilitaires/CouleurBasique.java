/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import java.awt.Color;

/**
 *
 * @author HP Pavilion
 */
public class CouleurBasique {
    private Color couleur_background_selection;
    private Color couleur_background_normal;
    private Color couleur_encadrement_selection;
    private Color couleur_foreground_objet_modifie;
    private Color couleur_foreground_objet_nouveau;
    private Color couleur_foreground_objet_existant;

    public CouleurBasique() {
    }

    public CouleurBasique(Color couleur_background_selection, Color couleur_background_normal, Color couleur_encadrement_selection, Color couleur_foreground_objet_modifie, Color couleur_foreground_objet_nouveau, Color couleur_foreground_objet_existant) {
        this.couleur_background_selection = couleur_background_selection;
        this.couleur_background_normal = couleur_background_normal;
        this.couleur_encadrement_selection = couleur_encadrement_selection;
        this.couleur_foreground_objet_modifie = couleur_foreground_objet_modifie;
        this.couleur_foreground_objet_nouveau = couleur_foreground_objet_nouveau;
        this.couleur_foreground_objet_existant = couleur_foreground_objet_existant;
    }

    public Color getCouleur_background_selection() {
        return couleur_background_selection;
    }

    public void setCouleur_background_selection(Color couleur_background_selection) {
        this.couleur_background_selection = couleur_background_selection;
    }

    public Color getCouleur_background_normal() {
        return couleur_background_normal;
    }

    public void setCouleur_background_normal(Color couleur_background_normal) {
        this.couleur_background_normal = couleur_background_normal;
    }

    public Color getCouleur_encadrement_selection() {
        return couleur_encadrement_selection;
    }

    public void setCouleur_encadrement_selection(Color couleur_encadrement_selection) {
        this.couleur_encadrement_selection = couleur_encadrement_selection;
    }

    public Color getCouleur_foreground_objet_modifie() {
        return couleur_foreground_objet_modifie;
    }

    public void setCouleur_foreground_objet_modifie(Color couleur_foreground_objet_modifie) {
        this.couleur_foreground_objet_modifie = couleur_foreground_objet_modifie;
    }

    public Color getCouleur_foreground_objet_nouveau() {
        return couleur_foreground_objet_nouveau;
    }

    public void setCouleur_foreground_objet_nouveau(Color couleur_foreground_objet_nouveau) {
        this.couleur_foreground_objet_nouveau = couleur_foreground_objet_nouveau;
    }

    public Color getCouleur_foreground_objet_existant() {
        return couleur_foreground_objet_existant;
    }

    public void setCouleur_foreground_objet_existant(Color couleur_foreground_objet_existant) {
        this.couleur_foreground_objet_existant = couleur_foreground_objet_existant;
    }

    @Override
    public String toString() {
        return "CouleurBasique{" + "couleur_background_selection=" + couleur_background_selection + ", couleur_background_normal=" + couleur_background_normal + ", couleur_encadrement_selection=" + couleur_encadrement_selection + ", couleur_foreground_objet_modifie=" + couleur_foreground_objet_modifie + ", couleur_foreground_objet_nouveau=" + couleur_foreground_objet_nouveau + ", couleur_foreground_objet_existant=" + couleur_foreground_objet_existant + '}';
    }
}
