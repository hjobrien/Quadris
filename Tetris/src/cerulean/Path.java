package cerulean;

import java.util.Arrays;

/**
 * wrapper class for int[] that allows one to be used in a HashMap by changing the hashCode() method
 * 
 * @author Hank O'Brien
 *
 */
public class Path {
  private int[] path;

  public Path(int[] path) {
    this.path = path;
  }

  public int[] getPath() {
    return path;
  }

  /**
   * uses the hashcode implementation found in List
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    for (Integer i : path) {
      hashCode = hashCode * 31 + (i == null ? 0 : i.hashCode());
    }
    return hashCode;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof Path))
      return false;
    Path other = (Path) obj;
    if (!Arrays.equals(this.path, other.path))
      return false;
    return true;
  }



}
