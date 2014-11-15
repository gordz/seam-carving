import java.awt.Color;



public class SeamCarver {
	
	
	private Picture picture;
	
	private static final Color EDGE_PIXEL = new Color(255, 255, 255);
	
	/**
	 * create a seam carver object based on the given picture
	 * @param picture
	 */
	public SeamCarver(Picture picture)  {
		this.picture = new Picture(picture);
	}
	
	/**
	 * current picture
	 * @return
	 */
	public Picture picture() {
		return null;
	}
	
	/**
	 * width of current picture
	 * @return
	 */
	public int width()  {
		return -1;
	}
	
	/**
	 * height of current picture
	 * @return
	 */
	public int height() {
		return -1;
	}
	
	/**
	 * energy of pixel at column x and row y
	 * @param x
	 * @param y
	 * @return
	 * @throws IndexOutOfBoundsException If the x or y indexes are out of bounds.
	 */
	public double energy(int x, int y) {
		if (x < 0 || x > picture.width()) {
			throw new IndexOutOfBoundsException("x index ouf of bounds.");
		}
		
		if (y < 0 || y > picture.height()) {
			throw new IndexOutOfBoundsException("y index ouf of bounds.");
		}
		
		// Edge pixels have energe 255^2 + 255^2 + 255^2.
		if (x == 0 || y == 0 || x == picture.width() -1 || y == picture.height() - 1) {
			return 195075;	
		}

		final Color leftPixel = picture.get(x - 1, y);
		final Color rightPixel = picture.get(x + 1, y);
		final Color topPixel = picture.get(x, y - 1);
		final Color bottomPixel = picture.get(x, y + 1);
		
		final int rX = Math.abs(leftPixel.getRed() - rightPixel.getRed());
		final int bX = Math.abs(leftPixel.getBlue() - rightPixel.getBlue());
		final int gX = Math.abs(leftPixel.getGreen() - rightPixel.getGreen());
		
		final int rY = Math.abs(topPixel.getRed() - bottomPixel.getRed());
		final int bY = Math.abs(topPixel.getBlue() - bottomPixel.getBlue());
		final int gY = Math.abs(topPixel.getGreen() - bottomPixel.getGreen());
		
		final double xGradientSquare = Math.pow(rX, 2) + Math.pow(bX, 2)  + Math.pow(gX, 2);
		final double yGradientSquare = Math.pow(rY, 2) + Math.pow(bY, 2) + Math.pow(gY, 2);
		final double energy = xGradientSquare + yGradientSquare;
		return energy;
	}
	
	/**
	 * sequence of indices for horizontal seam
	 * @return
	 */
	public int[] findHorizontalSeam() {
		return null;
	}
	
	/**
	 * sequence of indices for vertical seam
	 * @return
	 */
	public int[] findVerticalSeam() {
		return null;
	}
	
	/**
	 * remove horizontal seam from current picture
	 * @param seam
	 * @throws NullPointerException When <code>seam</code> is null.
	 */
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null) {
			throw new NullPointerException("seam must not be null.");
		}
	}
	
	/**
	 * remove vertical seam from current picture
	 * @param seam
	 * @throws NullPointerException When <code>seam</code> is null.
	 */
	public void removeVerticalSeam(int[] seam) {
		if (seam == null) {
			throw new NullPointerException("seam must not be null.");
		}
	}
}

