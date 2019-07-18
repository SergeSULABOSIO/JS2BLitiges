/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.UI.PanelLitige;
import java.util.Date;
import java.util.Vector;
import SOURCES.Utilitaires.DonneesLitige;
import SOURCES.Utilitaires.ParametresLitige;
import SOURCES.Utilitaires.UtilLitige;
import Source.Interface.InterfaceAyantDroit;
import Source.Interface.InterfaceClasse;
import Source.Interface.InterfaceEleve;
import Source.Interface.InterfaceExercice;
import Source.Interface.InterfaceFrais;
import Source.Interface.InterfaceMonnaie;
import Source.Interface.InterfacePaiement;
import Source.Interface.InterfacePeriode;
import Source.Objet.Ayantdroit;
import Source.Objet.Classe;
import Source.Objet.CouleurBasique;
import Source.Objet.Eleve;
import Source.Objet.Entreprise;
import Source.Objet.Exercice;
import Source.Objet.Frais;
import Source.Objet.LiaisonFraisClasse;
import Source.Objet.LiaisonFraisEleve;
import Source.Objet.LiaisonFraisPeriode;
import Source.Objet.Monnaie;
import Source.Objet.Paiement;
import Source.Objet.Periode;
import java.awt.Color;

/**
 *
 * @author HP Pavilion
 */
public class PrincipalLitige extends javax.swing.JFrame {

    public int idUtilisateur = 1;
    public String nomUtilisateur = "Serge SULA BOSIO";
    public int idFacture = 20;
    public int idClasse = 3;
    public double tva = 0;
    public double remise = 0;
    public String numeroFacture = "" + (new Date().getTime());

    
    public Entreprise entreprise = null;
    public Exercice exercice = null;
    

    //Classe
    public Classe classeCM1 = null;
    public Classe classeCM2 = null;
    public Classe classeCE1 = null;

    //Monnaie
    public Monnaie MONNAIE_USD = null;
    public Monnaie MONNAIE_Euro = null;
    public Monnaie MONNAIE_CDF = null;
    
    
    public Vector<LiaisonFraisPeriode> liaisonInsription = new Vector<>();
    public Vector<LiaisonFraisPeriode> liaisonMinervale = new Vector<>();
    public Vector<LiaisonFraisPeriode> liaisonTravManuel = new Vector<>();

    //Frais
    public Frais INSCRIPTION = null;
    public Frais MINERVALE = null;
    public Frais TRAVAIL_MANUEL = null;

    //Eleves
    public Eleve eleveTONGO = null;
    public Eleve eleveSULA = null;
    public Eleve eleveOPOTHA = null;
    public Eleve eleveMAKULA = null;
    
    //Ayant droit
    public Ayantdroit ayantSULA = null;

    //Type des périodes
    public Periode Trimestre01 = null;
    public Periode Trimestre02 = null;
    public Periode Trimestre03 = null;

    //Paiements
    public Paiement paiementOPOTHA_Inscription = null;
    public Paiement paiementOPOTHA_Minervale01 = null;
    public Paiement paiementSULA_Inscription02 = null;
    public Paiement paiementSULA_Minervale01 = null;

    //Liaisons frais AyantDroit
    public LiaisonFraisEleve liaisonSULA_Inscription = null;
    public LiaisonFraisEleve liaisonSULA_Minervale = null;
    public LiaisonFraisEleve liaisonSULA_TravMan = null;

    public PanelLitige panelLitige = null;

    /**
     * Creates new form TestPrincipal
     */
    public PrincipalLitige() {
        initComponents();
        initData();
    }

    private void initData() {
        entreprise = new Entreprise(1, "ECOLE CARESIENNE DE KINSHASA", "7e Rue Limeté Industrielle, Kinshasa/RDC", "+243844803514", "infos@cartesien.org", "wwww.cartesien.org", "logo.png", "RCCM/KD/CD/4513", "IDN00111454", "IMP00124100", "Equity Bank Congo SA", "AIB RDC Sarl", "000000121212400", "IBANNN0012", "SWIFTCDK");
        exercice = new Exercice(12, entreprise.getId(), idUtilisateur, "Année Scolaire 2019-2020", new Date(), UtilLitige.getDate_AjouterAnnee(new Date(), 1), InterfaceExercice.BETA_EXISTANT);
    
        classeCM1 = new Classe(1, idUtilisateur, entreprise.getId(), exercice.getId(), "CM1", 50, "Local 01", UtilLitige.generateSignature(), InterfaceClasse.BETA_EXISTANT);
        classeCM2 = new Classe(2, idUtilisateur, entreprise.getId(), exercice.getId(), "CM2", 50, "Local 02", UtilLitige.generateSignature(), InterfaceClasse.BETA_EXISTANT);
        classeCE1 = new Classe(3, idUtilisateur, entreprise.getId(), exercice.getId(), "CE1", 50, "Local 03", UtilLitige.generateSignature(), InterfaceClasse.BETA_EXISTANT);
        
        MONNAIE_USD = new Monnaie(20, entreprise.getId(), idUtilisateur, exercice.getId(), "Dollars Américains", "$", InterfaceMonnaie.NATURE_MONNAIE_ETRANGERE, 1620, UtilLitige.generateSignature(), InterfaceMonnaie.BETA_EXISTANT);
        MONNAIE_Euro = new Monnaie(22, entreprise.getId(), idUtilisateur, exercice.getId(), "Euro", "Euro", InterfaceMonnaie.NATURE_MONNAIE_ETRANGERE, 1800, UtilLitige.generateSignature(), InterfaceMonnaie.BETA_EXISTANT);
        MONNAIE_CDF = new Monnaie(21, entreprise.getId(), idUtilisateur, exercice.getId(), "Francs Congolais", "Fc", InterfaceMonnaie.NATURE_MONNAIE_LOCALE, 1, UtilLitige.generateSignature(), InterfaceMonnaie.BETA_EXISTANT);
    
        Trimestre01 = new Periode(1, entreprise.getId(), idUtilisateur, exercice.getId(), "1er Trimestree", exercice.getDebut(), exercice.getFin(), UtilLitige.generateSignature(), InterfacePeriode.BETA_EXISTANT);
        Trimestre02 = new Periode(2, entreprise.getId(), idUtilisateur, exercice.getId(), "2ème Trimestre", exercice.getDebut(), exercice.getFin(), UtilLitige.generateSignature(), InterfacePeriode.BETA_EXISTANT);
        Trimestre03 = new Periode(3, entreprise.getId(), idUtilisateur, exercice.getId(), "3ème Trimestre", exercice.getDebut(), exercice.getFin(), UtilLitige.generateSignature(), InterfacePeriode.BETA_EXISTANT);

        INSCRIPTION = new Frais(1, idUtilisateur, entreprise.getId(), exercice.getId(), MONNAIE_USD.getId(), 121212, "INSCRIPTION", "USD", 1, new Vector<LiaisonFraisClasse>(), liaisonInsription, 100, InterfaceFrais.BETA_EXISTANT);
        MINERVALE = new Frais(2, idUtilisateur, entreprise.getId(), exercice.getId(), MONNAIE_USD.getId(), 121212, "MINERVALE", "USD", 1, new Vector<LiaisonFraisClasse>(), liaisonMinervale, 100, InterfaceFrais.BETA_EXISTANT);
        TRAVAIL_MANUEL = new Frais(3, idUtilisateur, entreprise.getId(), exercice.getId(), MONNAIE_USD.getId(), 121212, "TRAVAIL MANUEL", "USD", 1, new Vector<LiaisonFraisClasse>(), liaisonTravManuel, 100, InterfaceFrais.BETA_EXISTANT);

        liaisonInsription.add(new LiaisonFraisPeriode(Trimestre01.getId(), Trimestre01.getNom(), 0000, 100));
        liaisonInsription.add(new LiaisonFraisPeriode(Trimestre02.getId(), Trimestre02.getNom(), 0000, 0));
        liaisonInsription.add(new LiaisonFraisPeriode(Trimestre03.getId(), Trimestre03.getNom(), 0000, 0));
        
        liaisonMinervale.add(new LiaisonFraisPeriode(Trimestre01.getId(), Trimestre01.getNom(), 0000, 50));
        liaisonMinervale.add(new LiaisonFraisPeriode(Trimestre02.getId(), Trimestre02.getNom(), 0000, 25));
        liaisonMinervale.add(new LiaisonFraisPeriode(Trimestre03.getId(), Trimestre03.getNom(), 0000, 25));

        liaisonTravManuel.add(new LiaisonFraisPeriode(Trimestre01.getId(), Trimestre01.getNom(), 0000, 100));
        liaisonTravManuel.add(new LiaisonFraisPeriode(Trimestre02.getId(), Trimestre02.getNom(), 0000, 0));
        liaisonTravManuel.add(new LiaisonFraisPeriode(Trimestre03.getId(), Trimestre03.getNom(), 0000, 0));
        
        eleveTONGO = new Eleve(120, entreprise.getId(), idUtilisateur, exercice.getId(), classeCE1.getId(), UtilLitige.generateSignature(), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "TONGO", "BATANGILA", "Christian", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);
        eleveSULA = new Eleve(121, entreprise.getId(), idUtilisateur, exercice.getId(), classeCE1.getId(), UtilLitige.generateSignature(), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "SULA", "BOSIO", "Serge", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);
        eleveOPOTHA = new Eleve(122, entreprise.getId(), idUtilisateur, exercice.getId(), classeCM1.getId(), UtilLitige.generateSignature(), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "OPOTHA", "LOFUNGULA", "Emmanuel", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);
        eleveMAKULA = new Eleve(123, entreprise.getId(), idUtilisateur, exercice.getId(), classeCE1.getId(), UtilLitige.generateSignature(), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "MAKULA", "BOFANDO", "Alain", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);

        ayantSULA = new Ayantdroit(1, entreprise.getId(), idUtilisateur, exercice.getId(), 121, "SULA BOSIO", new Vector<LiaisonFraisEleve>(), UtilLitige.generateSignature(), eleveSULA.getSignature(), InterfaceAyantDroit.BETA_EXISTANT);
        
        liaisonSULA_Inscription = new LiaisonFraisEleve(eleveSULA.getSignature(), INSCRIPTION.getId(), 0, MONNAIE_CDF.getId(), "CDF");
        liaisonSULA_Minervale = new LiaisonFraisEleve(eleveSULA.getSignature(), MINERVALE.getId(), 1000, MONNAIE_USD.getId(), "USD");
        liaisonSULA_TravMan = new LiaisonFraisEleve(eleveSULA.getSignature(), TRAVAIL_MANUEL.getId(), 0, MONNAIE_USD.getId(), "USD");
        
        paiementOPOTHA_Inscription = new Paiement(1, eleveOPOTHA.getId(), INSCRIPTION.getId(), Trimestre01.getId(), "OPOTHA", INSCRIPTION.getNom(), "OPOTHA", 10000, new Date(), InterfacePaiement.MODE_CAISSE, "DSER22445", InterfacePaiement.BETA_EXISTANT);
        paiementOPOTHA_Minervale01 = new Paiement(3, eleveOPOTHA.getId(), MINERVALE.getId(), Trimestre01.getId(), "OPOTHA", MINERVALE.getNom(), "OPOTHA", 100, new Date(), InterfacePaiement.MODE_CAISSE, "DSER22445", InterfacePaiement.BETA_EXISTANT);
        paiementSULA_Inscription02 = new Paiement(2, eleveSULA.getId(), INSCRIPTION.getId(), Trimestre01.getId(), "SULA", INSCRIPTION.getNom(), "SULA BOSIO", 5000, new Date(), InterfacePaiement.MODE_CAISSE, "DSER22445", InterfacePaiement.BETA_EXISTANT);
        paiementSULA_Minervale01 = new Paiement(3, eleveSULA.getId(), MINERVALE.getId(), Trimestre01.getId(), "SULA", MINERVALE.getNom(), "SULA BOSIO", 500, new Date(), InterfacePaiement.MODE_CAISSE, "DSER22445", InterfacePaiement.BETA_EXISTANT);
    
    }

    private ParametresLitige getParametres() {
        Vector<Classe> listeClasse = new Vector<>();
        listeClasse.addElement(classeCM1);
        listeClasse.addElement(classeCM2);
        listeClasse.addElement(classeCE1);
        
        //On charge les paramètres
        Vector<Frais> listeFrais = new Vector<>();
        listeFrais.add(INSCRIPTION);
        listeFrais.add(MINERVALE);
        listeFrais.add(TRAVAIL_MANUEL);

        Vector<Monnaie> listeMonnaies = new Vector();
        listeMonnaies.addElement(MONNAIE_USD);
        listeMonnaies.addElement(MONNAIE_CDF);
        listeMonnaies.addElement(MONNAIE_Euro);

        Vector<Periode> listePeriodes = new Vector<>();
        listePeriodes.add(Trimestre01);
        listePeriodes.add(Trimestre02);
        listePeriodes.add(Trimestre03);

        //return new ParametresFacture(idFacture, numeroFacture, idUtilisateur, nomUtilisateur, entreprise, exercice, MONNAIE_USD, listeMonnaies, listeClasse);
        return new ParametresLitige(idUtilisateur, nomUtilisateur, entreprise, exercice, MONNAIE_USD, listeMonnaies, listeClasse, listeFrais, listePeriodes);
    }

    private DonneesLitige getDonnees() {
        //On charge les données
        Vector<Eleve> listeEleves = new Vector<>();
        listeEleves.add(eleveTONGO);
        listeEleves.add(eleveSULA);
        listeEleves.add(eleveOPOTHA);
        listeEleves.add(eleveMAKULA);

        Vector<Ayantdroit> listeAyantDroits = new Vector<>();
        
        Vector<LiaisonFraisEleve> liaisonsAyantDroitSULA = new Vector<>();
        liaisonsAyantDroitSULA.add(liaisonSULA_Minervale);
        liaisonsAyantDroitSULA.add(liaisonSULA_Inscription);
        liaisonsAyantDroitSULA.add(liaisonSULA_TravMan);
        
        Vector<Paiement> listePaiements = new Vector<>();
        listePaiements.add(paiementOPOTHA_Inscription);
        listePaiements.add(paiementOPOTHA_Minervale01);
        listePaiements.add(paiementSULA_Inscription02);
        listePaiements.add(paiementSULA_Minervale01);
        //Serge
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
        CouleurBasique couleurs = new CouleurBasique();

        this.panelLitige = new PanelLitige(couleurs, jTabbedPane1, getDonnees(), getParametres());

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
            java.util.logging.Logger.getLogger(PrincipalLitige.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalLitige.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalLitige.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalLitige.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrincipalLitige().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
