/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import ICONES.Icones;
import SOURCES.EcouteurLitiges.EcouteurLitiges;
import SOURCES.UI.PanelLitige;
import SOURCES.Utilitaires.CalculateurLitiges;
import SOURCES.Utilitaires.DataLitiges;
import java.util.Date;
import java.util.Vector;
import SOURCES.Utilitaires.ParametresLitige;
import SOURCES.Utilitaires.UtilLitige;
import Source.Callbacks.ConstructeurCriteres;
import Source.Callbacks.EcouteurCrossCanal;
import Source.Callbacks.EcouteurFreemium;
import Source.Callbacks.EcouteurNavigateurPages;
import Source.Interface.InterfaceAyantDroit;
import Source.Interface.InterfaceClasse;
import Source.Interface.InterfaceEleve;
import Source.Interface.InterfaceFrais;
import Source.Interface.InterfaceLitige;
import Source.Interface.InterfaceMonnaie;
import Source.Interface.InterfacePeriode;
import Source.Interface.InterfaceUtilisateur;
import Source.Objet.Ayantdroit;
import Source.Objet.Classe;
import Source.Objet.CouleurBasique;
import Source.Objet.Echeance;
import Source.Objet.Eleve;
import Source.Objet.Entreprise;
import Source.Objet.Annee;
import Source.Objet.Frais;
import Source.Objet.LiaisonFraisClasse;
import Source.Objet.LiaisonFraisEleve;
import Source.Objet.LiaisonFraisPeriode;
import Source.Objet.Litige;
import Source.Objet.Monnaie;
import Source.Objet.Paiement;
import Source.Objet.Periode;
import Source.Objet.UtilObjet;
import Source.Objet.Utilisateur;
import Source.UI.NavigateurPages;
import Sources.CHAMP_LOCAL;
import Sources.PROPRIETE;
import Sources.UI.JS2BPanelPropriete;
import Source.Interface.InterfaceAnnee;

/**
 *
 * @author HP Pavilion
 */
public class PrincipalLitige extends javax.swing.JFrame {

    //Classe
    public Classe classe_CM1 = null;
    public Classe classe_CM2 = null;

    //Monnaie
    public Monnaie monnaie_USD = null;
    public Monnaie monnaie_CDF = null;

    //Frais
    public Frais frais_inscription = null;
    public Frais frais_minervale = null;

    //Eleves
    public Eleve eleve_SULA_BOSIO = null;
    public Eleve eleve_OPOTHA_LOFUNGULA = null;

    //Ayant droit
    public Ayantdroit ayantdroit_SULA_BOSIO = null;

    //Type des périodes
    public Periode periode_Trimestre01 = null;
    public Periode periode_Trimestre02 = null;

    public PanelLitige panelLitige = null;
    public Icones icones = null;

    public Vector<Classe> listeClasse = new Vector<>();
    public Vector<Frais> listeFrais = new Vector<>();
    public Vector<Monnaie> listeMonnaies = new Vector();
    public Vector<Periode> listePeriodes = new Vector<>();
    public Vector<Eleve> listeEleves = new Vector<>();
    public Vector<Ayantdroit> listeAyantDroit = new Vector<>();
    public Vector<Paiement> listepaPaiements = new Vector<>();

    public Entreprise entreprise = new Entreprise(1, "ECOLE CARESIENNE DE KINSHASA", "7e Rue Limeté Industrielle, Kinshasa/RDC", "+243844803514", "infos@cartesien.org", "wwww.cartesien.org", "logo.png", "RCCM/KD/CD/4513", "IDN00111454", "IMP00124100", "Equity Bank Congo SA", "AIB RDC Sarl", "000000121212400", "IBANNN0012", "SWIFTCDK");
    public Utilisateur utilisateur = new Utilisateur(12, entreprise.getId(), "SULA", "BOSIO", "Serge", "sulabosiog@gmail.com", "abc", InterfaceUtilisateur.TYPE_ADMIN, UtilLitige.generateSignature(), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT);
    public Annee exercice = new Annee(12, entreprise.getId(), utilisateur.getId(), "Année 2019-2020", new Date(), UtilLitige.getDate_AjouterAnnee(new Date(), 1), UtilObjet.getSignature(), InterfaceAnnee.BETA_EXISTANT);

    /**
     * Creates new form TestPrincipal
     */
    public PrincipalLitige() {
        initComponents();
        icones = new Icones();
        this.setIconImage(icones.getAdresse_03().getImage());
    }

    private void initDonnees() {
        listeEleves.removeAllElements();
        listeAyantDroit.removeAllElements();
        listepaPaiements.removeAllElements();

        eleve_SULA_BOSIO = new Eleve(121, entreprise.getId(), utilisateur.getId(), exercice.getId(), classe_CM1.getId(), UtilLitige.generateSignature(), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "SULA", "BOSIO", "Serge", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);
        eleve_OPOTHA_LOFUNGULA = new Eleve(122, entreprise.getId(), utilisateur.getId(), exercice.getId(), classe_CM1.getId(), UtilLitige.generateSignature(), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "OPOTHA", "LOFUNGULA", "Emmanuel", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);

        Vector<LiaisonFraisEleve> lfeSULA = new Vector<>();
        lfeSULA.add(new LiaisonFraisEleve(eleve_SULA_BOSIO.getSignature(), frais_inscription.getSignature(), frais_inscription.getId(), 0, monnaie_USD.getId(), "USD"));
        lfeSULA.add(new LiaisonFraisEleve(eleve_SULA_BOSIO.getSignature(), frais_inscription.getSignature(), frais_minervale.getId(), 0, monnaie_USD.getId(), "USD"));

        ayantdroit_SULA_BOSIO = new Ayantdroit(1, entreprise.getId(), utilisateur.getId(), exercice.getId(), eleve_SULA_BOSIO.getId(), eleve_SULA_BOSIO.getNom(), lfeSULA, UtilLitige.generateSignature(), eleve_SULA_BOSIO.getSignature(), InterfaceAyantDroit.BETA_EXISTANT);

        listeEleves.add(eleve_SULA_BOSIO);
        listeEleves.add(eleve_OPOTHA_LOFUNGULA);

        listeAyantDroit.add(ayantdroit_SULA_BOSIO);
    }

    public ParametresLitige getParametreLitige() {
        entreprise = new Entreprise(1, "ECOLE CARESIENNE DE KINSHASA", "7e Rue Limeté Industrielle, Kinshasa/RDC", "+243844803514", "infos@cartesien.org", "wwww.cartesien.org", "logo.png", "RCCM/KD/CD/4513", "IDN00111454", "IMP00124100", "Equity Bank Congo SA", "AIB RDC Sarl", "000000121212400", "IBANNN0012", "SWIFTCDK");
        exercice = new Annee(12, entreprise.getId(), 1, "Année Scolaire 2019-2020", new Date(), UtilLitige.getDate_AjouterAnnee(new Date(), 1), UtilObjet.getSignature(), InterfaceAnnee.BETA_EXISTANT);
        utilisateur = new Utilisateur(1, entreprise.getId(), "SULA", "BOSIO", "SERGE", "sulabosiog@gmail.com", "abc", InterfaceUtilisateur.TYPE_ADMIN, UtilLitige.generateSignature(), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT);

        classe_CM1 = new Classe(1, utilisateur.getId(), entreprise.getId(), exercice.getId(), "CM1", 50, "Local 01", UtilLitige.generateSignature(), InterfaceClasse.BETA_EXISTANT);
        classe_CM2 = new Classe(2, utilisateur.getId(), entreprise.getId(), exercice.getId(), "CM2", 50, "Local 02", UtilLitige.generateSignature(), InterfaceClasse.BETA_EXISTANT);

        monnaie_USD = new Monnaie(20, entreprise.getId(), utilisateur.getId(), exercice.getId(), "Dollars Américains", "$", InterfaceMonnaie.NATURE_MONNAIE_ETRANGERE, 1620, UtilLitige.generateSignature(), InterfaceMonnaie.BETA_EXISTANT);
        monnaie_CDF = new Monnaie(21, entreprise.getId(), utilisateur.getId(), exercice.getId(), "Francs Congolais", "Fc", InterfaceMonnaie.NATURE_MONNAIE_LOCALE, 1, UtilLitige.generateSignature(), InterfaceMonnaie.BETA_EXISTANT);

        periode_Trimestre01 = new Periode(1, entreprise.getId(), utilisateur.getId(), exercice.getId(), "1er Trimestree", exercice.getDebut(), exercice.getFin(), UtilLitige.generateSignature(), InterfacePeriode.BETA_EXISTANT);
        periode_Trimestre02 = new Periode(2, entreprise.getId(), utilisateur.getId(), exercice.getId(), "2ème Trimestre", exercice.getDebut(), exercice.getFin(), UtilLitige.generateSignature(), InterfacePeriode.BETA_EXISTANT);

        Vector<LiaisonFraisClasse> l_c_inscr = new Vector<>();
        l_c_inscr.add(new LiaisonFraisClasse(classe_CM1.getId(), "CM1A", classe_CM1.getSignature(), 100));
        l_c_inscr.add(new LiaisonFraisClasse(classe_CM2.getId(), "CM2A", classe_CM2.getSignature(), 100));

        Vector<LiaisonFraisPeriode> l_p_inscr = new Vector<>();
        l_p_inscr.add(new LiaisonFraisPeriode(periode_Trimestre01.getId(), periode_Trimestre01.getNom() + "AAA", periode_Trimestre01.getSignature(), 100));
        l_p_inscr.add(new LiaisonFraisPeriode(periode_Trimestre02.getId(), periode_Trimestre02.getNom() + "AAA", periode_Trimestre02.getSignature(), 0));

        frais_inscription = new Frais(1, utilisateur.getId(), entreprise.getId(), exercice.getId(), monnaie_USD.getId(), 100, "INSCRIPTION", "USD", monnaie_USD.getSignature(), UtilLitige.generateSignature(), InterfaceFrais.BETA_EXISTANT);
        frais_inscription.setLiaisonsClasses(l_c_inscr);
        frais_inscription.setLiaisonsPeriodes(l_p_inscr);
        
        //frais_inscription = new Frais(1, utilisateur.getId(), entreprise.getId(), exercice.getId(), monnaie_USD.getId(), monnaie_USD.getSignature(), UtilLitige.generateSignature(), "INSCRIPTION", "USD", 1, l_c_inscr, l_p_inscr, 100, InterfaceFrais.BETA_EXISTANT);

        Vector<LiaisonFraisClasse> l_c_min = new Vector<>();
        l_c_min.add(new LiaisonFraisClasse(classe_CM1.getId(), "CM1", classe_CM1.getSignature(), 100));
        l_c_min.add(new LiaisonFraisClasse(classe_CM2.getId(), "CM2", classe_CM2.getSignature(), 100));

        Vector<LiaisonFraisPeriode> l_p_min = new Vector<>();
        l_p_min.add(new LiaisonFraisPeriode(periode_Trimestre01.getId(), periode_Trimestre01.getNom() + "AAA", periode_Trimestre01.getSignature(), 50));
        l_p_min.add(new LiaisonFraisPeriode(periode_Trimestre02.getId(), periode_Trimestre02.getNom() + "AAA", periode_Trimestre02.getSignature(), 50));

        frais_minervale = new Frais(2, utilisateur.getId(), entreprise.getId(), exercice.getId(), monnaie_USD.getId(), 500, "MINERVALE", "USD", monnaie_USD.getSignature(), UtilLitige.generateSignature(), InterfaceFrais.BETA_EXISTANT);
        frais_minervale.setLiaisonsClasses(l_c_min);
        frais_minervale.setLiaisonsPeriodes(l_p_min);

        //frais_minervale = new Frais(2, utilisateur.getId(), entreprise.getId(), exercice.getId(), monnaie_USD.getId(), monnaie_USD.getSignature(), UtilLitige.generateSignature(), "MINERVALE", "USD", 1, l_c_min, l_p_min, 500, InterfaceFrais.BETA_EXISTANT);

        listeClasse.addElement(classe_CM1);
        listeClasse.addElement(classe_CM2);

        listeFrais.add(frais_inscription);
        listeFrais.add(frais_minervale);

        listeMonnaies.addElement(monnaie_USD);
        listeMonnaies.addElement(monnaie_CDF);

        listePeriodes.add(periode_Trimestre01);
        listePeriodes.add(periode_Trimestre02);

        return new ParametresLitige(utilisateur, entreprise, exercice, monnaie_USD, listeMonnaies, listeClasse, listeFrais, listePeriodes);

    }

    private void initParametres() {
        ParametresLitige parametresLitige = getParametreLitige();

        this.panelLitige = new PanelLitige(new EcouteurLitiges() {
            @Override
            public void onClose() {
                
            }
        }, new EcouteurFreemium() {
            @Override
            public boolean onVerifie() {
                return true;
            }

            @Override
            public boolean onVerifieNombre(String nomTable) {
                return true;
            }
        }, new CouleurBasique(), jTabbedPane1, new DataLitiges(parametresLitige), null, new EcouteurCrossCanal() {
            @Override
            public void onOuvrirInscription(Eleve eleve) {
                System.out.println("DEMARRAGE DE LA FICHE D'INSCRIPTION DE L'ELEVE " + eleve.getNom());
            }

            @Override
            public void onOuvrirPaiements(Eleve eleve) {
                System.out.println("DEMARRAGE DE LA FICHE DE PAIEMENT DE L'ELEVE " + eleve.getNom());
            }

            @Override
            public void onOuvrirLitiges(Eleve eleve) {
                System.out.println("DEMARRAGE DE LA FICHE DES LITIGES DE L'ELEVE " + eleve.getNom());
            }

        });

    }

    public boolean checkCriteres(String motCle, Object data, JS2BPanelPropriete jsbpp) {
        Eleve eleve = (Eleve) data;
        boolean repClasse = true;
        boolean repMotCle = panelLitige.verifierNomEleve(motCle, eleve);
        int idFrais = -1;
        int idPeriode = -1;
        int idSolvabilite = -1;
        Litige litige = null;
        Ayantdroit aya = null;

        if (jsbpp != null) {
            PROPRIETE propClasse = jsbpp.getPropriete("Classe");
            repClasse = panelLitige.verifierClasse(propClasse.getValeurSelectionne() + "", eleve);
            //System.out.println(propClasse.getValeurSelectionne() + "");

            PROPRIETE propFrais = jsbpp.getPropriete("Frais");
            System.out.println(propFrais.getValeurSelectionne() + "");
            if ((propFrais.getValeurSelectionne() + "").trim().length() != 0) {
                Frais frs = panelLitige.getFrais(propFrais.getValeurSelectionne() + "");
                if (frs != null) {
                    idFrais = frs.getId();
                }
            }

            PROPRIETE propPeriode = jsbpp.getPropriete("Période");
            //System.out.println(propPeriode.getValeurSelectionne() + "");
            if ((propPeriode.getValeurSelectionne() + "").trim().length() != 0) {
                Periode prd = panelLitige.getPeriode(propPeriode.getValeurSelectionne() + "");
                if (prd != null) {
                    idPeriode = prd.getId();
                }
            }

            PROPRIETE propSolvabilite = jsbpp.getPropriete("Solvabilité");
            //System.out.println(propSolvabilite.getValeurSelectionne() + "");
            String valSolva = (propSolvabilite.getValeurSelectionne() + "");
            if (valSolva.trim().length() != 0) {
                if (valSolva.equals("SOLVABLES")) {
                    idSolvabilite = 0;
                } else if (valSolva.equals("INSOLVABLES")) {
                    idSolvabilite = 1;
                }
            }

        }

        if (repMotCle == true && repClasse == true) {
            //On cherche si cet eleve est un ayantdroit
            for (Ayantdroit ayd : listeAyantDroit) {
                if (ayd.getIdEleve() == eleve.getId()) {
                    aya = ayd;
                }
            }

            Vector<Paiement> paiementsEleveEncours = new Vector<>();
            //On cherche tous les payements effectués par cet élève
            for (Paiement pymt : listepaPaiements) {
                if (eleve.getId() == pymt.getIdEleve()) {
                    paiementsEleveEncours.add(pymt);
                }
            }
            Vector<Echeance> listeEcheances = CalculateurLitiges.getEcheances(idSolvabilite, idFrais, idPeriode, eleve, aya, paiementsEleveEncours, panelLitige.getParametresLitige());
            if (listeEcheances != null) {
                if (!listeEcheances.isEmpty()) {
                    litige = new Litige(1, eleve.getId(), eleve.getIdClasse(), listeEcheances, InterfaceLitige.BETA_EXISTANT);
                }
            }
            panelLitige.setDonneesLitiges(litige, eleve, aya);
            return true;
        } else {
            panelLitige.setDonneesLitiges(litige, eleve, aya);
            return false;
        }

    }

    private void chercherEleves(String motCle, int taillePage, JS2BPanelPropriete criteresAvances) {
        int index = 1;
        for (Eleve ee : listeEleves) {
            //System.out.println(" ** " + ee.toString());
            if (index == taillePage) {
                break;
            }
            boolean checkCritere = checkCriteres(motCle, ee, criteresAvances);

            index++;
        }
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
        Icones icones = new Icones();
        initParametres();
        initDonnees();
        if (panelLitige != null) {
            NavigateurPages navigateur = panelLitige.getNavigateurPage();
            navigateur.initialiser(this, new EcouteurNavigateurPages() {
                @Override
                public void onRecharge(String motCle, int pageActuelle, int taillePage, JS2BPanelPropriete criteresAvances) {
                    new Thread() {
                        public void run() {
                            navigateur.setInfos(100, 10);
                            navigateur.patienter(true, "Chargement...");
                            panelLitige.reiniliserLitige();
                            chercherEleves(motCle, taillePage, criteresAvances);
                        }
                    }.start();
                }
            }, new ConstructeurCriteres() {
                @Override
                public JS2BPanelPropriete onInitialise() {
                    JS2BPanelPropriete panProp = new JS2BPanelPropriete(icones.getFiltrer_01(), "Critères avancés", true);
                    panProp.viderListe();

                    //Critres classes
                    Vector listeClasses = new Vector();
                    listeClasses.add("TOUTES");
                    for (Classe cl : panelLitige.getParametresLitige().getListeClasse()) {
                        listeClasses.add(cl.getNom());
                    }
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getClasse_01(), "Classe", "cls", listeClasses, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);

                    //Critres Frais
                    Vector listeFrais = new Vector();
                    listeFrais.add("TOUS");
                    for (Frais cl : panelLitige.getParametresLitige().getListeFraises()) {
                        listeFrais.add(cl.getNom());
                    }
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getTaxes_01(), "Frais", "cls", listeFrais, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);

                    //Critres Période
                    Vector listePeriodes = new Vector();
                    listePeriodes.add("TOUTES");
                    for (Periode per : panelLitige.getParametresLitige().getListePeriodes(-1)) {
                        listePeriodes.add(per.getNom());
                    }
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getCalendrier_01(), "Période", "cls", listePeriodes, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);

                    Vector listeSolvabilite = new Vector();
                    listeSolvabilite.add("TOUS");
                    listeSolvabilite.add("SOLVABLES");
                    listeSolvabilite.add("INSOLVABLES");
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getRecette_01(), "Solvabilité", "cls", listeSolvabilite, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);

                    return panProp;
                }
            });

            jTabbedPane1.add("Facture", panelLitige);
            jTabbedPane1.setSelectedComponent(panelLitige);

            navigateur.reload();
        }
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
