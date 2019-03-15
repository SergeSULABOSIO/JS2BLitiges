/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTables;

import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceLitige;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.ModelesTables.ModeleListeLitiges;
import SOURCES.UI.CelluleSimpleTableau;
import SOURCES.Utilitaires.ParametresLitige;
import SOURCES.Utilitaires.Util;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author user
 */

public class RenduTableLitiges implements TableCellRenderer {
    
    private ImageIcon iconeEdition;
    private ModeleListeLitiges modeleListeLitiges;
    private ParametresLitige parametresLitige;

    public RenduTableLitiges(ImageIcon iconeEdition, ModeleListeLitiges modeleListeLitiges, ParametresLitige parametresLitige) {
        this.iconeEdition = iconeEdition;
        this.modeleListeLitiges = modeleListeLitiges;
        this.parametresLitige = parametresLitige;
    }
    
    private String getClasse(int idClasse){
        for(InterfaceClasse Iclasse : parametresLitige.getListeClasse()){
            if(idClasse == Iclasse.getId()){
                return Iclasse.getNom();
            }
        }
        return "";
    }
    
    private String getMonnaie(int idMonnaie){
        for(InterfaceMonnaie Imonnaie : parametresLitige.getListeMonnaies()){
            if(idMonnaie == Imonnaie.getId()){
                return Imonnaie.getCode();
            }
        }
        return "";
    }
    
    private String getEcheance(InterfaceEcheance Iecheance){
        if(Iecheance != null){
            String mntDu = Util.getMontantFrancais(Iecheance.getMontantDu()) + " " + getMonnaie(Iecheance.getIdMonnaie());
            String mntPaye = Util.getMontantFrancais(Iecheance.getMontantPaye()) + " " + getMonnaie(Iecheance.getIdMonnaie());
            double solde = (Iecheance.getMontantDu() - Iecheance.getMontantPaye());
            String mntSolde = Util.getMontantFrancais(solde) + " " + getMonnaie(Iecheance.getIdMonnaie());
            return Iecheance.getNom() + " ("+ mntPaye + "/"+ mntDu  + ":" + mntSolde + ")";
        }else{
            return "";
        }
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Elève", "Classe", +Objets Echéances};
        CelluleSimpleTableau cellule = null;
        switch (column) {
            case 0://N°
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                break;
            case 1://Eleve
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 2://Classe
                cellule = new CelluleSimpleTableau(" " + getClasse(Integer.parseInt(value+"")) + " " , CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
            default://Les Objets Echéances
                InterfaceEcheance Ieche = (InterfaceEcheance)value;
                cellule = new CelluleSimpleTableau(" " + getEcheance(Ieche) + " ", CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
        }
        cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
        return cellule;
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
