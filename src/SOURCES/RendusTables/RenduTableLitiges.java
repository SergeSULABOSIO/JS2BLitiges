/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTables;

import ICONES.Icones;
import SOURCES.ModelesTables.ModeleListeLitiges;
import SOURCES.UI.CelluleProgressionLitige;
import SOURCES.Utilitaires.DonneesLitige;
import SOURCES.Utilitaires.ParametresLitige;
import Source.Interface.InterfaceAyantDroit;
import Source.Interface.InterfaceClasse;
import Source.Interface.InterfaceEleve;
import Source.Interface.InterfaceLitige;
import Source.Objet.CouleurBasique;
import Source.Objet.Echeance;
import Source.Objet.Litige;
import Source.UI.CelluleTableauSimple;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author user
 */
public class RenduTableLitiges implements TableCellRenderer {

    private Icones icones;
    private ModeleListeLitiges modeleListeLitiges;
    private ParametresLitige parametresLitige;
    private DonneesLitige donneesLitige;
    private CouleurBasique couleurBasique;

    public RenduTableLitiges(CouleurBasique couleurBasique, Icones icones, ModeleListeLitiges modeleListeLitiges, DonneesLitige donneesLitige, ParametresLitige parametresLitige) {
        this.icones = icones;
        this.couleurBasique = couleurBasique;
        this.modeleListeLitiges = modeleListeLitiges;
        this.parametresLitige = parametresLitige;
        this.donneesLitige = donneesLitige;
    }

    private String getClasse(int idClasse) {
        for (InterfaceClasse Iclasse : parametresLitige.getListeClasse()) {
            if (idClasse == Iclasse.getId()) {
                return Iclasse.getNom() + ", " + Iclasse.getNomLocal();
            }
        }
        return "";
    }

    private String getEleve(int idEleve) {
        for (InterfaceEleve Ieleve : donneesLitige.getEleves()) {
            if (idEleve == Ieleve.getId()) {
                return Ieleve.getNom() + " " + Ieleve.getPostnom() + " " + Ieleve.getPrenom();
            }
        }
        return "";
    }

    private boolean isAyanDroit(int idEleve) {
        for (InterfaceAyantDroit Iaya : donneesLitige.getListeAyantDroits()) {
            if (idEleve == Iaya.getIdEleve()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Elève", "Classe", "Solvable?", +Objets Echéances};
        CelluleTableauSimple cellule;
        switch (column) {
            case 0://N°
                cellule = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_CENTRE, null);
                cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
                return cellule;
            case 1://Eleve
                ImageIcon img = null;
                if (value != null) {
                    if (isAyanDroit(Integer.parseInt(value + "")) == true) {
                        img = icones.getAdministrateur_01();
                    }
                }
                cellule = new CelluleTableauSimple(couleurBasique, " " + getEleve(Integer.parseInt(value + "")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, img);
                cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
                return cellule;
            case 2://Classe
                cellule = new CelluleTableauSimple(couleurBasique, " " + getClasse(Integer.parseInt(value + "")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, null);
                cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
                return cellule;
            case 3://Solvable?
                ImageIcon imgSolv = null;
                if ((boolean) value == true) {
                    imgSolv = icones.getAimer_01();
                } else {
                    imgSolv = icones.getAnnuler_01();
                }
                cellule = new CelluleTableauSimple(couleurBasique, "", CelluleTableauSimple.ALIGNE_CENTRE, imgSolv);
                cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
                return cellule;
            default://Les Objets Echéances
                if (value != null) {
                    CelluleProgressionLitige celluleProgress = new CelluleProgressionLitige(couleurBasique, (Echeance) value, parametresLitige, icones);
                    celluleProgress.ecouterSelection(isSelected, row, hasFocus);
                    return celluleProgress;
                } else {
                    CelluleTableauSimple cellSim = new CelluleTableauSimple(couleurBasique, "", CelluleTableauSimple.ALIGNE_CENTRE, null);
                    cellSim.ecouterSelection(isSelected, row, row, hasFocus);
                    return cellSim;
                }
        }
    }

    private int getBeta(int row) {
        if (modeleListeLitiges != null) {
            Litige Ilitige = this.modeleListeLitiges.getLitiges(row);
            if (Ilitige != null) {
                return Ilitige.getBeta();
            }
        }
        return InterfaceLitige.BETA_NOUVEAU;
    }
}
