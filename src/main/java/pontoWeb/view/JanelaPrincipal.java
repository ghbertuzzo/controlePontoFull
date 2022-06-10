package pontoWeb.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pontoWeb.controller.AtrasoController;
import pontoWeb.controller.HistoricoController;
import pontoWeb.controller.HoraExtraController;
import pontoWeb.controller.HorarioDeTrabalhoController;
import pontoWeb.controller.MarcacoesFeitasController;
import pontoWeb.db.ConnectionFactory;
import pontoWeb.model.Historico;
import net.sf.jasperreports.engine.JRException;

public class JanelaPrincipal extends JFrame {

	public ConnectionFactory connectionFactory;
	public Container containerPrincipal;
	public HorarioDeTrabalhoView horarioDeTrabalhoView;
	public MarcacoesFeitasView marcacoesFeitasView;
	public AtrasoView atrasoView;
	public HoraExtraView horaExtraView;

	private List<JButton> listButtons;
	public List<JTextField> listTextFieldsData;

	public HorarioDeTrabalhoController htController;
	public MarcacoesFeitasController mfController;
	public AtrasoController atController;
	public HoraExtraController heController;
	public HistoricoController historicoController;

	private static final long serialVersionUID = -8113310048659696964L;

	public JanelaPrincipal(ConnectionFactory connectionFactory) throws SQLException {
		super();
		this.connectionFactory = connectionFactory;
		this.listButtons = new ArrayList<JButton>();
		this.listTextFieldsData = new ArrayList<JTextField>();
		createLayout();

		this.htController = new HorarioDeTrabalhoController(this.connectionFactory);
		this.mfController = new MarcacoesFeitasController(this.connectionFactory);
		this.atController = new AtrasoController(this,this.connectionFactory);
		this.heController = new HoraExtraController(this,this.connectionFactory);
		this.historicoController = new HistoricoController(this.connectionFactory);
		activeButtons();
	}

	private void createLayout() throws SQLException {
		basicConfigFrame();
		addLayout();
		addDataFields();
		JPanel gridLayout = addGridLayout();
		this.horarioDeTrabalhoView = new HorarioDeTrabalhoView(gridLayout);
		this.marcacoesFeitasView = new MarcacoesFeitasView(gridLayout);
		this.atrasoView = new AtrasoView(gridLayout, this,this.connectionFactory);
		this.horaExtraView = new HoraExtraView(gridLayout, this,this.connectionFactory);
		addExportButton();
	}

	private void addExportButton() {
		// TODO Auto-generated method stub
		JButton btnExportar = new JButton();
		btnExportar.setText("Exportar Histórico");
		this.listButtons.add(btnExportar);
		JPanel firstPanel = new JPanel();
		firstPanel.add(btnExportar);
		this.add(firstPanel, BorderLayout.PAGE_END);
	}

	private JPanel addGridLayout() {
		JPanel gridLayout = new JPanel();
		gridLayout.setLayout(new GridLayout(2, 2));
		this.containerPrincipal.add(gridLayout, BorderLayout.CENTER);
		return gridLayout;
	}

	private void activeButtons() {
		if (this.listButtons.size() == 3) {
			this.listButtons.get(0).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						saveDB();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			this.listButtons.get(1).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						loadDB();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			this.listButtons.get(2).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						exportReport();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (JRException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}

	}

	public Boolean validDataFields() {
		for (int i = 0; i < this.listTextFieldsData.size(); i++) {
			if (this.listTextFieldsData.get(i).getText().length() != 2) {
				JOptionPane.showMessageDialog(null, "Entrada inválida: informe 2 digitos!");
				this.listTextFieldsData.get(i).requestFocus();
				return false;
			}
			if (!this.listTextFieldsData.get(i).getText().matches("[+-]?\\d*(\\.\\d+)?")) {
				JOptionPane.showMessageDialog(null, "Entrada inválida: Informe 2 digitos numéricos!");
				this.listTextFieldsData.get(i).requestFocus();
				return false;
			}
			if (i == 0) {
				if (Integer.parseInt(this.listTextFieldsData.get(i).getText()) > 31) {
					JOptionPane.showMessageDialog(null, "Entrada inválida: informe um dia válido!");
					this.listTextFieldsData.get(i).requestFocus();
					return false;
				}
			}
			if (i == 1) {
				if (Integer.parseInt(this.listTextFieldsData.get(i).getText()) > 12) {
					JOptionPane.showMessageDialog(null, "Entrada inválida: informe um mês válido!");
					this.listTextFieldsData.get(i).requestFocus();
					return false;
				}
			}
			if (i == 2) {
				if (Integer.parseInt(this.listTextFieldsData.get(i).getText()) > 22) {
					JOptionPane.showMessageDialog(null, "Entrada inválida: informe um ano válido!");
					this.listTextFieldsData.get(i).requestFocus();
					return false;
				}
			}
		}
		return true;
	}

	public void exportReport() throws SQLException, FileNotFoundException, JRException, ClassNotFoundException {
		List<Historico> historicos = this.historicoController.getHistoricos();
		if (historicos != null) {
			this.historicoController.exportReportHistorico(historicos);
		}

	}

	public void saveDB() throws SQLException {
		if (validDataFields()) {
			if (validTables()) {
				if (this.historicoController.hasARegister(this.listTextFieldsData)) {
					Object[] options = { "Sim", "Não" };
					int opcao = JOptionPane.showOptionDialog(null,
							"Este dia já possui um registro. \nDeseja sobrescrever as informações?",
							"Registro Existente", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
							options, options[0]);
					if (opcao == 0) {
						// SOBRESCREVER
						if (this.historicoController.update(this)) {
							JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso", "Novo Registro",
									JOptionPane.INFORMATION_MESSAGE);
							clearTables();
						} else {
							JOptionPane.showMessageDialog(null, "Erro ao sobrescrever registro", "Novo Registro",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						// ABORTAR
					}
				} else { // NÃO POSSUI REGISTRO, REALIZAR INSERT
					if (this.historicoController.insert(this)) {
						JOptionPane.showMessageDialog(null, "Registro salvo com sucesso", "Novo Registro",
								JOptionPane.INFORMATION_MESSAGE);
						clearTables();
					} else {
						JOptionPane.showMessageDialog(null, "Erro ao inserir registro", "Novo Registro",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		}
	}

	private void clearTables() {
		this.horarioDeTrabalhoView.clearTable();
		this.marcacoesFeitasView.clearTable();
		this.horaExtraView.clearTable();
		this.atrasoView.clearTable();
	}

	private Boolean validTables() {
		// VALIDAR HT
		if (this.horarioDeTrabalhoView.validHorarioTrabalho(this.horarioDeTrabalhoView.getTableModel())) {
			// VALIDAR MF
			if (this.marcacoesFeitasView.validMarcacoesFeitas(this.marcacoesFeitasView.getTableModel())) {
				// RECALCULAR HE E ATRASOS PARA EVITAR ERROS
				this.atController.subAtraso();
				this.heController.subHoraExtra();
				JOptionPane.showMessageDialog(null, "Hora Extra e Atrasos recalculados antes de salvar!",
						"Cálculos realizados", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}
		}
		return false;
	}

	public void loadDB() throws SQLException {
		if (validDataFields()) {
			Historico historico = this.historicoController.returnHistoricoHasARegister(this.listTextFieldsData);
			if (historico != null) {
				this.historicoController.load(historico, this);
				JOptionPane.showMessageDialog(null, "Informações do dia "
						+ this.historicoController.getDateFormattedView(this.listTextFieldsData) + " carregadas.",
						"Registro encontrado", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
						"Nenhum registro encontrado na data: "
								+ this.historicoController.getDateFormattedView(this.listTextFieldsData),
						"Nenhum registro encontrado", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void addDataFields() {
		JPanel firstPanel = new JPanel();
		JPanel secondPanel = new JPanel();
		firstPanel.add(secondPanel);
		JLabel labelDate = new JLabel("Data");
		JLabel label = new JLabel("/");
		JLabel label2 = new JLabel("/");
		JTextField textFieldData1 = new JTextField(2);
		JTextField textFieldData2 = new JTextField(2);
		JTextField textFieldData3 = new JTextField(2);
		JButton btnSalvar = new JButton();
		btnSalvar.setText("Salvar");
		JButton btnCarregar = new JButton();
		btnCarregar.setText("Carregar");

		this.listButtons.add(btnSalvar);
		this.listButtons.add(btnCarregar);
		secondPanel.add(labelDate);
		secondPanel.add(textFieldData1);
		secondPanel.add(label);
		secondPanel.add(textFieldData2);
		secondPanel.add(label2);
		secondPanel.add(textFieldData3);
		secondPanel.add(btnSalvar);
		secondPanel.add(btnCarregar);
		this.listTextFieldsData.add(textFieldData1);
		this.listTextFieldsData.add(textFieldData2);
		this.listTextFieldsData.add(textFieldData3);
		this.add(firstPanel, BorderLayout.PAGE_START);
	}

	private void addLayout() {
		this.setLayout(new BorderLayout());
		this.containerPrincipal = getContentPane();
	}

	private void basicConfigFrame() {
		this.setTitle("Controle de Ponto");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 650);
		this.setVisible(true);
	}
}
