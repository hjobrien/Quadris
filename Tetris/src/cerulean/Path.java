package cerulean;

import java.util.Arrays;

/**
 * wrapper class for int[]. Supports behavior that requires hashing based on value and not memory
 * addresses. This means two separate Path objects with the same values in their path field will hash
 * to the same value
 * 
 * @author Hank O'Brien
 *
 */
@Deprecated
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

  public String toString(){
	  String pathFormula = "";
	  for (int i : path){
		  pathFormula += i + " ";
	  }
	  return pathFormula;
  }

}
