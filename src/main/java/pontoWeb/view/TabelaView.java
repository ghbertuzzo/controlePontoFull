package pontoWeb.view;

import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import pontoWeb.model.Periodo;

public class TabelaView {

	private List<JTextField> listTextFields;
	private List<JButton> listButtons;
	private DefaultTableModel tableModel;
	private JTable table;
	private List<LocalTime> listEntries;
	private List<LocalTime> listExits;

	public TabelaView() {
		this.listTextFields = new ArrayList<JTextField>();
		this.listButtons = new ArrayList<JButton>();
		this.listEntries = new ArrayList<LocalTime>();
		this.listExits = new ArrayList<LocalTime>();
	}	
	
	public List<JTextField> getListTextFields() {
		return listTextFields;
	}

	public void setListTextFields(List<JTextField> listTextFields) {
		this.listTextFields = listTextFields;
	}

	public List<JButton> getListButtons() {
		return listButtons;
	}

	public void setListButtons(List<JButton> listButtons) {
		this.listButtons = listButtons;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public List<LocalTime> getListEntries() {
		return listEntries;
	}

	public void setListEntries(List<LocalTime> listEntries) {
		this.listEntries = listEntries;
	}

	public List<LocalTime> getListExits() {
		return listExits;
	}

	public void setListExits(List<LocalTime> listExits) {
		this.listExits = listExits;
	}

	public JPanel addTitle(String text) {
		JLabel labelhorario = new JLabel(text);
		labelhorario.setFont(new Font("SansSerif", Font.BOLD, 16));
		JPanel panel = new JPanel();
		panel.add(labelhorario);
		return panel;
	}
	
	public JPanel addTable() {
		String[] columnNames = { "Entrada", "Saída" };
		Object[][] data = {};
		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
		this.setTableModel(tableModel);
		JTable table = new JTable(tableModel);
		this.setTable(table);
		JScrollPane scrollPane = new JScrollPane(table);
		JPanel panel = new JPanel(new GridLayout());
		panel.add(scrollPane);
		return panel;
	}	

	public void clearTable() {
		this.getTableModel().setRowCount(0);
		this.getListEntries().clear();
		this.getListExits().clear();
	}

	public void renderTable() {
		this.getTableModel().setRowCount(0);
		for (int i = 0; i < this.getListEntries().size(); i++) {
			this.getTableModel().addRow(new Object[] {localtimeToString(this.getListEntries().get(i)), localtimeToString(this.getListExits().get(i))});
		}
	}

	public String localtimeToString(LocalTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return formatter.format(time);
	}
	
	public TabelaView newTable (List<Periodo> listPeriodos) {
		TabelaView tabelaView = new TabelaView();
		for (Periodo periodo: listPeriodos) {
			tabelaView.getListEntries().add(periodo.getEntrada());
			tabelaView.getListExits().add(periodo.getSaida());
		}
		return tabelaView;		
	}	
	
}
