/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelesTables;

import BEAN_BARRE_OUTILS.Bouton;
import BEAN_MenuContextuel.RubriqueSimple;
import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceLitige;
import java.awt.Color;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeLitiges extends AbstractTableModel {

    private String[] titreColonnes = null;
    private Vector<InterfaceLitige> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private Bouton btEnreg;
    private RubriqueSimple mEnreg;

    public ModeleListeLitiges(JScrollPane parent, Bouton btEnreg, RubriqueSimple mEnreg, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.mEnreg = mEnreg;
        this.btEnreg = btEnreg;
        
        //C'est ici qu'il faut charger automatiquement les données
        
    }

    public InterfaceLitige getLitiges(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceLitige art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public InterfaceLitige getLitige_id(int id) {
        if (id != -1) {
            for (InterfaceLitige art : listeData) {
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public Vector<InterfaceLitige> getListeData() {
        return this.listeData;
    }
    
    public void AjouterLitige(InterfaceLitige newLitige) {
        mEnreg.setCouleur(Color.blue);
        btEnreg.setCouleur(Color.blue);
        redessinerTable();
    }

    public void redessinerTable() {
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

    public void actualiser() {
        redessinerTable();
    }

    @Override
    public int getRowCount() {
        return listeData.size();
    }

    private void initTitresColonnes() {
        Vector titresCols = new Vector();
        titresCols.add("N°");
        titresCols.add("Elève");
        titresCols.add("Classe");
        if(!listeData.isEmpty()){
            for (InterfaceEcheance Ieche : listeData.firstElement().getListeEcheances()) {
                titresCols.add(Ieche.getNom());
            }
        }
        
        //On verse les titres dans le tableau static
        this.titreColonnes = new String[titresCols.size()];
        for (int i = 0; i < titreColonnes.length; i++) {
            this.titreColonnes[i] = titresCols.elementAt(i) + "";
        }
    }

    @Override
    public int getColumnCount() {
        initTitresColonnes();
        return titreColonnes.length;
    }

    @Override
    public String getColumnName(int column) {
        initTitresColonnes();
        return titreColonnes[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"N°", "Elève", "Classe"};
        if (columnIndex < 4) {
            switch (columnIndex) {
                case 0:
                    return (rowIndex + 1) + "";
                case 1:
                    return listeData.elementAt(rowIndex).getNom();
                case 2:
                    return listeData.elementAt(rowIndex).getNbTranches();
                case 3:
                    return listeData.elementAt(rowIndex).getMontant_default();
                default:
                    return "null";
            }
        } else {
            Vector<LiaisonClasseFrais> liaisons = listeData.elementAt(rowIndex).getLiaisons();
            if (!liaisons.isEmpty()) {
                return listeData.elementAt(rowIndex).getLiaisons().elementAt(columnIndex - 4).getMontant();
            } else {
                return "RAS";
            }

        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Nom", "Capacité"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return String.class;//Nom
            case 2:
                return Integer.class;//Capacité
            case 3:
                return Double.class;//Capacité
            default:
                return Object.class;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void appliquerMontantDefaut(InterfaceFrais Iclasse) {
        if (Iclasse != null) {
            for (LiaisonClasseFrais liaison : Iclasse.getLiaisons()) {
                liaison.setMontant(Iclasse.getMontant_default());
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Nom", "Capacité"};
        InterfaceFrais Ifrais = listeData.get(rowIndex);
        String avant = Ifrais.toString() + "" + Ifrais.getLiaisons().toString();
        if (columnIndex < 4) {
            switch (columnIndex) {
                case 1:
                    Ifrais.setNom(aValue + "");
                    break;
                case 2:
                    Ifrais.setNbTranches(Integer.parseInt(aValue + ""));
                    break;
                case 3:
                    Ifrais.setMontant_default(Double.parseDouble(aValue + ""));
                    appliquerMontantDefaut(Ifrais);
                    break;
                default:
                    break;
            }
        } else {
            listeData.elementAt(rowIndex).getLiaisons().elementAt(columnIndex - 4).setMontant(Double.parseDouble(aValue + ""));
        }

        String apres = Ifrais.toString() + "" + Ifrais.getLiaisons().toString();
        if (!avant.equals(apres)) {
            if (Ifrais.getBeta() == InterfaceFrais.BETA_EXISTANT) {
                Ifrais.setBeta(InterfaceFrais.BETA_MODIFIE);
                mEnreg.setCouleur(Color.blue);
                btEnreg.setCouleur(Color.blue);
            }
        }
        listeData.set(rowIndex, Ifrais);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

}
