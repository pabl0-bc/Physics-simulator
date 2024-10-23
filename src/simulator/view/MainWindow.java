package simulator.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	private Controller _ctrl;

	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		ControlPanel _ctrlPanel = new ControlPanel(_ctrl);
		mainPanel.add(_ctrlPanel, BorderLayout.PAGE_START);

// Definición del panel de tablas (usa un BoxLayout vertical)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);

		StatusBar _statusBar = new StatusBar(_ctrl);
		mainPanel.add(_statusBar, BorderLayout.PAGE_END);

		contentPanel.add(new InfoTable("Groups", new GroupsTableModel(_ctrl)));

		contentPanel.add(new InfoTable("Bodies", new BodiesTableModel(_ctrl)));

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Utils.quit(MainWindow.this);
			}
		});

		setLocationRelativeTo(null);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
