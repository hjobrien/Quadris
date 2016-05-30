package cerulean;

import java.util.Arrays;

public class Path {
  private int[] path;
  
  public Path(int[] path){
    this.path = path;
  }
  
  public int[] getPath(){
    return path;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    for(Integer i : path){
      hashCode = hashCode * 31 + (i == null ? 0 : i.hashCode());
    }
    return hashCode;
  }

  /* (non-Javadoc)
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
