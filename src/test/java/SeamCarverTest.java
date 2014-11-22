import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

import java.awt.Color;

import org.junit.Assert;
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
	public void energy_ShouldThrowIndexOutOfBoundsException_WhenXGreaterThanWidth() {
		seamCarver.energy(11, 1);
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void energy_ShouldThrowIndexOutOfBoundsException_WhenYGreaterThanHeight() {
		seamCarver.energy(1, 21);
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
	
	@Test
	public void findHorizontalSeam_ShouldReturnSeam_For3x7_WhenImageIsNotEmpty() {
		Picture picture = new Picture("seamCarving/3x7.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] seam = seamCarver.findHorizontalSeam();
		assertArrayEquals(new int[] { 1, 2, 1}, seam);
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
	public void removeHorizontalSeam_ShouldRemoveSeam() {
		Picture picture = new Picture("seamCarving/6x5.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		seamCarver.removeHorizontalSeam(new int[] {2, 3, 3, 3, 2, 1});
	}
	


	@Test
	public void printImage() {
		Picture picture = new Picture(PICTURE_6x5);
		System.out.println(toString(picture));
	}
	
	private String toString(Picture picture) {
		StringBuilder row = new StringBuilder();

		for (int y = 0; y < picture.height(); y++) {
			row.append("{ ");
			for (int x = 0; x < picture.width(); x++) {
				row.append(picture.get(x, y).getRGB());
				if (x != picture.width() - 1) {
					row.append(" ,");
				}
			}
			row.append(" }\n");
		}
		//row.append(" }\n");

		return row.toString();	
	}
	
	@Test
	public void picture_ShouldReturnCurrentPicture() {
		Picture picture = new Picture(PICTURE_6x5);
		SeamCarver carver = new SeamCarver(picture);
		Assert.assertEquals(picture, carver.picture());
	}
	
	
	// TODO update energy calculations.
	@Test
	public void removeVerticalSeam_ShouldRemoveVerticalSeam() {
		Picture picture = new Picture(PICTURE_6x5);
		SeamCarver carver = new SeamCarver(picture);
		System.out.println(toString(carver.picture()));
		carver.removeVerticalSeam(carver.findVerticalSeam());
		
		// Check dimentions.
		assertThat(carver.picture().width(), equalTo(5));
		assertThat(carver.picture().height(), equalTo(5));
		
		int[][] expectedImage = {
			{ -10399125 ,-2315123 ,-8278562 ,-2021167 ,-12423717 },
			{ -4895164 ,-15786792 ,-682346 ,-3289679 ,-7128477 },
			{ -3874795 ,-5842498 ,-8357726 ,-13527927 ,-4168359 },
			{ -11301017 ,-9547785 ,-9812050 ,-8308590 ,-8818797 },
			{ -11362935 ,-10719615 ,-10938151 ,-3193568 ,-6524897 }
		};
		
		Picture expectedPicture = new Picture(expectedImage[0].length, expectedImage.length);
		for (int y = expectedImage.length - 1; y >= 0; y-- ) {
			for (int x = 0; x < expectedImage[0].length; x++) {
				expectedPicture.set(x, (expectedImage.length - 1 - y), new Color(expectedImage[x][y]));
			}
		}
		
		for (int x = 0; x < 4; x++) {
			System.out.println(String.format("original %s,  new: %s", picture.get(x, 0).getRGB(), expectedPicture.get(x, 0).getRGB()));
		}

		//System.out.println("Original image:\n " + toString(picture));
		//System.out.println("New image:\n " + toString(expectedPicture));
		
		
		
		/*
		Picture expectedPicture = new Picture(expectedImage[0].length, expectedImage.length);
		for (int y = expectedImage.length - 1; y >= 0; y-- ) {
			for (int x = 0; x < expectedImage[0].length; x++) {
				expectedPicture.set(x, (expectedImage.length - 1 - y), new Color(expectedImage[x][y]));
			}
		}
		
		Assert.assertEquals(carver.picture(), expectedPicture);
		*/
	}	
	
	@Test
	public void removeVerticalAndHorizontal() {
		Picture picture = new Picture(PICTURE_6x5);
		SeamCarver carver = new SeamCarver(picture);
		carver.removeHorizontalSeam(carver.findHorizontalSeam());
		carver.removeVerticalSeam(carver.findVerticalSeam());
		carver.removeHorizontalSeam(carver.findHorizontalSeam());
		carver.removeVerticalSeam(carver.findVerticalSeam());
		carver.removeHorizontalSeam(carver.findHorizontalSeam());
	}
}
