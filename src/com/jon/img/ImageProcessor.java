package com.jon.img;

// By Jon Healy
// ISTE 222.01
// Exercise 4
// 4/27/21

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImageProcessor {

	static int processPixel(int p, String type) {
		int a = (p >> 24) & 0xff;
		int r = (p >> 16) & 0xff;
		int g = (p >> 8) & 0xff;
		int b = p & 0xff;

		if (type.equalsIgnoreCase("negative")) {
			r = 255 - r;
			g = 255 - g;
			b = 255 - b;
		} else if (type.equalsIgnoreCase("grayscale")) {
			r = g = b = (r + g + b) / 3;
		} else if (type.equalsIgnoreCase("sepia")) {
			int newR = (int) (0.393 * r + 0.769 * g + 0.189 * b);
			int newG = (int) (0.349 * r + 0.686 * g + 0.168 * b);
			int newB = (int) (0.272 * r + 0.534 * g + 0.131 * b);

			r = Math.min(newR, 255);
			g = Math.min(newG, 255);
			b = Math.min(newB, 255);
		} else {
			throw new IllegalArgumentException("Type " + type + "is not supported!");
		}

		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	static void processImage(String inFilename, String outFilename, String type) {
		BufferedImage img = null;
		File f = null;

		// load image
		try {
			f = new File(inFilename);
			img = ImageIO.read(f);
		} catch (IOException e) {
			System.out.println(e);
		}

		// get image width and height
		int width = img.getWidth();
		int height = img.getHeight();

		// process all pixels in the image
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int p = img.getRGB(x, y);
				p = processPixel(p, type);
				img.setRGB(x, y, p);
			}
		}

		// write image to file
		try {
			f = new File(outFilename);
			ImageIO.write(img, "jpg", f);
			System.out.println("Generated image " + f.getName() + " at path: " + f.getPath());
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String args[]) {
		processImage("images" + File.separator + "Taj.jpg", "images" + File.separator + "Taj_negative.jpg", "negative");
		processImage("images" + File.separator + "Taj.jpg", "images" + File.separator + "Taj_sepia.jpg", "sepia");
		processImage("images" + File.separator + "Taj.jpg", "images" + File.separator + "Taj_grayscale.jpg", "grayscale");
	}

}
