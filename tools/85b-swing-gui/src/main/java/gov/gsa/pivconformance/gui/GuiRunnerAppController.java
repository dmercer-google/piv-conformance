package gov.gsa.pivconformance.gui;

import java.awt.Font;
import java.awt.Window;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import gov.gsa.pivconformance.conformancelib.configuration.ConfigurationException;
import gov.gsa.pivconformance.conformancelib.configuration.ConformanceTestDatabase;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GuiRunnerAppController {
	private static final Logger s_logger = LoggerFactory.getLogger(GuiRunnerAppController.class);
	
	// more than one controller will cause problems
	private static final GuiRunnerAppController INSTANCE = new GuiRunnerAppController();
	
	private ConformanceTestDatabase m_testDatabase;
	private GuiRunnerApplication m_app;
	private OpenDatabaseAction m_openDatabaseAction;
	private ShowDebugWindowAction m_showDebugWindowAction;
	private GuiRunAllTestsAction m_runAllTestsAction;
	private GuiToggleTestTreeAction m_toggleTreeAction;
	private GuiDisplayAboutDialogAction m_displayAboutDialogAction;
	private GuiDisplayTestReportAction m_displayTestReportAction;
	private OpenDefaultPIVDatabaseAction m_openDefaultPIVDatabaseAction;
	private OpenDefaultPIVIDatabaseAction m_openDefaultPIVIDatabaseAction;
	private GuiTestExecutionController m_tec;
	private String m_cctVersion;

	private JTextField pivAuthOverrideTextField;
	private JTextField digitalSignatureOverrideTextField;
	private JTextField keyManagementOverrideTextField;
	private JTextField cardAuthenticationOverrideTextField;
	private JTextField contentSigningOverrideTextField;
	public void reset() {
		m_testDatabase = null;
		m_app = null;
		m_openDatabaseAction = null;
		m_showDebugWindowAction = null;
		m_runAllTestsAction = null;
		m_toggleTreeAction = null;
		m_displayAboutDialogAction = null;
		m_displayTestReportAction = null;
		m_openDefaultPIVDatabaseAction = null;
		m_openDefaultPIVIDatabaseAction = null;
		m_tec = null;
		m_cctVersion = null;
		
		createActions();
	}

	private GuiRunnerAppController() {
		reset();
	}
	
	public static GuiRunnerAppController getInstance() {
		return INSTANCE;
	}

	public ConformanceTestDatabase getTestDatabase() {
		return m_testDatabase;
	}

	public void setTestDatabase(ConformanceTestDatabase testDatabase) {
		m_testDatabase = testDatabase;
		
		if(m_app != null && testDatabase != null)
			reloadTree();
	}

	public GuiRunnerApplication getApp() {
		return m_app;
	}

	public void setApp(GuiRunnerApplication app) {
		m_app = app;
	}

	void setTestRunLogController (GuiTestExecutionController trlc) {
		m_tec = trlc;
	}

	public GuiTestExecutionController getTestExecutionController() {
		return m_tec;
	}
	
	void setCctVersion (String cctVersion) {
		m_cctVersion = cctVersion;
	}

	public String getCctVersion() {
		return m_cctVersion;
	}
	
	public JFrame getMainFrame() {
		return m_app.getMainFrame();
	}
	
	public OpenDatabaseAction getOpenDatabaseAction() {
		return m_openDatabaseAction;
	}

	public OpenDefaultPIVDatabaseAction getOpenDefaultPIVDatabaseAction() {
		return m_openDefaultPIVDatabaseAction;
	}

	public OpenDefaultPIVIDatabaseAction getOpenDefaultPIVIDatabaseAction() {
		return m_openDefaultPIVIDatabaseAction;
	}
	
	public ShowDebugWindowAction getShowDebugWindowAction() {
		return m_showDebugWindowAction;
	}

	public GuiRunAllTestsAction getRunAllTestsAction() {
		return m_runAllTestsAction;
	}

	public GuiDisplayAboutDialogAction getDisplayAboutDialogAction() {
		return m_displayAboutDialogAction;
	}

	public GuiToggleTestTreeAction getToggleTestTreeAction() {
		return m_toggleTreeAction;
	}

	public GuiDisplayTestReportAction getDisplayTestReportAction() {
		return m_displayTestReportAction;
	}
	
	// this used to toggle the window, but now that we're off RCP and in a separate JFrame, the [x] can be used to hide and this just shows it
	public void showDebugWindow() {
		DebugWindow window = m_app.getDebugFrame();
		if(!window.isVisible()) {
			window.setVisible(true);
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void showOidDialog(Window win) {
		//s_logger.error("Stubbed out showOidDialog() is still here");
		String toolTip = "Enter pipe-separated list of possible certificate policy OIDs (logically ORd)";
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(UIManager.getColor("Button.background"));
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(619,287);
        frame.setTitle("Certificate Policy OID Overrides");
        frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
        		FormSpecs.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("max(75dlu;min)"),
        		ColumnSpec.decode("8dlu"),
        		ColumnSpec.decode("max(150dlu;min)"),
        		ColumnSpec.decode("max(150dlu;min)"),
        		FormSpecs.RELATED_GAP_COLSPEC,},
        	new RowSpec[] {
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		FormSpecs.DEFAULT_ROWSPEC,
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("max(20px;default)"),
        		FormSpecs.RELATED_GAP_ROWSPEC,}));
        
        JTextArea txtDefinitionTextArea = new JTextArea();
        txtDefinitionTextArea.setWrapStyleWord(true);
        txtDefinitionTextArea.setBackground(UIManager.getColor("Button.background"));
        txtDefinitionTextArea.setEnabled(false);
        txtDefinitionTextArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txtDefinitionTextArea.setLineWrap(true);
        txtDefinitionTextArea.setText("This form allows you to override the certificate policy OIDs that the tool checks for each type of certificate.  If your certificates assert different policy OIDs, complete this form to ensure the tool tests for your OIDs.");
        frame.getContentPane().add(txtDefinitionTextArea, "2, 2, 4, 1, fill, top");
        
        JLabel lblPivAuthentication = new JLabel("PIV Authentication");
        frame.getContentPane().add(lblPivAuthentication, "2, 4, right, default");
        
        pivAuthOverrideTextField = new JTextField();
        pivAuthOverrideTextField.setToolTipText(toolTip);
        frame.getContentPane().add(pivAuthOverrideTextField, "4, 4, 2, 1");
        pivAuthOverrideTextField.setColumns(10);
        
        JLabel lblDigitalSignature = new JLabel("Digital Signature");
        frame.getContentPane().add(lblDigitalSignature, "2, 6, right, default");
        
        digitalSignatureOverrideTextField = new JTextField();
        digitalSignatureOverrideTextField.setToolTipText(toolTip);
        frame.getContentPane().add(digitalSignatureOverrideTextField, "4, 6, 2, 1");
        digitalSignatureOverrideTextField.setColumns(10);
        
        JLabel lblKeyManagement = new JLabel("Key Management");
        frame.getContentPane().add(lblKeyManagement, "2, 8, right, default");
        
        keyManagementOverrideTextField = new JTextField();
        keyManagementOverrideTextField.setToolTipText(toolTip);
        frame.getContentPane().add(keyManagementOverrideTextField, "4, 8, 2, 1");
        keyManagementOverrideTextField.setColumns(10);
        
        JLabel lblCardAuthentication = new JLabel("Card Authentication");
        frame.getContentPane().add(lblCardAuthentication, "2, 10, right, default");
        
        cardAuthenticationOverrideTextField = new JTextField();
        cardAuthenticationOverrideTextField.setToolTipText(toolTip);
        frame.getContentPane().add(cardAuthenticationOverrideTextField, "4, 10, 2, 1");
        cardAuthenticationOverrideTextField.setColumns(10);
        
        JLabel lblContentSigning = new JLabel("Content Signing");
        frame.getContentPane().add(lblContentSigning, "2, 12, right, default");
        
        contentSigningOverrideTextField = new JTextField();
        contentSigningOverrideTextField.setToolTipText(toolTip);
        frame.getContentPane().add(contentSigningOverrideTextField, "4, 12, 2, 1");
        contentSigningOverrideTextField.setColumns(10);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        
        JButton saveButton = new JButton("Save");
        frame.getContentPane().add(saveButton, "4, 16, right, default");	
        
         saveButton.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed(ActionEvent e) {
				s_logger.debug("Save override OIDS action performed");

				// TODO: Save changes to database
				ConformanceTestDatabase db = new ConformanceTestDatabase(null);
				String path = GuiRunnerAppController.getInstance().getApp().getMainContent().getTestExecutionPanel().getDatabaseNameField().getText();
				try {
					db.openDatabaseInFile(path);
					s_logger.debug("Opened database file {}", path);
				} catch (ConfigurationException e1) {
					s_logger.debug("Database error: {}", e1.getMessage());
				}
				win.dispose();
			}
		});
        frame.getContentPane().add(cancelButton, "5, 16, left, default");
        
        cancelButton.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed(ActionEvent e) {
				s_logger.debug("Cancel override OIDS action performed");
				win.dispose();
			}
		});        
        frame.setVisible(true);	
	}

	public void showAboutDialog() {
		s_logger.error("Stubbed out showAboutDialog() is still here");
	}
	
	public void reloadTree() {
		GuiTestTreePanel tree = m_app.getTreePanel();
		tree.refresh();
	}
	/*
	public String logRollover(TimeStampedFileAppender<?> appender, boolean nextHeader) {
		String rv = null;
		
		if(m_conformanceTestCsvAppender == null) {
			s_logger.warn("logRollover was called without any appender configured.");
		}
		
		rv = appender.createTimeStampLog(); // Timestamped name of copy of log returned from getFile() 
		
		if(nextHeader == true) {
			File f = new File(m_conformanceTestCsvAppender.getFile());
			PrintStream p;
			try {
				p = new PrintStream(f);
				p.println("Date,Test Id,Description,Expected Result,Actual Result");
				p.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m_conformanceTestCsvAppender.setImmediateFlush(true);
		return rv;
	}
	*/
	protected void createActions() {
		ImageIcon openIcon = getActionIcon("folder", "Open");
		m_openDatabaseAction = new OpenDatabaseAction("Open Database", openIcon, "Open a conformance test database");
	    ImageIcon runIcon = getActionIcon("building_go", "Run");
	    m_runAllTestsAction = new GuiRunAllTestsAction("Run all tests", runIcon, "Run all available tests in database");
	    ImageIcon debugIcon = getActionIcon("application_xp_terminal", "Debug");
	    m_showDebugWindowAction = new ShowDebugWindowAction("Show Debugging tools", debugIcon, "Show detailed log and debugging tools");
	    //ImageIcon oidIcon = getActionIcon("application_view_list", "Override OIDs");
	    //m_showOidDialogAction = new ShowOidDialogAction("Override test OIDs...", oidIcon, "Use alternative certificate policy OIDs");
	    ImageIcon toggleIcon = getActionIcon("application_side_tree", "Toggle Tree");
	    m_toggleTreeAction = new GuiToggleTestTreeAction("Toggle test tree view", toggleIcon, "Show or hide the test tree");
	    ImageIcon displayReportIcon = getActionIcon("html", "Display HTML report");
	    m_displayTestReportAction = new GuiDisplayTestReportAction("Display Test Report", displayReportIcon, "Display test report for current log");
	    ImageIcon savingIcon = getActionIcon("folder", "Saving");

	    ImageIcon pivIcon = getActionIcon("PIV", "Open");
	    m_openDefaultPIVDatabaseAction = new OpenDefaultPIVDatabaseAction("Open Default PIV Database", pivIcon, "Open Default PIV conformance test database");
	
	    ImageIcon pivIIcon = getActionIcon("PIVI", "Open");
	    m_openDefaultPIVIDatabaseAction = new OpenDefaultPIVIDatabaseAction("Open Default PIV-I Database", pivIIcon, "Open Default PIV-I conformance test database");
	}
	
	protected ImageIcon getActionIcon(String imageName, String altText) {
		String imgLocation = "/icons/" + imageName + ".png";
		URL imageUrl = GuiRunnerAppController.class.getResource(imgLocation);
		if(imageUrl == null) {
			s_logger.error("Unable to get image at classpath location {}", imgLocation);
			return null;
		}
		return new ImageIcon(imageUrl, altText);
	}
}
