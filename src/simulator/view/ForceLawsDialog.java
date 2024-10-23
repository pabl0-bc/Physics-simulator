package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPropertyName;

import netscape.javascript.JSException;
import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ForceLawsDialog extends JDialog implements SimulatorObserver {
	private DefaultComboBoxModel<String> _lawsModel;
	private DefaultComboBoxModel<String> _groupsModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _forceLawsInfo;
	private String[] _headers = { "Key", "Value", "Description" };
	private int _selectedLawsIndex;

	ForceLawsDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		_ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
	}

	private void initGUI() {
		setTitle("Force Laws Selection");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		// panel de informacion de forceLawsDialog
		JPanel _panelInfo = new JPanel();
		JLabel _info = new JLabel(
				"<html>Select a force law and provide values for the parametes in the Values column (default values are used for<br> parameters with no value)<html>");
		_panelInfo.add(_info);
		mainPanel.add(_panelInfo, BorderLayout.PAGE_START);

		// _forceLawsInfo se usará para establecer la información en la tabla
		_forceLawsInfo = _ctrl.getForceLawsInfo();

		_dataTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};
		
		_dataTableModel.setColumnIdentifiers(_headers);
		JTable _tab = new JTable(_dataTableModel);
		mainPanel.add(new JScrollPane(_tab, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

		_lawsModel = new DefaultComboBoxModel<>();
		for (int i = 0; i < _forceLawsInfo.size(); i++) {
			_lawsModel.addElement(_forceLawsInfo.get(i).getString("desc"));
		}

		JPanel _comboBoxPanel = new JPanel();

		JLabel _flLabel = new JLabel("Force law: ");
		_comboBoxPanel.add(_flLabel);

		JComboBox<String> _lawsBox = new JComboBox<String>(_lawsModel);
		_lawsBox.addActionListener((e) -> {

			int info = _lawsBox.getSelectedIndex();
			_selectedLawsIndex = info;
			JSONObject data = _forceLawsInfo.get(info).getJSONObject("data");
			_dataTableModel.setRowCount(data.length());
			_dataTableModel.fireTableStructureChanged();
			int i = 0;
			for (String key : data.keySet()) {
				_dataTableModel.setValueAt(key, i, 0);
				_dataTableModel.fireTableCellUpdated(i, 0);
				_dataTableModel.setValueAt(data.get(key), i, 2);
				i++;
			}

		});
		_comboBoxPanel.add(_lawsBox);

		_groupsModel = new DefaultComboBoxModel<>();
		JLabel _groupLabel = new JLabel("Groups: ");
		_comboBoxPanel.add(_groupLabel);

		JComboBox<String> _groupBox = new JComboBox<String>(_groupsModel);
		_comboBoxPanel.add(_groupBox);

		mainPanel.add(_comboBoxPanel);

		//crear los botones OK y Cancel y añadirlos al panel

		JPanel _buttonPanel = new JPanel();
		
		JButton _cancelButton = new JButton();
		_cancelButton.setText("Cancel");
		_buttonPanel.add(_cancelButton);
		_cancelButton.addActionListener((e) -> {
			setVisible(false);
		});
		
		JButton _okButton = new JButton();
		_okButton.addActionListener((e) -> {
			if(_groupsModel.getSelectedItem() != null) {
			JSONObject data = new JSONObject();

			JSONArray vec = new JSONArray();
			JSONObject newFl = new JSONObject();

			for (int i = 0; i < _dataTableModel.getRowCount(); i++) {
				if (_dataTableModel.getValueAt(i, 0).toString().toLowerCase().equals("g")) {
					data.put(_dataTableModel.getValueAt(i, 0).toString().toLowerCase(),
							_dataTableModel.getValueAt(i, 1));
				} else if (_dataTableModel.getValueAt(i, 0).toString().toLowerCase().equals("c")) {
					String aux = _dataTableModel.getValueAt(i, 1).toString();
					String[] parts = aux.split(",");
					for (String s : parts) {
						vec.put(s);
					}
					if(vec.length() == 2)
						data.put(_dataTableModel.getValueAt(i, 0).toString().toLowerCase(), vec);
				}
			}
			newFl.put("data", data);
			newFl.put("type", _forceLawsInfo.get(_selectedLawsIndex).getString("type"));

				try {
					_ctrl.setForcesLaws(_groupsModel.getSelectedItem().toString(), newFl);
				}
				catch(Throwable p) {
					Utils.showErrorMsg(p.getMessage());
				}
				setVisible(false);
			}

		});
		_okButton.setText("Ok");
		_buttonPanel.add(_okButton);

		mainPanel.add(_buttonPanel);

		setPreferredSize(new Dimension(700, 400));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public void open() {
		setLocation(getParent().getLocation().x - 35, getParent().getLocation().y + 100);
		pack();
		setVisible(true);
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_groupsModel.removeAllElements();
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		this._groupsModel.addAll(groups.keySet());
		_dataTableModel.fireTableStructureChanged();
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		_groupsModel.addElement(g.getId());
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
	}
}
