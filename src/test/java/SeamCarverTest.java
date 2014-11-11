

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
}
