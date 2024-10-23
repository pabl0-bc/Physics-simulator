package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;

import simulator.misc.Vector2D;
import simulator.model.BodiesGroup;
import simulator.model.Body;

@SuppressWarnings("serial")
class Viewer extends SimulationViewer {

	private static final int _WIDTH = 500;
	private static final int _HEIGHT = 500;

	// (_centerX,_centerY) is used as the origin when drawing
	// the bodies
	private int _centerX;
	private int _centerY;

	// values used to shift the actual origin (the middle of
	// the window), when calculating (_centerX,_centerY)
	private int _originX = 0;
	private int _originY = 0;

	// the scale factor, used to reduce the bodies coordinates
	// to the size of the component
	private double _scale = 1.0;

	// indicates if the help message should be shown
	private boolean _showHelp = true;

	// indicates if the position/velocity vectors should be shown
	private boolean _showVectors = true;

	// the list bodies and groups
	private List<Body> _bodies;
	private List<BodiesGroup> _groups;

	// a color generator, and a map that assigns colors to groups
	private ColorsGenerator _colorGen;
	private Map<String, Color> _gColor;

	// the index and Id of the selected group, -1 and null means all groups
	private int _selectedGroupIdx = -1;
	private String _selectedGroup = null;

	Viewer() {
		initGUI();
	}

	private void initGUI() {

		// add a border
		setBorder(BorderFactory.createLineBorder(Color.black, 2));

		// initialize the color generator, and the map, that we use
		// assign colors to groups
		_colorGen = new ColorsGenerator();
		_gColor = new HashMap<>();

		// initialize the lists of bodies and groups
		_bodies = new ArrayList<>();
		_groups = new ArrayList<>();

		// The preferred and minimum size of the components
		setMinimumSize(new Dimension(_WIDTH, _HEIGHT));
		setPreferredSize(new Dimension(_WIDTH, _HEIGHT));

		// add a key listener to handle the user actions
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-':
					_scale = _scale * 1.1;
					repaint();
					break;
				case '+':
					_scale = Math.max(1000.0, _scale / 1.1);
					repaint();
					break;
				case '=':
					autoScale();
					repaint();
					break;
				case 'j':
					_originX += 10;
					repaint();
					break;
				case 'l':
					_originX -= 10;
					repaint();
					break;
				case 'i':
					_originY += 10;
					repaint();
					break;
				case 'm':
					_originY -= 10;
					repaint();
					break;
				case 'k':
					_originX = 0;
					_originY = 0;
					repaint();
					break;
				case 'h':
					_showHelp = !_showHelp;
					repaint();
					break;
				case 'v':
					_showVectors = !_showVectors;
					repaint();
					break;
				case 'g':
					_selectedGroupIdx++;
					if (_selectedGroupIdx < _groups.size()) {
						_selectedGroup = _groups.get(_selectedGroupIdx).getId();
					} else {
						_selectedGroupIdx = -1;
						_selectedGroup = null;
					}
					repaint();
					break;
				default:
				}
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// a better graphics object
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// calculate the center
		_centerX = getWidth() / 2 - _originX;
		_centerY = getHeight() / 2 - _originY;

		// draw red cross at (_centerX,_centerY)
		gr.setColor(Color.red);
		int crossSize = 10;
		int startX = _centerX - crossSize / 2;
		int startY = _centerY - crossSize / 2;
		gr.drawLine(startX, _centerY, startX + crossSize, _centerY);
		gr.drawLine(_centerX, startY, _centerX, startY + crossSize);

		// draw bodies
		drawBodies(gr);

		// show help if needed
		if (_showHelp) {
			showHelp(gr);
		}
	}

	private void showHelp(Graphics2D g) {
		super.paintComponents(g);

		g.setFont(new Font("Arial", Font.ROMAN_BASELINE, 13));
		g.setColor(Color.red);

		g.drawString("h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit", 10, 15);
		g.drawString("g: show next group", 10, 35);
		g.drawString("l: move right, j: move left, i: move up, m: move down: k: reset", 10, 55);
		g.drawString("Scaling ratio: " + _scale, 10, 75);

		g.setColor(Color.blue);

		if (_selectedGroupIdx == -1)
			g.drawString("Selected Group: all", 10, 95);
		else
			g.drawString("Selected Group: " + _groups.get(_selectedGroupIdx).getId(), 10, 95);
	}

	private void drawBodies(Graphics2D g) {
		super.paintComponent(g);

		for (Body b : _bodies) {
			if (isVisible(b)) {

				int x = (int) (b.getPosition().getX() / _scale + _centerX);
				int y = (int) (-b.getPosition().getY() / _scale + _centerY);
				Vector2D vel = b.getVelocity().direction().scale(25);
				Vector2D force = b.getForce().direction().scale(25);

				if (_showVectors) {
					// vector velocidad
					drawLineWithArrow(g, x + 5, y + 5, x + (int) (vel.direction().getX()), y - (int) (vel.getY()), 3, 4,
							Color.green, Color.green);
					// vector fuerza
					drawLineWithArrow(g, x + 5, y + 5, x + (int) (force.getX()), y - (int) (force.getY()), 3, 4,
							Color.red, Color.red);
				}
				g.setColor(_gColor.get(b.getgId()));
				g.fillOval(x, y, 10, 10);
				g.setColor(Color.black);
				g.drawString(b.getId(), x, y - 5);
				g.setColor(_gColor.get(b.getgId()));

			}
		}
	}

	private boolean isVisible(Body b) {
		return _selectedGroup == null || _selectedGroup.equals(b.getgId());
	}

	// calculates a value for scale such that all visible bodies fit in the window
	private void autoScale() {

		double max = 1.0;

		for (Body b : _bodies) {
			Vector2D p = b.getPosition();
			max = Math.max(max, Math.abs(p.getX()));
			max = Math.max(max, Math.abs(p.getY()));
		}

		double size = Math.max(1.0, Math.min(getWidth(), getHeight()));

		_scale = max > size ? 4.0 * max / size : 1.0;
	}

	@Override
	public void addGroup(BodiesGroup g) {
		_groups.add(g);
		_gColor.put(g.getId(), _colorGen.nextColor()); // assign color to group
		autoScale();
		update();
	}

	@Override
	public void addBody(Body b) {
		_bodies.add(b);
		autoScale();
		update();
	}

	@Override
	public void reset() {
		_groups.clear();
		_bodies.clear();
		_gColor.clear();
		_colorGen.reset(); // reset the color generator
		_selectedGroupIdx = -1;
		_selectedGroup = null;
		update();
	}

	@Override
	void update() {
		repaint();
	}

	// This method draws a line from (x1,y1) to (x2,y2) with an arrow.
	// The arrow is of height h and width w.
	// The last two arguments are the colors of the arrow and the line
	private void drawLineWithArrow(//
			Graphics g, //
			int x1, int y1, //
			int x2, int y2, //
			int w, int h, //
			Color lineColor, Color arrowColor) {

		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };

		g.setColor(lineColor);
		g.drawLine(x1, y1, x2, y2);
		g.setColor(arrowColor);
		g.fillPolygon(xpoints, ypoints, 3);
	}

}
