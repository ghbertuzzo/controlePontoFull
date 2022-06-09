package pontoWeb.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MarcacoesFeitasView extends TabelaView{	

	public MarcacoesFeitasView(Container container) {
		addSecondTable(container);
		activeButtons();
	}

	public boolean validMarcacoesFeitas(DefaultTableModel tableModel) {
		if (tableModel.getRowCount() > 0) {
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Preencha as Marcações Feitas");
			return false;
		}
	}
	
	public void addSecondTable(Container container) {
		JPanel firstPanel = new JPanel(new GridLayout(2, 1));
		JPanel secondPanel = new JPanel(new GridLayout(4, 1));
		JPanel panelNull = new JPanel();
		JPanel panelTitle = addTitle("MARCAÇÕES FEITAS");
		JPanel panelEditTexts = addEditTexts();
		JPanel panelButtons = addButtons();
		JPanel panelTable = addTable();
		secondPanel.add(panelNull);
		secondPanel.add(panelTitle);
		secondPanel.add(panelEditTexts);
		secondPanel.add(panelButtons);
		firstPanel.add(secondPanel);
		firstPanel.add(panelTable);
		container.add(firstPanel);
	}
	
	public JPanel addEditTexts() {
		String[] labels = { "Entrada", "Saída" };
		JPanel firstPanel = new JPanel();
		JPanel secondPanel = new JPanel();
		firstPanel.add(secondPanel);		
		JLabel labelEntrie = new JLabel(labels[0], JLabel.TRAILING);
		JTextField textField = new JTextField(2);
		JTextField textField2 = new JTextField(2);
		JLabel label2 = new JLabel(":");
		JLabel labelExit = new JLabel(labels[1], JLabel.TRAILING);
		JTextField textField3 = new JTextField(2);
		JTextField textField4 = new JTextField(2);
		JLabel label4 = new JLabel(":");
		secondPanel.add(labelEntrie);
		secondPanel.add(textField);
		secondPanel.add(label2);
		secondPanel.add(textField2);		
		secondPanel.add(labelExit);
		secondPanel.add(textField3);
		secondPanel.add(label4);
		secondPanel.add(textField4);		
		getListTextFields().add(textField);
		getListTextFields().add(textField2);
		getListTextFields().add(textField3);
		getListTextFields().add(textField4);		
		return firstPanel;
	}
	
	public JPanel addButtons() {
		JPanel panelButtons = new JPanel(new FlowLayout());
		JButton buttonAdd = new JButton();
		buttonAdd.setText("Adicionar");
		JButton buttonRemove = new JButton();
		buttonRemove.setText("Remover");
		JButton buttonClear = new JButton();
		buttonClear.setText("Limpar");
		panelButtons.add(buttonAdd);
		panelButtons.add(buttonRemove);
		panelButtons.add(buttonClear);
		getListButtons().add(buttonAdd);
		getListButtons().add(buttonRemove);
		getListButtons().add(buttonClear);
		return panelButtons;
	}
	
	public void activeButtons() {
		if (this.getListButtons().size() == 3) {
			this.getListButtons().get(0).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					addEntries();
				}
			});
			this.getListButtons().get(1).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					removeList();
				}
			});
			this.getListButtons().get(2).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					clearTable();
				}
			});
		}
	}
	
	public void addEntries() {
		if(validateFields()) {
			addList();			
		}
	}

	public Boolean validFieldsHoursAndMinutes(int i, JTextField field) {
		if(i % 2 == 0) {
			if ( Integer.parseInt(field.getText()) > 23) {
				JOptionPane.showMessageDialog(null, "Entrada inválida: informe a hora corretamente!");
				field.requestFocus();
				return false;
			}
		}else {
			if ( Integer.parseInt(field.getText()) > 59) {
				JOptionPane.showMessageDialog(null, "Entrada inválida: informe os minutos corretamente!");
				field.requestFocus();
				return false;
			}
		}
		return true;
	}
	public Boolean validateFields() {
		for(int i = 0; i< this.getListTextFields().size();i++) {			
			if (this.getListTextFields().get(i).getText().length() != 2) {
				JOptionPane.showMessageDialog(null, "Entrada inválida: informe 2 digitos!");
				this.getListTextFields().get(i).requestFocus();
				return false;
			}
			if (!this.getListTextFields().get(i).getText().matches("[+-]?\\d*(\\.\\d+)?")) {
				JOptionPane.showMessageDialog(null, "Entrada inválida: Informe 2 digitos numéricos!");
				this.getListTextFields().get(i).requestFocus();
				return false;
			}
			if(!validFieldsHoursAndMinutes(i, this.getListTextFields().get(i))) {
				return false;
			}
		}
		return true;
	}

	public void removeList() {
		int index = this.getTable().getSelectedRow();
		if (index == -1) {
			JOptionPane.showMessageDialog(null, "Selecione a linha que deseja excluir");
		} else {
			this.getListEntries().remove(index);
			this.getListExits().remove(index);
			renderTable();
		}
	}

	public void addList() {
		this.getListEntries().add(LocalTime.of(Integer.parseInt(this.getListTextFields().get(0).getText().trim()),
				Integer.parseInt(this.getListTextFields().get(1).getText().trim())));
		this.getListExits().add(LocalTime.of(Integer.parseInt(this.getListTextFields().get(2).getText().trim()),
				Integer.parseInt(this.getListTextFields().get(3).getText().trim())));
		renderTable();
		clearInputs();
		this.getListTextFields().get(0).requestFocus();
	}

	public void clearInputs() {
		for (JTextField entrada : this.getListTextFields()) {
			entrada.setText("");
		}
	}
	
}
