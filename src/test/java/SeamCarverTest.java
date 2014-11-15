import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

import java.awt.Color;

import junit.framework.Assert;

import org.junit.Test;



public class SeamCarverTest {
	
	private Picture picture = new Picture(10,20);
	
	
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
		Assert.fail();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removeHorizonalSeam_ShouldThrowIllegalArgumentException_WhenPictureHeightEqualTo_0() {
		Assert.fail();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removeVerticalSeam_ShouldThrowIllegalArgumentException_WhenPictureWidthEqualTo_1() {
		Assert.fail();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removeVerticalSeam_ShouldThrowIllegalArgumentException_WhenPictureHWidthEqualTo_0() {
		Assert.fail();
	}
	
	@Test
	public void findVerticalSeam_ShouldReturnSeam_WhenImageIsNotEmpty() {
		Picture picture = new Picture("6x5.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] seam = seamCarver.findVerticalSeam();
		assertArrayEquals(new int[] { 2, 3, 3, 3, 2 }, seam);
	}
}
