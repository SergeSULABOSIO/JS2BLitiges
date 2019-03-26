/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import java.util.Date;
import java.util.Vector;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceAyantDroit;
import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfaceEleve;
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.UI.Panel;
import SOURCES.Utilitaires.DonneesLitige;
import SOURCES.Utilitaires.LiaisonEleveFrais;
import SOURCES.Utilitaires.ParametresLitige;
import SOURCES.Utilitaires.Util;

/**
 *
 * @author HP Pavilion
 */
public class Test_Principal extends javax.swing.JFrame {

    public int idUtilisateur = 1;
    public String nomUtilisateur = "Serge SULA BOSIO";
    public int idFacture = 20;
    public int idClasse = 3;
    public double tva = 0;
    public double remise = 0;
    public String numeroFacture = "" + (new Date().getTime());
    //
    public TEST_Entreprise entreprise = new TEST_Entreprise(-1, "S2B, Simple.Intuitif", "167B, Av. ITAGA, C./LINGWALA, KINSHASA - RDC", "+243844803514", "info@s2b-simple.com", "www.s2b-simple.com", "EquityBank Congo", "S2B", "000000002114545", "0012554", "CDKIS0012", "logo.png", "RCCM/CD/KIN45-59", "IDNAT000124", "IMP1213");
    public TEST_Exercice exercice = new TEST_Exercice(12, entreprise.getId(), idUtilisateur, "Année Scolaire 2019-2020", new Date(), Util.getDate_AjouterAnnee(new Date(), 1), InterfaceExercice.BETA_EXISTANT);
    //Classe
    public TEST_Classe classeCM1 = new TEST_Classe(1, idUtilisateur, entreprise.getId(), exercice.getId(), "CM1", 50, "Local 01", (new Date().getTime() + 12));
    public TEST_Classe classeCM2 = new TEST_Classe(2, idUtilisateur, entreprise.getId(), exercice.getId(), "CM2", 50, "Local 02", (new Date().getTime() + 13));
    public TEST_Classe classeCE1 = new TEST_Classe(3, idUtilisateur, entreprise.getId(), exercice.getId(), "CE1", 50, "Local 03", (new Date().getTime() + 14));
    //Monnaie
    public TEST_Monnaie MONNAIE_USD = new TEST_Monnaie(20, entreprise.getId(), idUtilisateur, exercice.getId(), "Dollars Américains", "$", InterfaceMonnaie.NATURE_MONNAIE_ETRANGERE, 1620, new Date().getTime(), InterfaceMonnaie.BETA_EXISTANT);
    public TEST_Monnaie MONNAIE_Euro = new TEST_Monnaie(22, entreprise.getId(), idUtilisateur, exercice.getId(), "Euro", "Euro", InterfaceMonnaie.NATURE_MONNAIE_ETRANGERE, 1800, new Date().getTime(), InterfaceMonnaie.BETA_EXISTANT);
    public TEST_Monnaie MONNAIE_CDF = new TEST_Monnaie(21, entreprise.getId(), idUtilisateur, exercice.getId(), "Francs Congolais", "Fc", InterfaceMonnaie.NATURE_MONNAIE_LOCALE, 1, new Date().getTime() + 1, InterfaceMonnaie.BETA_EXISTANT);
    //Frais
    public TEST_Article INSCRIPTION = new TEST_Article(1, "INSCRIPTION", 1, "Année", MONNAIE_CDF.getId(), tva, 10000, remise, 1, InterfaceArticle.BETA_EXISTANT);
    public TEST_Article MINERVALE = new TEST_Article(2, "MINERVALE", 1, "Année", MONNAIE_USD.getId(), tva, 1500, remise, 5, InterfaceArticle.BETA_EXISTANT);
    public TEST_Article TRAVAIL_MANUEL = new TEST_Article(3, "TRAVAIL MANUEL", 1, "Année", MONNAIE_USD.getId(), tva, 10, remise, 2, InterfaceArticle.BETA_EXISTANT);
    //Eleves
    public TEST_Eleve eleveTONGO = new TEST_Eleve(120, entreprise.getId(), idUtilisateur, exercice.getId(), classeCE1.getId(), (new Date().getTime() + 45), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "TONGO", "BATANGILA", "Christian", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);
    public TEST_Eleve eleveSULA = new TEST_Eleve(121, entreprise.getId(), idUtilisateur, exercice.getId(), classeCE1.getId(), (new Date().getTime() + 46), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "SULA", "BOSIO", "Serge", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);
    public TEST_Eleve eleveOPOTHA = new TEST_Eleve(122, entreprise.getId(), idUtilisateur, exercice.getId(), classeCM1.getId(), (new Date().getTime() + 47), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "OPOTHA", "LOFUNGULA", "Emmanuel", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);
    public TEST_Eleve eleveMAKULA = new TEST_Eleve(123, entreprise.getId(), idUtilisateur, exercice.getId(), classeCE1.getId(), (new Date().getTime() + 48), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "MAKULA", "BOFANDO", "Alain", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);
    //Paiements
    public TEST_Paiement paiementOPOTHA_Inscription = new TEST_Paiement(1, eleveOPOTHA.getId(), INSCRIPTION.getId(), "OPOTHA", INSCRIPTION.getNom(), "OPOTHA", 10000, new Date(), InterfacePaiement.MODE_CAISSE, "DSER22445", InterfacePaiement.BETA_EXISTANT);
    public TEST_Paiement paiementOPOTHA_Minervale01 = new TEST_Paiement(3, eleveOPOTHA.getId(), MINERVALE.getId(), "OPOTHA", MINERVALE.getNom(), "OPOTHA", 100, new Date(), InterfacePaiement.MODE_CAISSE, "DSER22445", InterfacePaiement.BETA_EXISTANT);
    public TEST_Paiement paiementSULA_Inscription02 = new TEST_Paiement(2, eleveSULA.getId(), INSCRIPTION.getId(), "SULA", INSCRIPTION.getNom(), "SULA BOSIO", 5000, new Date(), InterfacePaiement.MODE_CAISSE, "DSER22445", InterfacePaiement.BETA_EXISTANT);
    public TEST_Paiement paiementSULA_Minervale01 = new TEST_Paiement(3, eleveSULA.getId(), MINERVALE.getId(), "SULA", MINERVALE.getNom(), "SULA BOSIO", 100, new Date(), InterfacePaiement.MODE_CAISSE, "DSER22445", InterfacePaiement.BETA_EXISTANT);
    //Liaisons frais AyantDroit
    public LiaisonEleveFrais liaisonSULA_Inscription = new LiaisonEleveFrais(eleveSULA.getSignature(), INSCRIPTION.getId(), 0, MONNAIE_CDF.getId(), "CDF");
    public LiaisonEleveFrais liaisonSULA_Minervale = new LiaisonEleveFrais(eleveSULA.getSignature(), MINERVALE.getId(), 1000, MONNAIE_USD.getId(), "USD");
    public LiaisonEleveFrais liaisonSULA_TravMan = new LiaisonEleveFrais(eleveSULA.getSignature(), TRAVAIL_MANUEL.getId(), 0, MONNAIE_USD.getId(), "USD");
    
    
    
    
    public Vector<InterfaceArticle> donneesArticles = new Vector<>();
    public Panel panelLitige = null;

    /**
     * Creates new form TestPrincipal
     */
    public Test_Principal() {
        initComponents();
    }

    private ParametresLitige getParametres() {

        //On charge les paramètres
        Vector<InterfaceArticle> listeArticles = new Vector<>();
        listeArticles.add(INSCRIPTION);
        listeArticles.add(MINERVALE);
        listeArticles.add(TRAVAIL_MANUEL);

        Vector<InterfaceMonnaie> listeMonnaies = new Vector();
        listeMonnaies.addElement(MONNAIE_USD);
        listeMonnaies.addElement(MONNAIE_CDF);
        listeMonnaies.addElement(MONNAIE_Euro);

        Vector<InterfaceClasse> listeClasse = new Vector<>();
        listeClasse.addElement(classeCM1);
        listeClasse.addElement(classeCM2);
        listeClasse.addElement(classeCE1);

        //return new ParametresFacture(idFacture, numeroFacture, idUtilisateur, nomUtilisateur, entreprise, exercice, MONNAIE_USD, listeMonnaies, listeClasse);
        return new ParametresLitige(idFacture, numeroFacture, idUtilisateur, nomUtilisateur, entreprise, exercice, MONNAIE_USD, listeMonnaies, listeClasse, listeArticles);
    }

    private DonneesLitige getDonnees() {
        //On charge les données

        Vector<InterfaceEleve> listeEleves = new Vector<>();
        listeEleves.add(eleveTONGO);
        listeEleves.add(eleveSULA);
        listeEleves.add(eleveOPOTHA);
        listeEleves.add(eleveMAKULA);
        
        Vector<InterfaceAyantDroit> listeAyantDroits = new Vector<>();
        Vector<LiaisonEleveFrais> liaisonsSULA = new Vector<>();
        liaisonsSULA.add(liaisonSULA_Minervale);
        liaisonsSULA.add(liaisonSULA_Inscription);
        liaisonsSULA.add(liaisonSULA_TravMan);
        
        listeAyantDroits.add(new TEST_Ayantdroit(1, entreprise.getId(), idUtilisateur, exercice.getId(), 121, "SULA BOSIO", liaisonsSULA, new Date().getTime(), new Date().getTime(), InterfaceAyantDroit.BETA_EXISTANT));
        
        Vector<InterfacePaiement> listePaiements = new Vector<>();
        listePaiements.add(paiementOPOTHA_Inscription);
        listePaiements.add(paiementOPOTHA_Minervale01);
        listePaiements.add(paiementSULA_Inscription02);
        listePaiements.add(paiementSULA_Minervale01);

        //return new DonneesFacture(eleve, donneesArticles, donneesPaiements);
        return new DonneesLitige(listeEleves, listeAyantDroits, listePaiements);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setText("Tester Tab");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        //Initialisation du gestionnaire des factures
        this.panelLitige = new Panel(jTabbedPane1, getDonnees(), getParametres());

        //Chargement du gestionnaire sur l'onglet
        jTabbedPane1.add("Facture", panelLitige);

        //On séléctionne l'onglet actuel
        jTabbedPane1.setSelectedComponent(panelLitige);

    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {//Nimbus
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Test_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Test_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Test_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Test_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Test_Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
