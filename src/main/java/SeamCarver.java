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
		return picture;
	}
	
	/**
	 * width of current picture
	 * @return
	 */
	public int width()  {
		return picture.width();
	}
	
	/**
	 * height of current picture
	 * @return
	 */
	public int height() {
		return picture.height();
	}
	
	/**
	 * energy of pixel at column x and row y
	 * @param x
	 * @param y
	 * @return
	 * @throws IndexOutOfBoundsException If the x or y indexes are out of bounds.
	 */
	public double energy(int x, int y) {
		if (x < 0 || x > picture.width() - 1) {
			throw new IndexOutOfBoundsException("x index ouf of bounds.");
		}
		
		if (y < 0 || y > picture.height() - 1) {
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
		// Transpose image.
		final Picture transposed = transpose(picture);
		this.picture = transposed;
		energy = calculateEnergyMatrix(transposed);
		return findVerticalSeam(transposed);
	}
	
	/**
	 * Transpose a Picture.
	 * @param picture
	 * @return
	 */
	private Picture transpose(final Picture picture) {
		final Picture transposed = new Picture(picture.height(), picture.width());
		for (int y = 0; y < picture.height(); y++) {
			for (int x = 0; x < picture.width(); x++) {
				transposed.set(y, x, picture.get(x, y));
			}
		}
		return transposed;
	}
	
	private int[] findVerticalSeam(Picture image) {
		int[] edgeTo = new int[(image.width() * image.height()) + 2];
		double[] distTo = new double[(image.width() * image.height()) + 2];
		
		for (int i = 0; i <= (image.width() * image.height()) + 1; i++) {
			edgeTo[i] = -1;
			distTo[i] = Double.POSITIVE_INFINITY;
		}
	
		// Initialise source vertex.
		distTo[0] = 0;
		edgeTo[0] = -1;
		
		int[] adji = adj(0);
		for (int adjacent : adji) {
			distTo[adjacent] = 195075;
			edgeTo[adjacent] = 0;
		}

		for (int vertex = 1; vertex <= (image.width() * image.height()) + 1; vertex++) {
			int[] adj = adj(vertex);
			for (int adjacent : adj) {
				if (distTo[vertex] + energy(adjacent) < distTo[adjacent]) {
					distTo[adjacent] = distTo[vertex] + energy(adjacent);
					edgeTo[adjacent] = vertex;
				}
			}
		}
		
		int finalVertex = edgeTo[pixelToVertex(image.width() - 1, image.height() - 1) + 1];
	
		Stack<Integer> path = new Stack<Integer>();
		for (int v = finalVertex; v != 0; v = edgeTo[v]) {
			path.push(v);
		}
		
		final int[] seam = new int[path.size()];
 		for (int i = 0; i < seam.length; i++) {
 			int vertex = path.pop();
 			seam[i] = vertexToPixel(vertex).x;
		}
 		return seam;
	}
	
	/**
	 * sequence of indices for vertical seam
	 * @return
	 */
	public int[] findVerticalSeam() {
		return findVerticalSeam(picture);
	}
	
	private double energy(int vertex) {
		if (vertex == 0 || vertex == ((picture.width() * picture.height()) + 1)) {
			return 0;
			
		}
		Pixel pixel = vertexToPixel(vertex);
		return energy[pixel.y][pixel.x];
	}
	
	
 
	private static class Pixel {
		int x;
		int y;
	
		public Pixel(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString() {
			return "Pixel (" + x + ", " + y + ")";
		}
	}
	
	private Pixel vertexToPixel(int vertex) {
		int x = ((vertex - 1) % picture.width());
		int y = ((vertex - 1) / picture.width());
		return new Pixel(x, y);
	}

	private int pixelToVertex(int x, int y) {
		return (y * picture.width()) + x + 1;
	}
	
	
	private int[] adj(int vertex) {
		
		if (vertex == 0) {
			int[] adj = new int[picture.width()];
			for (int i = 0 ; i < picture.width(); i++) {
				adj[i] = i + 1;
			}
			return adj;
		} else if (vertex == (picture.width() * picture.height()) + 1) {
			return new int[0];
		} else if (vertex > ((picture.width() * picture.height()) - picture.width())) {
			return new int[] {(picture.width() * picture.height()) + 1};
		}
		
		
		Pixel pixel = vertexToPixel(vertex);
		int x = pixel.x;
		int y = pixel.y;
		
		int[] adj;
		if (x == 0) {
			adj = new int[2];
			adj[0] = pixelToVertex(x, y + 1);
			adj[1] = pixelToVertex(x + 1, y + 1);
		} else if (x == picture.width() - 1) {
			adj = new int[2];
			adj[0] = pixelToVertex(x - 1, y + 1);
			adj[1] = pixelToVertex(x, y + 1);
		} else {
			adj = new int[3];
			adj[0] = pixelToVertex(x - 1, y + 1);
			adj[1] = pixelToVertex(x, y + 1);
			adj[2] = pixelToVertex(x + 1, y + 1);
		}
		return adj;
	}
	
	/**
	 * Calculate the energy for each pixel in a {@link Picture}.
	 * @param picture
	 * @return
	 */
	private double[][] calculateEnergyMatrix(final Picture picture) {
		final double[][] energy = new double[picture.height()][picture.width()];
		for (int x = 0; x < picture.width(); x++) {
			for (int y = 0; y < picture.height(); y++) {
				energy[y][x] = energy(x, y);
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
		
		if (picture.height() <= 1) {
			throw new IllegalArgumentException("Image height is <= 1, no more seams to remove.");
		}
		
		if (seam.length > picture.width()) {
			throw new IllegalArgumentException("Seam length exceeds image width.");
		}
		
		validateSeam(seam);
		
		Picture transposed = transpose(picture);
		double[][] transposedEnergy = calculateEnergyMatrix(transposed);
		
		
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
		
		if (seam.length > picture.height()) {
			throw new IllegalArgumentException("Seam length exceeds image height.");
		}
		
		if (picture.width() <= 1) {
			throw new IllegalArgumentException("Image width is <= 1, no more seams to remove.");
		}
		
		validateSeam(seam);
		
		Picture carvedImage = new Picture(picture.width() - 1, picture.height());
		
		// Remove seam and create new image.
		for (int y = 0; y < picture.height(); y++) {
			// For each row.
			int newX = 0;
			for (int x = 0; x < picture.width(); x++) {
				// Ensure seam index is within the image bounds.
				if (seam[y] < 0 || seam[y] > picture.width() - 1) {
					throw new IllegalArgumentException("Seam index out of bounds.");
				}
				
				if (seam[y] == x) {
					continue;
				} else {
					carvedImage.set(newX++, y, picture.get(x, y));
				}
			}
		}
		picture = carvedImage;
		
		// Recalculate energy
		energy = calculateEnergyMatrix(carvedImage);
	}
	
	/**
	 * Validate that a seam has no invalid entries.
	 * @param seam
	 * @throws InvalidArgumentException If the seam contains invalid entries.
	 */
	private void validateSeam(int[] seam) {
		for (int i = 0; i < seam.length - 1; i++) {
			if (Math.abs(seam[i] - seam[i + 1]) > 1) {
				throw new IllegalArgumentException("Seam contains invalid entries.");
			}
		}
	}
}

