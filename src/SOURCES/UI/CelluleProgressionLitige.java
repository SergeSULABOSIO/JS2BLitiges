/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI;

import ICONES.Icones;
import SOURCES.Utilitaires.ParametresLitige;
import SOURCES.Utilitaires.UtilLitige;
import Source.Interface.InterfaceEcheance;
import Source.Interface.InterfaceMonnaie;
import Source.Objet.CouleurBasique;
import Source.Objet.Echeance;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author user
 */
public class CelluleProgressionLitige extends javax.swing.JPanel {
    
    /**
     * Creates new form PanValeurTable
     */
    
    public static final Color COULEUR_CELLULE_SELECTIONNEE = new java.awt.Color(255, 255, 51);
    public Echeance echeance;
    private ParametresLitige parametresLitige;
    private CouleurBasique couleurBasique;
    
    public CelluleProgressionLitige(CouleurBasique couleurBasique, Echeance echeance, ParametresLitige parametresLitige, Icones icone) {
        initComponents();
        this.couleurBasique = couleurBasique;
        this.parametresLitige = parametresLitige;
        this.echeance = echeance;
        this.labicone.setIcon(icone.getSablier_01());
        
        if(echeance != null){
            setValeur(getLabel(echeance), echeance.getMontantPaye(), echeance.getMontantDu());
        }
    }
    
    private String getMonnaie(int idMonnaie) {
        for (InterfaceMonnaie Imonnaie : parametresLitige.getListeMonnaies()) {
            if (idMonnaie == Imonnaie.getId()) {
                return Imonnaie.getCode();
            }
        }
        return "";
    }
    
    private String getLabel(InterfaceEcheance Iecheance) {
        if (Iecheance != null) {
            String mntDu = UtilLitige.getMontantFrancais(Iecheance.getMontantDu()) + " " + getMonnaie(Iecheance.getIdMonnaie());
            String mntPaye = UtilLitige.getMontantFrancais(Iecheance.getMontantPaye()) + " " + getMonnaie(Iecheance.getIdMonnaie());
            double solde = (Iecheance.getMontantDu() - Iecheance.getMontantPaye());
            String mntSolde = UtilLitige.getMontantFrancais(solde) + " " + getMonnaie(Iecheance.getIdMonnaie());
            return "(" + mntPaye + " / " + mntDu + ")";
        } else {
            return "";
        }
    }
    
    private void ecouterLigneImpare(int row){
        if((row % 2) == 0){
            this.setBackground(Color.WHITE);
        }else{
            this.setBackground(Color.lightGray);
        }
    }
    
    public void ecouterSelection(boolean isSelected, int row, boolean hasfocus){
        if(isSelected == true){
            labicone.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
            labicone.setForeground(Color.WHITE);
            //this.setBackground(Color.BLACK);
            this.setBackground(couleurBasique.getCouleur_background_selection());    //this.setBackground(Color.BLACK);
        }else{
            labicone.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 11));
            labicone.setForeground(Color.BLACK);
            this.setForeground(couleurBasique.getCouleur_background_selection());       //labvaleur.setForeground(Color.BLACK);
            ecouterLigneImpare(row);
        }
        appliquerCouleurFocusBordureCellule(hasfocus);
    }
    
    public void appliquerCouleurFocusBordureCellule(boolean hasFocus){
        if(hasFocus == true){
            this.setBorder(javax.swing.BorderFactory.createLineBorder(couleurBasique.getCouleur_encadrement_selection(), 2));    //COULEUR_CELLULE_SELECTIONNEE
        }else{
            this.setBorder(null);
        }
    }
    
    
    public void setValeur(String valeurs, double val, double totalDu){
        this.progress.setStringPainted(true);
        this.progress.setMaximum((int)totalDu);
        this.progress.setString(valeurs);
        this.progress.setValue((int)val);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labicone = new javax.swing.JLabel();
        progress = new javax.swing.JProgressBar();

        setBackground(new java.awt.Color(255, 255, 255));

        labicone.setBackground(new java.awt.Color(255, 255, 255));
        labicone.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labicone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG_Litige/Facture01.png"))); // NOI18N
        labicone.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(labicone, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labicone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labicone;
    private javax.swing.JProgressBar progress;
    // End of variables declaration//GEN-END:variables
}