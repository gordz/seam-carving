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
}
