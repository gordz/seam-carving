import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.awt.Color;

import org.junit.Test;



public class SeamCarverTest {
	
	private Picture picture = new Picture(10,20);
	
	public static final Picture PICTURE_6x5 = new Picture("seamCarving/6x5.png");
	public static final Picture PICTURE_1x5 = new Picture(1,5);
	public static final Picture PICTURE_5x1 = new Picture(5,1);
	
	private SeamCarver seamCarver = new SeamCarver(picture);
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void energy_ShouldThrowIndexOutOfBoundsException_WhenXLessThan_0() {
		seamCarver.energy(-1, 1);
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void energy_ShouldThrowIndexOutOfBoundsException_WhenYLessThan_0() {
		seamCarver.energy(1, -1);
	}
	
	@Test (expected = NullPointerException.class) 
	public void removeHorizontalSeam_ShouldThrowNullPointerException_WhenArgIsNull() {
		seamCarver.removeHorizontalSeam(null);
	}
	
	@Test (expected = NullPointerException.class) 
	public void removeVerticalSeam_ShouldThrowNullPointerException_WhenArgIsNull() {
		seamCarver.removeVerticalSeam(null);
	}
	
	@Test
	public void width_ShouldReturnImageWidth() {
		SeamCarver seamCarver = new SeamCarver(PICTURE_6x5);
		assertThat(seamCarver.width(), equalTo(6));
	}
	
	@Test
	public void height_ShouldReturnImageHeight() {
		SeamCarver seamCarver = new SeamCarver(PICTURE_6x5);
		assertThat(seamCarver.height(), equalTo(5));
	}
	
	@Test
	public void energy_ShouldReturnEnergyOfPixel() {
		Picture picture = new Picture(3,4);
		
		picture.set(0, 0, new Color(255, 101, 51));
		picture.set(1, 0, new Color(255, 101, 153));
		picture.set(2, 0, new Color(255, 101, 255));
		
		picture.set(0, 1, new Color(255, 153, 51));
		picture.set(1, 1, new Color(255, 153, 153));
		picture.set(2, 1, new Color(255, 153, 255));
		
		picture.set(0, 2, new Color(255, 203, 51));
		picture.set(1, 2, new Color(255, 204, 153));
		picture.set(2, 2, new Color(255, 205, 255));
		
		picture.set(0, 3, new Color(255, 255, 51));
		picture.set(1, 3, new Color(255, 255, 153));
		picture.set(2, 3, new Color(255, 255, 255));
		
		SeamCarver seamCarver = new SeamCarver(picture);
		assertThat(seamCarver.energy(0, 0), equalTo(195075D));
		assertThat(seamCarver.energy(1, 0), equalTo(195075D));
		assertThat(seamCarver.energy(2, 0), equalTo(195075D));
		
		assertThat(seamCarver.energy(0, 1), equalTo(195075D));
		assertThat(seamCarver.energy(1, 1), equalTo(52225D));
		assertThat(seamCarver.energy(2, 1), equalTo(195075D));
		
		assertThat(seamCarver.energy(0, 2), equalTo(195075D));
		assertThat(seamCarver.energy(1, 2), equalTo(52024D));
		assertThat(seamCarver.energy(2, 2), equalTo(195075D));
		
		assertThat(seamCarver.energy(0, 3), equalTo(195075D));
		assertThat(seamCarver.energy(1, 3), equalTo(195075D));
		assertThat(seamCarver.energy(2, 3), equalTo(195075D));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removeHorizonalSeam_ShouldThrowIllegalArgumentException_WhenPictureHeightEqualTo_1() {
		SeamCarver carver = new SeamCarver(PICTURE_5x1);
		carver.removeHorizontalSeam(new int[] {0, 0, 0, 0, 0});
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removeHorizonatlSeam_ShouldThrowIllegalArgumentException_WhenSeamLengthExceedsImageWidth() {
		SeamCarver carver = new SeamCarver(PICTURE_6x5);
		carver.removeHorizontalSeam(new int[PICTURE_6x5.width() + 1]);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removeVerticalSeam_ShouldThrowIllegalArgumentException_WhenPictureWidthEqualTo_1() {
		SeamCarver carver = new SeamCarver(PICTURE_1x5);
		carver.removeVerticalSeam(new int[] {0, 0, 0, 0, 0});
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removeVerticalSeam_ShouldThrowIllegalArgumentException_WhenSeamLengthExceedsImageHeight() {
		SeamCarver carver = new SeamCarver(PICTURE_6x5);
		carver.removeVerticalSeam(new int[PICTURE_6x5.height() + 1]);
	}
	
	@Test
	public void findVerticalSeam_ShouldReturnSeam_For6x5_WhenImageIsNotEmpty() {
		Picture picture = new Picture("seamCarving/6x5.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] seam = seamCarver.findVerticalSeam();
		assertArrayEquals(new int[] { 2, 3, 3, 3, 2 }, seam);
	}
	
	@Test
	public void findVerticalSeam_ShouldReturnSeam_For3x7_WhenImageIsNotEmpty() {
		Picture picture = new Picture("seamCarving/3x7.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] seam = seamCarver.findVerticalSeam();
		assertArrayEquals(new int[] { 0, 1, 1, 1, 1, 1, 0}, seam);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removeVerticalSeam_ShouldThrowIllegalArgumentException_WhenAdjacentSeamVerticesDifferByMoreThan_1() {
		Picture picture = new Picture("seamCarving/6x5.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		seamCarver.removeVerticalSeam(new int[] {0, 0, 2, 0, 0});
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removeHorizontalSeam_ShouldThrowIllegalArgumentException_WhenAdjacentSeamVerticesDifferByMoreThan_1() {
		Picture picture = new Picture("seamCarving/6x5.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		seamCarver.removeHorizontalSeam(new int[] {0, 0, 2, 0, 0, 0});
	}
	
	@Test
	public void adj_ShouldReturnAdjacentVerticesInLastRow_WhenOnSecondLastRow() {
		Picture picture = new Picture("6x5.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		
		assertEquals(seamCarver.adj(19)[0], 25);
		assertEquals(seamCarver.adj(19)[1], 26);
		
		assertEquals(seamCarver.adj(20)[0], 25);
		assertEquals(seamCarver.adj(20)[1], 26);
		assertEquals(seamCarver.adj(20)[2], 27);
		
		assertEquals(seamCarver.adj(21)[0], 26);
		assertEquals(seamCarver.adj(21)[1], 27);
		assertEquals(seamCarver.adj(21)[2], 28);
		
		assertEquals(seamCarver.adj(22)[0], 27);
		assertEquals(seamCarver.adj(22)[1], 28);
		assertEquals(seamCarver.adj(22)[2], 29);
		
		assertEquals(seamCarver.adj(23)[0], 28);
		assertEquals(seamCarver.adj(23)[1], 29);
		assertEquals(seamCarver.adj(23)[2], 30);
		
		assertEquals(seamCarver.adj(24)[0], 29);
		assertEquals(seamCarver.adj(24)[1], 30);
	}

	
	@Test
	public void adj_ShouldReturnAdjacentVerticesForNextRow_WhenNotOnLastRow() {
		Picture picture = new Picture("6x5.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		
		assertEquals(seamCarver.adj(1)[0], 7);
		assertEquals(seamCarver.adj(1)[1], 8);
		
		assertEquals(seamCarver.adj(2)[0], 7);
		assertEquals(seamCarver.adj(2)[1], 8);
		assertEquals(seamCarver.adj(2)[2], 9);
		
		assertEquals(seamCarver.adj(3)[0], 8);
		assertEquals(seamCarver.adj(3)[1], 9);
		assertEquals(seamCarver.adj(3)[2], 10);
		
		assertEquals(seamCarver.adj(4)[0], 9);
		assertEquals(seamCarver.adj(4)[1], 10);
		assertEquals(seamCarver.adj(4)[2], 11);
		
		assertEquals(seamCarver.adj(5)[0], 10);
		assertEquals(seamCarver.adj(5)[1], 11);
		assertEquals(seamCarver.adj(5)[2], 12);
		
		assertEquals(seamCarver.adj(6)[0], 11);
		assertEquals(seamCarver.adj(6)[1], 12);
	}
	
	@Test
	public void adj_ShouldReturnVirtualPixel_WhenOnLastRow() {
		Picture picture = new Picture("6x5.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		
		assertEquals(seamCarver.adj(25)[0], 31);
		assertEquals(seamCarver.adj(26)[0], 31);
		assertEquals(seamCarver.adj(27)[0], 31);
		assertEquals(seamCarver.adj(28)[0], 31);
		assertEquals(seamCarver.adj(29)[0], 31);
		assertEquals(seamCarver.adj(30)[0], 31);
	}

	
}
