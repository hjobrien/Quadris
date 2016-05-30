package cerulean;

import static org.junit.Assert.*;

import org.junit.Test;

public class JUnitPathTest {

  @Test
  public void testEquals() {
    Path path1 = new Path(new int[]{1,1,1});
    Path path2 = new Path(new int[]{1,1,1});
    assertEquals(path1.equals(path2), true);
  }
  
  @Test
  public void testHashCode() {
    Path path1 = new Path(new int[]{1,1,1});
    Path path2 = new Path(new int[]{1,1,1});
    assertEquals(path1.hashCode(), path2.hashCode());
    assertFalse(path1 == path2);
    Path path3 = new Path(new int[]{0,0,0});
    assertTrue(path1.hashCode() != path3.hashCode());
    Path path4 = new Path(new int[]{1,1,1,1});
    assertTrue(path1.hashCode() != path4.hashCode());
  }

}
