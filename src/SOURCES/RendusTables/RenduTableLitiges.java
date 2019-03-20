/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTables;

import ICONES.Icones;
import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceEleve;
import SOURCES.Interface.InterfaceLitige;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.ModelesTables.ModeleListeLitiges;
import SOURCES.UI.CelluleProgressionTableau;
import SOURCES.UI.CelluleSimpleTableau;
import SOURCES.Utilitaires.DonneesLitige;
import SOURCES.Utilitaires.ParametresLitige;
import SOURCES.Utilitaires.Util;
import java.awt.Component;
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

    public RenduTableLitiges(Icones icones, ModeleListeLitiges modeleListeLitiges, DonneesLitige donneesLitige, ParametresLitige parametresLitige) {
        this.icones = icones;
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

    

    

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Elève", "Classe", +Objets Echéances};
        CelluleSimpleTableau cellule;
        switch (column) {
            case 0://N°
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
                return cellule;
            case 1://Eleve
                cellule = new CelluleSimpleTableau(" " + getEleve(Integer.parseInt(value + "")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, null);
                cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
                return cellule;
            case 2://Classe
                cellule = new CelluleSimpleTableau(" " + getClasse(Integer.parseInt(value + "")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, null);
                cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
                return cellule;
            default://Les Objets Echéances
                CelluleProgressionTableau celluleProgress = new CelluleProgressionTableau((InterfaceEcheance) value, parametresLitige, icones);
                celluleProgress.ecouterSelection(isSelected, row, hasFocus);
                return celluleProgress;
        }
    }

    private int getBeta(int row) {
        if (modeleListeLitiges != null) {
            InterfaceLitige Ilitige = this.modeleListeLitiges.getLitiges(row);
            if (Ilitige != null) {
                return Ilitige.getBeta();
            }
        }
        return InterfaceLitige.BETA_NOUVEAU;
    }
}
