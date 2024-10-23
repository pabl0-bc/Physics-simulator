package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class InfoTable extends JPanel {
	String _title;
	TableModel _tableModel;
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.black, 1);
	private JTable _model;

	InfoTable(String title, TableModel tableModel) {
		_title = title;
		_tableModel = tableModel;
		initGUI();
		
	}

	private void initGUI() {
		setPreferredSize(new Dimension(500, 250));
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder(_defaultBorder, _title, TitledBorder.LEFT,
				TitledBorder.TOP));
		_model = new JTable(_tableModel);
		// TODO
		_model.getColumnModel().getColumn(0).setPreferredWidth(50);
		_model.getColumnModel().getColumn(1).setPreferredWidth(100);
		_model.getColumnModel().getColumn(2).setPreferredWidth(50);
		
		add(new JScrollPane(_model), BorderLayout.CENTER);

	}
}

//_model.getColumnModel().getColumn(0).setWidth(200);
//_model.getColumnModel().getColumn(0).setPreferredWidth(200);
//_model.getColumnModel().getColumn(0).setMaxWidth(200);