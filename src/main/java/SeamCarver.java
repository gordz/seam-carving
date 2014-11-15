import java.awt.Color;



public class SeamCarver {
	
	
	private Picture picture;
	private double[][] energy;

	/**
	 * create a seam carver object based on the given picture
	 * @param picture
	 */
	public SeamCarver(final Picture picture)  {
		this.picture = new Picture(picture);
		this.energy = calculateEnergyMatrix(this.picture);
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
		int[] seam = new int[picture.width()];
		return seam;
	}
	
	/**
	 * sequence of indices for vertical seam
	 * @return
	 */
	public int[] findVerticalSeam() {
		final int[] seam = new int[picture.height()];
		
		double[] edgeTo = new double[(picture.height() * picture.width())];
		double[] distTo = new double[(picture.height() * picture.width())];
		
		for (int i = 0; i < distTo.length; i++) {
			distTo[i] = Double.POSITIVE_INFINITY;
			edgeTo[i] = Double.POSITIVE_INFINITY;
		}
		
		MinPQ<Pixel> minPQ = new MinPQ<SeamCarver.Pixel>();
		
		// Handle first row.
		for (int i = 0; i < picture.width(); i++) {
			distTo[i] = energy[i][0];
			minPQ.insert(new Pixel(i, 0, energy[i][0]));
		}
		
		while (!minPQ.isEmpty()) {
			Pixel pixel = minPQ.delMin();
			
		}
	}
	
	private void relax(Pixel pixel, MinPQ<Pixel> minPQ) {
		for (Pixel adjacent : adj(pixel)) {
			minPQ.insert(adjacent);
			if ()
		}
		
	}
	
	static class Pixel implements Comparable<Pixel> {
		
		private final int x;
		private final int y;
		private final double energy;
		
		Pixel(final int x, final int y, final double energy) {
			this.x = x;
			this.y = y;
			this.energy = energy;
		}

		public int compareTo(Pixel o) {
			return Double.compare(energy, o.energy);
		}
		
		
	}
	
	Pixel[] adj(Pixel pixel) {
		Pixel[] adj;
		if (pixel.x == 0) {
			adj = new Pixel[2];
			adj[0] = new Pixel (pixel.x, pixel.y + 1, energy[pixel.x][pixel.y + 1]);
			adj[1] = new Pixel (pixel.x + 1, pixel.y + 1, energy[pixel.x + 1][pixel.y + 1]);
		} else if (pixel.x == picture.width() - 1) {
			adj = new Pixel[2];;
			adj[0] = new Pixel(pixel.x - 1, pixel.y + 1, energy[pixel.x - 1][pixel.y + 1]);
			adj[1] = new Pixel(pixel.x, pixel.y + 1, energy[pixel.x][pixel.y + 1]);
		} else {
			adj = new Pixel[2];
			adj[0] = new Pixel(pixel.x - 1, pixel.y + 1, energy[pixel.x - 1][pixel.y + 1]);
			adj[1] = new Pixel(pixel.x, pixel.y + 1, energy[pixel.x][pixel.y + 1]);
			adj[2] = new Pixel (pixel.x + 1, pixel.y + 1, energy[pixel.x + 1][pixel.y + 1]);
		}
		return adj;
	}
	
	/**
	 * Calculate the energy for each pixel in a {@link Picture}.
	 * @param picture
	 * @return
	 */
	private double[][] calculateEnergyMatrix(final Picture picture) {
		final double[][] energy = new double[picture.width()][picture.height()];
		for (int x = 0; x < picture.width(); x++) {
			for (int y = 0; y < picture.height(); y++) {
				energy[x][y] = energy(x, y);
			}
		}
		return energy;
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

	
	EdgeWeightedDigraph constructDigraph() {
		// TODO Auto-generated method stub
		return new EdgeWeightedDigraph(picture.height() * picture.width());
		
	}
}

