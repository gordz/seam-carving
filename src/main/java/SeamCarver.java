

public class SeamCarver {
	
	
	private Picture picture;
	
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
		
		
		return -1;
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
	 */
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null) {
			throw new NullPointerException("seam must not be null.");
		}
	}
	
	/**
	 * remove vertical seam from current picture
	 * @param seam
	 */
	public void removeVerticalSeam(int[] seam) {
		if (seam == null) {
			throw new NullPointerException("seam must not be null.");
		}
	}
}

