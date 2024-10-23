package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ControlPanel extends JPanel implements SimulatorObserver {
	private Controller _ctrl;
	private JToolBar _toolaBar;
	private JFileChooser _fc;
	private boolean _stopped = true; // utilizado en los botones de run/stop
	private JButton _quitButton;
	private JButton _fcButton;
	private JButton _flDialogButton;
	private JButton _vwButton;
	private JButton _runButton;
	private JButton _stopButton;
	private File file;
	private InputStream i;
	private ForceLawsDialog flDialog;
	private JTextField _deltaTime;
	private JSpinner stepsSpinner;
	private double dt;

	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		_toolaBar = new JToolBar();
		add(_toolaBar, BorderLayout.PAGE_START);
		_fc = new JFileChooser("select JSON file");
		_fc.setCurrentDirectory(new File("resources\\examples\\input"));
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("JSON", "json");
		_fc.setFileFilter(filtro);

		_fcButton = new JButton();
		_fcButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
		_fcButton.setToolTipText("Load an input file into the simulator");
		_fcButton.setIcon(new ImageIcon("resources/icons/open.png"));
		_fcButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int respuesta = _fc.showOpenDialog(_fc);
				if (respuesta == JFileChooser.APPROVE_OPTION) {
					file = _fc.getSelectedFile();
					_ctrl.reset();
					try {
						i = new FileInputStream(file);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					_ctrl.loadData(i);
				}
			}
		});
		_toolaBar.add(_fcButton);

		_toolaBar.addSeparator();

		// crear el selector de ficheros
		_flDialogButton = new JButton();
		_flDialogButton.setToolTipText("Select force laws for groups");
		_flDialogButton.setIcon(new ImageIcon("resources/icons/physics.png"));
		_flDialogButton.addActionListener((e) -> {
			if (flDialog == null)
				flDialog = new ForceLawsDialog(Utils.getWindow(_flDialogButton), _ctrl);
			flDialog.open();
		});
		_toolaBar.add(_flDialogButton);

		// viewer window button
		_vwButton = new JButton();
		_vwButton.setToolTipText("Open viewer window");
		_vwButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_vwButton.addActionListener((e) -> {
			ViewerWindow viewerWindow = new ViewerWindow((JFrame) Utils.getWindow(_vwButton), _ctrl);
		});
		_toolaBar.add(_vwButton);

		_toolaBar.addSeparator();

		// run button
		_runButton = new JButton();
		_runButton.setToolTipText("Open viewer window");
		_runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		dt = -1;
		_runButton.addActionListener((e) -> {
			_stopped = false;
			try {
				dt = Double.parseDouble(_deltaTime.getText());
				switchButton(_stopped);
				_ctrl.setDeltaTime(dt);
				run_sim((Integer) stepsSpinner.getValue());
			} catch (Throwable p) {
				Utils.showErrorMsg(p.getMessage());
			}
		});
		_toolaBar.add(_runButton);

		// stop button
		_stopButton = new JButton();
		_stopButton.setToolTipText("Stop the simulator");
		_stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		_stopButton.addActionListener((e) -> {
			_stopped = true;
			switchButton(_stopped);
		});
		_toolaBar.add(_stopButton);

		stepsSpinner = new JSpinner(new SpinnerNumberModel(10000, 0, null, 100));
		stepsSpinner.setPreferredSize(new Dimension(80, 40));
		stepsSpinner.setMaximumSize(new Dimension(80, 40));
		stepsSpinner.setToolTipText("Simulation steps to run: 1-10000");
		_toolaBar.addSeparator();
		_toolaBar.add(new JLabel("Steps: "));
		_toolaBar.add(stepsSpinner);

		_deltaTime = new JTextField("2500.0");
		_deltaTime.setPreferredSize(new Dimension(80, 40));
		_deltaTime.setMaximumSize(new Dimension(80, 40));
		_deltaTime.addActionListener((e) -> {
			try {
				double aux = Double.parseDouble(_deltaTime.getText());
			} catch (IllegalArgumentException p) {
				Utils.showErrorMsg(p.getMessage());
				_deltaTime.setText(null);
			}
		});
		_deltaTime.setToolTipText("Real time (seconds) corresponding to a step");
		_toolaBar.addSeparator();
		_toolaBar.add(new JLabel("Delta-time: "));
		_toolaBar.add(_deltaTime);

		// Quit Button
		_toolaBar.add(Box.createGlue()); // this aligns the button to the right
		_toolaBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> Utils.quit(this));
		_toolaBar.add(_quitButton);

	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
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

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				Utils.showErrorMsg(TOOL_TIP_TEXT_KEY);
				_stopped = true;
				switchButton(_stopped);
				return;
			}
			SwingUtilities.invokeLater(() -> run_sim(n - 1));
		} else {
			_stopped = true;
		}
	}

	public void switchButton(boolean state) {
		_fcButton.setEnabled(state);
		_flDialogButton.setEnabled(state);
		_vwButton.setEnabled(state);
		_quitButton.setEnabled(state);
		_deltaTime.setEnabled(state);
		stepsSpinner.setEnabled(state);
	}
}
