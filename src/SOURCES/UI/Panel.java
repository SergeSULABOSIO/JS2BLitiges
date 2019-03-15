/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI;

import BEAN_BARRE_OUTILS.BarreOutils;
import BEAN_BARRE_OUTILS.Bouton;
import BEAN_BARRE_OUTILS.BoutonListener;
import BEAN_MenuContextuel.MenuContextuel;
import BEAN_MenuContextuel.RubriqueListener;
import BEAN_MenuContextuel.RubriqueSimple;
import ICONES.Icones;
import SOURCES.CallBack.EcouteurUpdateClose;
import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceAyantDroit;
import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceEleve;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceLitige;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.ModelesTables.ModeleListeLitiges;
import SOURCES.MoteurRecherche.MoteurRecherche;
import SOURCES.RendusTables.RenduTableLitiges;
import SOURCES.Utilitaires.DonneesLitige;
import SOURCES.Utilitaires.ParametresLitige;
import SOURCES.Utilitaires.Util;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author HP Pavilion
 */
public class Panel extends javax.swing.JPanel {

    /**
     * Creates new form Panel
     */
    private Icones icones = null;
    private final JTabbedPane parent;
    private Panel moi = null;
    private EcouteurUpdateClose ecouteurClose = null;
    public Bouton btImprimer, btPDF, btFermer, btActualiser, btPDFSynth;        //btEnregistrer, btAjouter, btSupprimer, btVider, 
    public RubriqueSimple mImprimer, mPDF, mFermer, mActualiser, mPDFSynth;     //mEnregistrer, mAjouter, mSupprimer, mVider, 
    private MenuContextuel menuContextuel = null;
    private BarreOutils bOutils = null;

    private ModeleListeLitiges modeleListeLitiges;
    private MoteurRecherche gestionnaireRecherche = null;
    public int indexTabSelected = -1;
    public DonneesLitige donneesLitige;
    public ParametresLitige parametresLitige;
    public double totMontantDu, totMontantPaye, totMontantNet = 0;
    public double totMontantDuSelected, totMontantPayeSelected, totMontantNetSelected = 0;

    public String monnaieOutput = "";
    public static final int TYPE_EXPORT_TOUT = 0;
    public static final int TYPE_EXPORT_SELECTION = 1;
    public int typeExport = TYPE_EXPORT_TOUT;

    private InterfaceLitige SelectedLitige = null;
    private InterfaceEleve SelectedEleve = null;
    private InterfaceAyantDroit SelectedAyantDroit = null;
    

    public Panel(JTabbedPane parent, DonneesLitige donneesLitige, ParametresLitige parametresLitige) {
        this.initComponents();
        this.icones = new Icones();
        this.parent = parent;
        this.init();
        this.donneesLitige = donneesLitige;
        this.parametresLitige = parametresLitige;

        //Initialisaterus
        parametrerTableLitiges();
        setIconesTabs();

        initMonnaieTotaux();
        actualiserTotaux();

        initComposantsMoteursRecherche();
        activerMoteurRecherche();
        ecouterLitigeSelectionne();
    }

    private void initComposantsMoteursRecherche() {
        //Composants du moteur de recherche
        chRecherche.setTextInitial("Recherche : Saisissez le nom de l'élève par ici");
        chFrais.removeAllItems();
        chFrais.addItem("TOUT TYPE DE FRAIS");
        for (InterfaceArticle Iarticle : parametresLitige.getArticles()) {
            chFrais.addItem(Iarticle.getNom());
        }
        chClasse.removeAllItems();
        chClasse.addItem("TOUTES LES CLASSES");
        for (InterfaceClasse Iclasse : parametresLitige.getListeClasse()) {
            chClasse.addItem(Iclasse.getNom() + ", " + Iclasse.getNomLocal());
        }
    }

    private void activerMoteurRecherche() {
        gestionnaireRecherche = new MoteurRecherche(icones, chRecherche, ecouteurClose) {

            @Override
            public void chercher(String motcle) {
                //On extrait les critère de filtrage des Encaissements
                modeleListeLitiges.chercher(chRecherche.getText(), -1, -1);
                actualiserTotaux("activerMoteurRecherche");
            }
        };
    }

    public InterfaceEntreprise getEntreprise() {
        return parametresLitige.getEntreprise();
    }

    public String getNomUtilisateur() {
        return parametresLitige.getNomUtilisateur();
    }

    public String getTitreDoc() {
        if (typeExport == Panel.TYPE_EXPORT_SELECTION) {
            return "LITIGES";
        } else {
            return "LITIGE - LISTE GLOBALE";
        }
    }

    public Date getDateDocument() {
        return new Date();
    }

    public int getTypeExport() {
        return typeExport;
    }

    private void initMonnaieTotaux() {
        String labTaux = "Taux de change: ";
        InterfaceMonnaie monnaieLocal = null;
        combototMonnaie.removeAllItems();
        for (InterfaceMonnaie monnaie : parametresLitige.getListeMonnaies()) {
            combototMonnaie.addItem(monnaie.getCode() + " - " + monnaie.getNom());
            if (monnaie.getTauxMonnaieLocale() == 1) {
                monnaieLocal = monnaie;
            }
        }
        for (InterfaceMonnaie monnaie : parametresLitige.getListeMonnaies()) {
            if (monnaie != monnaieLocal) {
                labTaux += " 1 " + monnaie.getCode() + " = " + Util.getMontantFrancais(monnaie.getTauxMonnaieLocale()) + " " + monnaieLocal.getCode() + ", ";
            }
        }
        labTauxDeChange.setText(labTaux);
    }

    private InterfaceMonnaie getSelectedMonnaieTotaux() {
        for (InterfaceMonnaie monnaie : parametresLitige.getListeMonnaies()) {
            if ((monnaie.getCode() + " - " + monnaie.getNom()).equals(combototMonnaie.getSelectedItem() + "")) {
                return monnaie;
            }
        }
        return null;
    }

    private InterfaceMonnaie getMonnaie(int idMonnaie) {
        for (InterfaceMonnaie monnaie : parametresLitige.getListeMonnaies()) {
            if (monnaie.getId() == idMonnaie) {
                return monnaie;
            }
        }
        return null;
    }

    private void getMontants(InterfaceMonnaie ImonnaieOutput, InterfaceLitige Ilitige) {
        if (Ilitige != null && ImonnaieOutput != null) {
            for (InterfaceEcheance Iecheance : Ilitige.getListeEcheances()) {
                if (ImonnaieOutput.getId() == Iecheance.getIdMonnaie()) {
                    totMontantDu += Iecheance.getMontantDu();
                    totMontantPaye += Iecheance.getMontantPaye();
                    totMontantNet += (Iecheance.getMontantDu() - Iecheance.getMontantPaye());
                } else {
                    InterfaceMonnaie ImonnaieOrigine = getMonnaie(Iecheance.getIdMonnaie());
                    if (ImonnaieOrigine != null) {
                        totMontantDu += (Iecheance.getMontantDu() * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                        totMontantPaye += (Iecheance.getMontantPaye() * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                        totMontantNet += ((Iecheance.getMontantDu() - Iecheance.getMontantPaye()) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                    }
                }
            }
        }
    }

    private void getMontantsSelection(InterfaceMonnaie ImonnaieOutput, InterfaceLitige Ilitige) {
        if (Ilitige != null && ImonnaieOutput != null) {
            for (InterfaceEcheance Iecheance : Ilitige.getListeEcheances()) {
                if (ImonnaieOutput.getId() == Iecheance.getIdMonnaie()) {
                    totMontantDuSelected += Iecheance.getMontantDu();
                    totMontantPayeSelected += Iecheance.getMontantPaye();
                    totMontantNetSelected += (Iecheance.getMontantDu() - Iecheance.getMontantPaye());
                } else {
                    InterfaceMonnaie ImonnaieOrigine = getMonnaie(Iecheance.getIdMonnaie());
                    if (ImonnaieOrigine != null) {
                        totMontantDuSelected += (Iecheance.getMontantDu() * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                        totMontantPayeSelected += (Iecheance.getMontantPaye() * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                        totMontantNetSelected += ((Iecheance.getMontantDu() - Iecheance.getMontantPaye()) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                    }
                }
            }
        }
    }

    private void reinitMontantsTotaux() {
        //Montant brut
        totMontantDu = 0;
        totMontantDuSelected = 0;
        
        //Montant payé
        totMontantPaye = 0;
        totMontantPayeSelected = 0;
        
        //Net à payer (Solde restant dû)
        totMontantNet = 0;
        totMontantNetSelected = 0;
    }

    private void actualiserTotaux() {
        reinitMontantsTotaux();

        //Pour les Montants globaux (Montant du, montant payé et Reste à payer)
        InterfaceMonnaie ImonnaieOutput = null;
        if (modeleListeLitiges != null) {
            ImonnaieOutput = getSelectedMonnaieTotaux();
            for (InterfaceLitige iFiche : modeleListeLitiges.getListeData()) {
                getMontants(ImonnaieOutput, iFiche);
            }
        }

        //Pour la selection
        int[] tabLignesSelected = tableListeFichesDePaie.getSelectedRows();
        for (int i = 0; i < tabLignesSelected.length; i++) {
            if (tabLignesSelected[i] != -1) {
                if (modeleListeLitiges != null) {
                    InterfaceLitige iFiche = modeleListeLitiges.getLitiges(tabLignesSelected[i]);
                    if (iFiche != null && ImonnaieOutput != null) {
                        getMontantsSelection(ImonnaieOutput, iFiche);
                    }
                }
            }
        }

        if (ImonnaieOutput != null) {
            monnaieOutput = ImonnaieOutput.getCode();
        }

        //Le global
        valMontDu.setText(Util.getMontantFrancais(totMontantDu) + " " + monnaieOutput);
        valMontPaye.setText(Util.getMontantFrancais(totMontantPaye) + " " + monnaieOutput);
        valMontReste.setText(Util.getMontantFrancais(totMontantNet) + " " + monnaieOutput);

        //L'élements séléctionné
        valMontDuSelect.setText(Util.getMontantFrancais(totMontantDuSelected) + " " + monnaieOutput);
        valMontPayeSelect.setText(Util.getMontantFrancais(totMontantPayeSelected) + " " + monnaieOutput);
        valMontResteSelect.setText(Util.getMontantFrancais(totMontantNetSelected) + " " + monnaieOutput);
    }

    public double getTotMontantDu() {
        return totMontantDu;
    }

    public double getTotMontantPaye() {
        return totMontantPaye;
    }

    public double getTotMontantNet() {
        return totMontantNet;
    }

    public double getTotMontantDuSelected() {
        return totMontantDuSelected;
    }

    public double getTotMontantPayeSelected() {
        return totMontantPayeSelected;
    }

    public double getTotMontantNetSelected() {
        return totMontantNetSelected;
    }

    public String getMonnaieOutput() {
        return this.monnaieOutput;
    }

    public String getTauxChange() {
        return labTauxDeChange.getText();
    }

    public int getCritereClasse() {
        return getClasse((chClasse.getSelectedItem() + "").trim());
    }
    
    public int getCritereFrais() {
        return getFrais((chFrais.getSelectedItem() + "").trim());
    }
    
    private int getClasse(String valeur){
        for(InterfaceClasse Icls: parametresLitige.getListeClasse()){
            String nm = Icls.getNom() + ", " + Icls.getNomLocal();
            if(nm.equals(valeur)){
                return Icls.getId();
            }
        }
        return -1;
    }
    
    private int getFrais(String valeur){
        for(InterfaceArticle Iart: parametresLitige.getArticles()){
            if(Iart.getNom().equals(valeur)){
                return Iart.getId();
            }
        }
        return -1;
    }

    public String getCritereMois() {
        return chFrais.getSelectedItem() + "";
    }

    private void actualiserTotaux(String methode) {
        System.err.println(methode);
        actualiserTotaux();
    }

    private void parametrerTableLitiges() {
        initModelTableLitige();
        fixerColonnesTableLitige(true);
    }

    private void initModelTableLitige() {
        //C'est justement ici que l'on va charger les litiges après les avoir calculés
        this.modeleListeLitiges = new ModeleListeLitiges(scrollListeFichesDePaie, new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                if (ecouteurClose != null) {
                    ecouteurClose.onActualiser(modeleListeLitiges.getRowCount() + " élement(s).", icones.getDossier_01());
                }
            }
        });
        tableListeFichesDePaie.setModel(this.modeleListeLitiges);
    }

    private void fixerColonnesTableLitige(boolean resizeTable) {
        //Parametrage du rendu de la table
        this.tableListeFichesDePaie.setDefaultRenderer(Object.class, new RenduTableLitiges(icones.getModifier_01(), modeleListeLitiges, parametresLitige));
        this.tableListeFichesDePaie.setRowHeight(25);

        //{"N°", "Elève", "Classe", + Echeances};
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(0), 30, true, null);//N°
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(1), 200, true, null);//Elève
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(2), 100, false, null);//Classe

        if (modeleListeLitiges.getRowCount() != 0) {
            int nbEcheances = modeleListeLitiges.getListeData().firstElement().getListeEcheances().size();
            for (int i = 0; i < nbEcheances; i++) {
                setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(3 + i), 150, true, null);//NET A PAYER
            }
        }

        //On écoute les sélction
        this.tableListeFichesDePaie.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    ecouterLitigeSelectionne();
                    actualiserTotaux("ecouterSelection - Table Litiges");
                }
            }
        });

        if (resizeTable == true) {
            this.tableListeFichesDePaie.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }

    public InterfaceLitige getSelectedLitige() {
        return SelectedLitige;
    }

    private void ecouterLitigeSelectionne() {
        int ligneSelected = tableListeFichesDePaie.getSelectedRow();
        if (ligneSelected != -1) {
            this.SelectedLitige = modeleListeLitiges.getLitiges(ligneSelected);
            if (SelectedLitige != null) {
                this.SelectedEleve = getEleve(SelectedLitige.getIdEleve());
                if (SelectedAyantDroit != null) {
                    String nomEleveSelectionne = SelectedEleve.getNom() + " " + SelectedEleve.getPostnom() + " " + SelectedEleve.getPrenom();
                    btPDFSynth.setText("Prod. Fiche", 12, true);
                    btPDFSynth.appliquerDroitAccessDynamique(true);
                    mPDFSynth.setText("Produire la fiche de " + nomEleveSelectionne);
                    mPDFSynth.appliquerDroitAccessDynamique(true);
                    renameTitrePaneAgent("Sélection - " + nomEleveSelectionne);
                    
                    String brut = Util.getMontantFrancais(totMontantDuSelected) + " " + monnaieOutput;
                    String paye = Util.getMontantFrancais(totMontantPayeSelected) + " " + monnaieOutput;
                    String reste = Util.getMontantFrancais(totMontantNetSelected) + " " + monnaieOutput;
                    this.ecouteurClose.onActualiser(nomEleveSelectionne + ", Montant dû: " + brut + ", Payé: " + paye + " et " + reste + ".", icones.getClient_01());

                } else {
                    desactiverBts();
                }
            } else {
                desactiverBts();
            }
        } else {
            desactiverBts();
        }
    }

    private void renameTitrePaneAgent(String titre) {
        panSelected.setBorder(javax.swing.BorderFactory.createTitledBorder(null, titre, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13), new java.awt.Color(51, 51, 255))); // NOI18N
    }

    private void desactiverBts() {
        if (btPDFSynth != null && mPDFSynth != null) {
            btPDFSynth.appliquerDroitAccessDynamique(false);
            mPDFSynth.appliquerDroitAccessDynamique(false);
            mPDFSynth.setText("Produire le billetin");
            renameTitrePaneAgent("Sélection");
        }
    }

    private InterfaceEleve getEleve(int idEleve) {
        for (InterfaceEleve Icha : this.donneesLitige.getEleves()) {
            if (Icha.getId() == idEleve) {
                return Icha;
            }
        }
        return null;
    }
    
    private InterfaceAyantDroit getAyantDroit(int idEleve) {
        for (InterfaceAyantDroit Icha : this.donneesLitige.getAyantDroits()) {
            if (Icha.getIdEleve() == idEleve) {
                return Icha;
            }
        }
        return null;
    }

    
    private void setTaille(TableColumn column, int taille, boolean fixe, TableCellEditor editor) {
        column.setPreferredWidth(taille);
        if (fixe == true) {
            column.setMaxWidth(taille);
            column.setMinWidth(taille);
        }
        if (editor != null) {
            column.setCellEditor(editor);
        }
    }

    private void setBoutons() {
        btImprimer = new Bouton(12, "Imprimer", icones.getImprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                imprimer();
            }
        });

        btFermer = new Bouton(12, "Fermer", icones.getFermer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                fermer();
            }
        });

        btPDF = new Bouton(12, "Exp. Tout", icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                typeExport = TYPE_EXPORT_TOUT;
                exporterPDF();
            }
        });

        btPDFSynth = new Bouton(12, "Exp. bulletin", icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                typeExport = TYPE_EXPORT_SELECTION;
                exporterPDF();
            }
        });

        btActualiser = new Bouton(12, "Actualiser", icones.getSynchroniser_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                actualiser();
            }
        });

        bOutils = new BarreOutils(barreOutils);
        bOutils.AjouterSeparateur();
        bOutils.AjouterBouton(btActualiser);
        bOutils.AjouterSeparateur();
        bOutils.AjouterBouton(btImprimer);
        bOutils.AjouterBouton(btPDF);
        bOutils.AjouterBouton(btPDFSynth);
        bOutils.AjouterSeparateur();
        bOutils.AjouterBouton(btFermer);
    }

    private void ecouterMenContA(java.awt.event.MouseEvent evt, int tab) {
        if (evt.getButton() == MouseEvent.BUTTON3) {
            switch (tab) {
                case 0: //Tab Monnaie
                    menuContextuel.afficher(scrollListeFichesDePaie, evt.getX(), evt.getY());
                    break;
            }
        }
    }

    public void init() {
        this.moi = this;
        this.chRecherche.setIcon(icones.getChercher_01());
        this.labInfos.setIcon(icones.getInfos_01());
        this.labInfos.setText("Prêt.");

        this.ecouteurClose = new EcouteurUpdateClose() {
            @Override
            public void onFermer() {
                parent.remove(moi);
            }

            @Override
            public void onActualiser(String texte, ImageIcon icone) {
                labInfos.setText(texte);
                labInfos.setIcon(icone);
            }
        };
        
        setBoutons();
        setMenuContextuel();
    }

    public void activerBoutons(int selectedTab) {
        this.indexTabSelected = selectedTab;
        actualiserTotaux("activerBoutons");
        ecouterLitigeSelectionne();
    }

    private void setIconesTabs() {
        this.tabPrincipal.setIconAt(0, icones.getArgent_01());  //Liste des Fiches de paie
        this.llabMontDu.setIcon(icones.getNombre_01());
        this.llabMontantPaye.setIcon(icones.getNombre_01());
        this.llabMontantSolde.setIcon(icones.getNombre_01());
        this.llabMontantDuSelected.setIcon(icones.getNombre_01());
        this.llabMontantPayeSelected.setIcon(icones.getNombre_01());
        this.llabMontantResteSelected.setIcon(icones.getNombre_01());
    }

    private void setMenuContextuel() {
        mImprimer = new RubriqueSimple("Imprimer", 12, false, icones.getImprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                imprimer();
            }
        });

        mFermer = new RubriqueSimple("Fermer", 12, false, icones.getFermer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                fermer();
            }
        });

        mPDF = new RubriqueSimple("Exporter tout", 12, false, icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                typeExport = TYPE_EXPORT_TOUT;
                exporterPDF();
            }
        });

        mActualiser = new RubriqueSimple("Actualiser", 12, false, icones.getSynchroniser_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                actualiser();
            }
        });

        mPDFSynth = new RubriqueSimple("Export cette fiche de paie", 12, true, icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                typeExport = TYPE_EXPORT_SELECTION;
                exporterPDF();
            }
        });

        menuContextuel = new MenuContextuel();
        //menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(mActualiser);
        //menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(mImprimer);
        menuContextuel.Ajouter(mPDF);
        menuContextuel.Ajouter(mPDFSynth);
        menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(mFermer);
    }

    private boolean mustBeSaved() {
        boolean rep = false;
        //On vérifie dans la liste d'encaissements
        for (InterfaceLitige Ienc : modeleListeLitiges.getListeData()) {
            if (Ienc.getBeta() == InterfaceLitige.BETA_MODIFIE || Ienc.getBeta() == InterfaceLitige.BETA_NOUVEAU) {
                rep = true;
            }
        }
        return rep;
    }

    public void fermer() {
        //Vérifier s'il n'y a rien à enregistrer
        if (mustBeSaved() == true) {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous enregistrer les modifications et/ou ajouts apportés à ces données?", "Avertissement", JOptionPane.YES_NO_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.ecouteurClose.onFermer();
            } else if (dialogResult == JOptionPane.NO_OPTION) {
                this.ecouteurClose.onFermer();
            }
        } else {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir fermer cette fenêtre?", "Avertissement", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.ecouteurClose.onFermer();
            }
        }
    }

    public void imprimer() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir imprimer ce document?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                //SortiesFichesDePaies sortie = getSortiesFichesDePaies(btImprimer, mImprimer);
                //DocumentPDF documentPDF = new DocumentPDF(this, DocumentPDF.ACTION_IMPRIMER, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ParametresLitige getParametresLitige() {
        return parametresLitige;
    }

    
    public String getNomfichierPreuve() {
        return "FicheLitigeS2B.pdf";
    }


    public void exporterPDF() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous les exporter dans un fichier PDF?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                //SortiesFichesDePaies sortie = getSortiesFichesDePaies(btPDF, mPDF);
                //DocumentPDF docpdf = new DocumentPDF(this, DocumentPDF.ACTION_OUVRIR, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void actualiser() {
        switch (indexTabSelected) {
            case 0: //Encaissements
                modeleListeLitiges.actualiser();
                ecouterLitigeSelectionne();
                break;
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

        barreOutils = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        tabPrincipal = new javax.swing.JTabbedPane();
        scrollListeFichesDePaie = new javax.swing.JScrollPane();
        tableListeFichesDePaie = new javax.swing.JTable();
        labInfos = new javax.swing.JLabel();
        chRecherche = new UI.JS2bTextField();
        panelCriteres_categorie = new javax.swing.JPanel();
        chClasse = new javax.swing.JComboBox<>();
        chFrais = new javax.swing.JComboBox<>();
        panSelected = new javax.swing.JPanel();
        valMontDuSelect = new javax.swing.JLabel();
        valMontPayeSelect = new javax.swing.JLabel();
        valMontResteSelect = new javax.swing.JLabel();
        llabMontantDuSelected = new javax.swing.JLabel();
        llabMontantPayeSelected = new javax.swing.JLabel();
        llabMontantResteSelected = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        valMontDu = new javax.swing.JLabel();
        valMontPaye = new javax.swing.JLabel();
        valMontReste = new javax.swing.JLabel();
        llabMontDu = new javax.swing.JLabel();
        llabMontantPaye = new javax.swing.JLabel();
        llabMontantSolde = new javax.swing.JLabel();
        labTauxDeChange = new javax.swing.JLabel();
        combototMonnaie = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));

        barreOutils.setBackground(new java.awt.Color(255, 255, 255));
        barreOutils.setRollover(true);
        barreOutils.setAutoscrolls(true);
        barreOutils.setPreferredSize(new java.awt.Dimension(59, 61));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture02.png"))); // NOI18N
        jButton5.setText("Ajouter");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barreOutils.add(jButton5);

        tabPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        tabPrincipal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPrincipalStateChanged(evt);
            }
        });

        scrollListeFichesDePaie.setBackground(new java.awt.Color(255, 255, 255));
        scrollListeFichesDePaie.setAutoscrolls(true);
        scrollListeFichesDePaie.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollListeFichesDePaieMouseClicked(evt);
            }
        });

        tableListeFichesDePaie.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Article", "Qunatité", "Unités", "Prix Unitaire HT", "Tva %", "Tva", "Total TTC"
            }
        ));
        tableListeFichesDePaie.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tableListeFichesDePaieMouseDragged(evt);
            }
        });
        tableListeFichesDePaie.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListeFichesDePaieMouseClicked(evt);
            }
        });
        tableListeFichesDePaie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableListeFichesDePaieKeyReleased(evt);
            }
        });
        scrollListeFichesDePaie.setViewportView(tableListeFichesDePaie);

        tabPrincipal.addTab("Litiges", scrollListeFichesDePaie);

        labInfos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labInfos.setText("Prêt.");

        chRecherche.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        chRecherche.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        chRecherche.setTextInitial("Recherche");

        panelCriteres_categorie.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Autres critères", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        chClasse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TOUTES LES CLASSES" }));
        chClasse.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chClasseItemStateChanged(evt);
            }
        });

        chFrais.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TOUT TYPE DE FRAIS" }));
        chFrais.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chFraisItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelCriteres_categorieLayout = new javax.swing.GroupLayout(panelCriteres_categorie);
        panelCriteres_categorie.setLayout(panelCriteres_categorieLayout);
        panelCriteres_categorieLayout.setHorizontalGroup(
            panelCriteres_categorieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelCriteres_categorieLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chClasse, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chFrais, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCriteres_categorieLayout.setVerticalGroup(
            panelCriteres_categorieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCriteres_categorieLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panelCriteres_categorieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chFrais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panSelected.setBackground(new java.awt.Color(255, 255, 255));
        panSelected.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fiche de paie séléctionnée", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 255))); // NOI18N

        valMontDuSelect.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        valMontDuSelect.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontDuSelect.setText("0000000000 $ ");

        valMontPayeSelect.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        valMontPayeSelect.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontPayeSelect.setText("0000000000 $ ");

        valMontResteSelect.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        valMontResteSelect.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontResteSelect.setText("0000000000 $ ");

        llabMontantDuSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabMontantDuSelected.setText("Montant dû");

        llabMontantPayeSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabMontantPayeSelected.setText("Montant payé");

        llabMontantResteSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabMontantResteSelected.setText("Reste à payer");

        javax.swing.GroupLayout panSelectedLayout = new javax.swing.GroupLayout(panSelected);
        panSelected.setLayout(panSelectedLayout);
        panSelectedLayout.setHorizontalGroup(
            panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSelectedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(llabMontantDuSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(llabMontantPayeSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(llabMontantResteSelected, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(valMontResteSelect, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(valMontPayeSelect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(valMontDuSelect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        panSelectedLayout.setVerticalGroup(
            panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSelectedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontDuSelect)
                    .addComponent(llabMontantDuSelected))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontPayeSelect)
                    .addComponent(llabMontantPayeSelected))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontResteSelect)
                    .addComponent(llabMontantResteSelected))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Litiges", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 255))); // NOI18N

        valMontDu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        valMontDu.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontDu.setText("00000000 $");

        valMontPaye.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        valMontPaye.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontPaye.setText("00000000 $");

        valMontReste.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        valMontReste.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontReste.setText("00000000 $");

        llabMontDu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabMontDu.setText("Total - Montant dû");

        llabMontantPaye.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabMontantPaye.setText("Total - Montant payé");

        llabMontantSolde.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabMontantSolde.setText("Total - Reste à payer");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(llabMontantPaye, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(llabMontDu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(llabMontantSolde, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(valMontReste, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(valMontPaye, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(valMontDu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontDu)
                    .addComponent(llabMontDu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontPaye)
                    .addComponent(llabMontantPaye))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontReste)
                    .addComponent(llabMontantSolde))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labTauxDeChange.setForeground(new java.awt.Color(51, 51, 255));
        labTauxDeChange.setText("Taux");

        combototMonnaie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        combototMonnaie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combototMonnaieItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barreOutils, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabPrincipal)
            .addComponent(panelCriteres_categorie, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(labTauxDeChange, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labInfos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chRecherche, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 100, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(combototMonnaie, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(panelCriteres_categorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combototMonnaie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labTauxDeChange)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labInfos)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableListeFichesDePaieMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeFichesDePaieMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, 0);
    }//GEN-LAST:event_tableListeFichesDePaieMouseClicked

    private void scrollListeFichesDePaieMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeFichesDePaieMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, 0);
    }//GEN-LAST:event_scrollListeFichesDePaieMouseClicked

    private void tabPrincipalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPrincipalStateChanged
        // TODO add your handling code here:
        activerBoutons(((JTabbedPane) evt.getSource()).getSelectedIndex());
    }//GEN-LAST:event_tabPrincipalStateChanged

    private void tableListeFichesDePaieKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableListeFichesDePaieKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeFichesDePaieKeyReleased

    private void combototMonnaieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combototMonnaieItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            actualiserTotaux("combototMonnaieItemStateChanged");
        }
    }//GEN-LAST:event_combototMonnaieItemStateChanged

    private void tableListeFichesDePaieMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeFichesDePaieMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeFichesDePaieMouseDragged

    private void chClasseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chClasseItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (gestionnaireRecherche != null) {
                gestionnaireRecherche.demarrerRecherche();
            }
        }
    }//GEN-LAST:event_chClasseItemStateChanged

    private void chFraisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chFraisItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (gestionnaireRecherche != null) {
                gestionnaireRecherche.demarrerRecherche();
            }
        }
    }//GEN-LAST:event_chFraisItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barreOutils;
    private javax.swing.JComboBox<String> chClasse;
    private javax.swing.JComboBox<String> chFrais;
    private UI.JS2bTextField chRecherche;
    private javax.swing.JComboBox<String> combototMonnaie;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labInfos;
    private javax.swing.JLabel labTauxDeChange;
    private javax.swing.JLabel llabMontDu;
    private javax.swing.JLabel llabMontantDuSelected;
    private javax.swing.JLabel llabMontantPaye;
    private javax.swing.JLabel llabMontantPayeSelected;
    private javax.swing.JLabel llabMontantResteSelected;
    private javax.swing.JLabel llabMontantSolde;
    private javax.swing.JPanel panSelected;
    private javax.swing.JPanel panelCriteres_categorie;
    private javax.swing.JScrollPane scrollListeFichesDePaie;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTable tableListeFichesDePaie;
    private javax.swing.JLabel valMontDu;
    private javax.swing.JLabel valMontDuSelect;
    private javax.swing.JLabel valMontPaye;
    private javax.swing.JLabel valMontPayeSelect;
    private javax.swing.JLabel valMontReste;
    private javax.swing.JLabel valMontResteSelect;
    // End of variables declaration//GEN-END:variables
}
