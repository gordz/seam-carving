import java.awt.Color;



public class SeamCarver {
	
	private int[][] image;
	private double[][] energy;
	private int width;
	private int height;
	private boolean transposed = false;
	
	/**
	 * create a seam carver object based on the given picture
	 * @param picture
	 */
	public SeamCarver(final Picture picture)  {
		image = new int[picture.width()][picture.height()];
		for (int x = 0; x < picture.width(); x++) {;
			for (int y = 0; y < picture.height(); y++) {
				image[x][y] = picture.get(x, y).getRGB();
			}
		}
		this.width = picture.width();
		this.height = picture.height();
		this.energy = calculateEnergyMatrix();
	}
	
	/**
	 * current picture
	 * @return
	 */
	public Picture picture() {
		if (transposed) {
			transpose();
		}
		
		final Picture picture = new Picture(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				picture.set(x, y, new Color(image[x][y]));
			}
		}
		return picture;
	}
	
	/**
	 * width of current picture
	 * @return
	 */
	public int width()  {
		return width;
	}
	
	/**
	 * height of current picture
	 * @return
	 */
	public int height() {
		return height;
	}
	
	/**
	 * energy of pixel at column x and row y
	 * @param x
	 * @param y
	 * @return
	 * @throws IndexOutOfBoundsException If the x or y indexes are out of bounds.
	 */
	public double energy(int x, int y) {
		if (x < 0 || x > width - 1) {
			throw new IndexOutOfBoundsException("x index ouf of bounds.");
		}
		
		if (y < 0 || y > height - 1) {
			throw new IndexOutOfBoundsException("y index ouf of bounds.");
		}
		
		// Edge pixels have energe 255^2 + 255^2 + 255^2.
		if (x == 0 || y == 0 || x == width -1 || y == height - 1) {
			return 195075;	
		}

		final Color leftPixel = new Color(image[x - 1][y]);
		final Color rightPixel = new Color(image[x + 1][y]);
		final Color topPixel = new Color(image[x][y - 1]);
		final Color bottomPixel = new Color(image[x][y + 1]);
		
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
		if (!transposed) {
			transpose();
		}
		
		return findSeam();
	}
	
	private int[] findSeam() {
		int[] edgeTo = new int[(width * height) + 2];
		double[] distTo = new double[(width * height) + 2];
		
		for (int i = 0; i < (width * height) + 2; i++) {
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

		for (int vertex = 1; vertex < (width * height) + 2; vertex++) {
			int[] adj = adj(vertex);
			for (int adjacent : adj) {
				if (distTo[vertex] + energy(adjacent) < distTo[adjacent]) {
					distTo[adjacent] = distTo[vertex] + energy(adjacent);
					edgeTo[adjacent] = vertex;
				}
			}
		}
		
		int finalVertex = edgeTo[pixelToVertex(width - 1, height - 1) + 1];
	
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
	
	
	private void transpose() {
		System.out.println("Transposing image. Original width :" + width + ", height: " + height);
		int[][] transposedImage = new int[image[0].length][image.length];
		double[][] transposedEnergy = new double[energy[0].length][energy.length];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				transposedImage[y][x] = image[x][y];
				transposedEnergy[y][x] = energy[x][y];
			}
		}
		
		// Swap width and height.
		int tempWidth = this.width;
		this.width = this.height;
		this.height = tempWidth;
		
		if (transposed) {
			transposed = false;
		} else {
			transposed = true;
		}
		
		image = transposedImage;
		energy = transposedEnergy;
	}
	
	/**
	 * sequence of indices for vertical seam
	 * @return
	 */
	public int[] findVerticalSeam() {
		if (transposed) {
			transpose();
		}
		return findSeam();
	}
	
	private double energy(int vertex) {
		if (vertex == 0 || vertex == (width * height) + 1) {
			return 0;
			
		}
		Pixel pixel = vertexToPixel(vertex);
		return energy[pixel.x][pixel.y];
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
		int x = ((vertex - 1) % width);
		int y = ((vertex - 1) / width);
		return new Pixel(x, y);
	}

	private int pixelToVertex(int x, int y) {
		return (y * width) + x + 1;
	}
	
	
	private int[] adj(int vertex) {
		
		if (vertex == 0) {
			int[] adj = new int[width];
			for (int i = 0 ; i < width; i++) {
				adj[i] = i + 1;
			}
			return adj;
		} else if (vertex == (width * height) + 1) {
			return new int[0];
		} else if (vertex > ((width * height) - width)) {
			return new int[] {(width * height) + 1};
		}
		
		
		Pixel pixel = vertexToPixel(vertex);
		int x = pixel.x;
		int y = pixel.y;
		
		int[] adj;
		if (x == 0) {
			adj = new int[2];
			adj[0] = pixelToVertex(x, y + 1);
			adj[1] = pixelToVertex(x + 1, y + 1);
		} else if (x == width - 1) {
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
	private double[][] calculateEnergyMatrix() {
		final double[][] energy = new double[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				energy[x][y] = energy(x, y);
			}
		}
		return energy;
	}
	
	/**
	 * remove vertical seam from current picture
	 * @param seam
	 * @throws NullPointerException When <code>seam</code> is null.
	 */
	private void removeSeam(int[] seam) {
		if (seam == null) {
			throw new NullPointerException("seam must not be null.");
		}
		
		if (seam.length != height) {
			throw new IllegalArgumentException("Seam must be same length as image height. Transposed: " + transposed + ", Seam length:" + seam.length + ", image height: " + height + ", array height: " + image[0].length + ", image width:" + width + ", array width:" + image.length);
		}
		
		
		if (width <= 1) {
			throw new IllegalArgumentException("Image width is <= 1, no more seams to remove.");
		}
		
		validateSeam(seam);
		
		int[][] newPicture = new int[width - 1][height];
		
		// Remove seam and create new image.
		for (int y = 0; y < height; y++) {
			// For each row.
			int newX = 0;
			for (int x = 0; x < width; x++) {
				// Ensure seam index is within the image bounds.
				try {
				if (seam[y] < 0 || seam[y] > (width - 1)) {
					throw new IllegalArgumentException("Seam index out of bounds.");
				}
				} catch (IndexOutOfBoundsException ex) {
					System.out.println("Caught exception: " + ex.getMessage());
					System.out.println("Seam length:" + seam.length + ", y: " + y + ", width: " + width + ", height: " + height + ", x: " + y + ", y:" + y + ", transposed: " + transposed);
					throw ex;
				}
				
				if (seam[y] == x) {
					continue;
				} else {
					newPicture[newX++][y] = image[x][y];
				}
			}
		}
		
		this.width = this.width -1;
		image = newPicture;
		energy = calculateEnergyMatrix();
	}
	
	/**
	 * remove horizontal seam from current picture
	 * @param seam
	 * @throws NullPointerException When <code>seam</code> is null.
	 */
	public void removeHorizontalSeam(int[] seam) {
		if (!transposed) {
			transpose();
		}
		removeSeam(seam);
	}
	
	/**
	 * remove vertical seam from current picture
	 * @param seam
	 * @throws NullPointerException When <code>seam</code> is null.
	 */
	public void removeVerticalSeam(int[] seam) {
		if (transposed) {
			transpose();
		}
		removeSeam(seam);
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

