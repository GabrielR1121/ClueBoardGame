package Clue;

import java.awt.*;

// Enumerator for the colors with their RGB code
public enum Colors {
	Scarlett("255,36,0"), Plum("109,66,148"), Orchid("214,110,210"), Green("0,125,0"), Mustard("166,127,1"),
	Peacock("47,92,250");

	private Color clr;

	private Colors(String rgb) {

		String[] strRGB = rgb.split(",");

		clr = new Color(Integer.parseInt(strRGB[0]), Integer.parseInt(strRGB[1]), Integer.parseInt(strRGB[2]));
	}

	public Color getColor() {
		return clr;
	}
}
