package pontoWeb.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import pontoWeb.controller.AtrasoController;
import pontoWeb.db.ConnectionFactory;

public class AtrasoView extends TabelaView {

	public JanelaPrincipal context;
	private AtrasoController atrasoController;
	private ConnectionFactory connection;

	public AtrasoView(Container container, JanelaPrincipal context,ConnectionFactory connection) throws SQLException {
		this.connection = connection;
		addThirtTable(container);
		activeButtons();
		this.context = context;
		this.atrasoController = new AtrasoController(context, this.connection);
	}

	public void addThirtTable(Container container) {
		JPanel firstPanel = new JPanel(new GridLayout(2, 1));
		JPanel secondPanel = new JPanel(new GridLayout(2, 1));
		JPanel panelTitle = addTitle("ATRASO");
		JPanel panelButtons = addButtons();
		JPanel panelTable = addTable();
		secondPanel.add(panelTitle);
		secondPanel.add(panelButtons);
		firstPanel.add(secondPanel);
		firstPanel.add(panelTable);
		container.add(firstPanel);
	}

	public JPanel addButtons() {
		JPanel panelButtons = new JPanel(new FlowLayout());
		JButton buttonSub = new JButton();
		JButton buttonClear = new JButton();
		buttonSub.setText("Subtração");
		buttonClear.setText("Limpar");
		panelButtons.add(buttonSub);
		panelButtons.add(buttonClear);
		getListButtons().add(buttonSub);
		getListButtons().add(buttonClear);
		return panelButtons;
	}

	public void activeButtons() {
		if (this.getListButtons().size() == 2) {
			this.getListButtons().get(0).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					atrasoController.subAtraso();
				}

			});
			this.getListButtons().get(1).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					clearTable();
				}
			});

		}
	}

}
