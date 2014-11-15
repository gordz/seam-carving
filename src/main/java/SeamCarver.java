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
	/*
	public int[] findVerticalSeam() {
		final int[] seam = new int[picture.height()];
		
		double[] edgeTo = new double[(picture.height() * picture.width())];
		double[] distTo = new double[(picture.height() * picture.width())];
		
		for (int i = 0; i < distTo.length; i++) {
			distTo[i] = Double.POSITIVE_INFINITY;
			edgeTo[i] = Double.POSITIVE_INFINITY;
		}
		
		return null;
	}
	*/
	
	/*
	public int[] findVerticalSeam() {
		final int[] seam = new int[picture.height()];
		
		double[][] edgeTo = new double[picture.width()][picture.height()];
		double[][] distTo = new double[picture.width()][picture.height()];
		
		for (int x = 0; x < picture.width(); x++) {
			for (int y = 0; y < picture.height(); y++) {
				edgeTo[x][y] = Double.POSITIVE_INFINITY;
				distTo[x][y] = Double.POSITIVE_INFINITY;
			}
		}
		
		return null;
	}
	*/
	
	/*
	public int[] findVerticalSeam() {
		final int[] seam = new int[picture.height()];
		
		double[][] edgeTo = new double[picture.width()][picture.height()];
		double[][] distTo = new double[picture.width()][picture.height()];
		
		for (int x = 0; x < picture.width(); x++) {
			for (int y = 0; y < picture.height(); y++) {
				edgeTo[x][y] = Double.POSITIVE_INFINITY;
				distTo[x][y] = Double.POSITIVE_INFINITY;
			}
		}
	
		MinPQ<Pixel> pq = new MinPQ<SeamCarver.Pixel>();
		
		for (int i = 0; i < picture.width(); i++) {
			distTo[i][0] = energy[i][0];
			pq.insert(new Pixel(i, 0, energy[i][0]));
		}
		
		while (!pq.isEmpty()) {
			Pixel next = pq.delMin();
			relax(next, pq, distTo, edgeTo);
		}
		
		
		
		return null;
	}
	*/
	

	public int[] findVerticalSeam() {
		final int[] seam = new int[picture.height()];
		
		int[] edgeTo = new int[(picture.width() * picture.height()) + 2];
		double[] distTo = new double[(picture.width() * picture.height()) + 2];
		
		for (int i = 0; i < (picture.width() * picture.height()) + 2; i++) {
			edgeTo[i] = -1;
			distTo[i] = Double.POSITIVE_INFINITY;
		}
	
		// Initialise source vertex.
		distTo[0] = 0;
		
		
		for (int x = 1; x <= picture.width(); x++) {
			distTo[x] = energy(x);
			edgeTo[x] = 0;
		}
	
		// For each vertex in topological order, relax the vertex.
		for (int y = 0; y < picture.height(); y++) {
			for (int x = 0; x < picture.width(); x++) {
				int vertex = pixelToVertex(x, y);
				int[] adj = adj(vertex);
				for (int adjacent : adj) {
					if (distTo[adjacent] > distTo[vertex] + energy(vertex)) {
						distTo[adjacent] = distTo[vertex] + energy(vertex);
						edgeTo[adjacent] = vertex;
					}
				}
			}
		}
		
		Stack<Integer> vertices = new Stack<Integer>();
		for (int v = edgeTo[edgeTo.length - 1]; v != -1; v = edgeTo[v]) {
			vertices.push(vertexToPixel(v).x);
		}
		
		return null;
	}
	
	double energy(int vertex) {
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
	
	
	int[] adj(int vertex) {
		
		if (vertex == 0) {
			int[] adj = new int[picture.width()];
			for (int i = 0 ; i < picture.width(); i++) {
				adj[i] = i + 1;
			}
			return adj;
		} else if (vertex == (picture.width() * picture.height()) + 1) {
			return new int[0];
		} else if (vertex >= ((picture.width() * picture.height()) - picture.width())) {
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
	
	
	
	int[] adj(int x, int y) {
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

