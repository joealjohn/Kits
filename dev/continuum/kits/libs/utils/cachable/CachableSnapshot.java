package dev.continuum.kits.libs.utils.cachable;

import dev.continuum.kits.libs.utils.model.Tuple;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CachableSnapshot<K, V> {
  Map<K, V> asMap();
  
  List<Tuple<K, V>> asList();
  
  Collection<Tuple<K, V>> asCollection();
  
  Tuple<K, V>[] asTupleArray();
}


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\cachable\CachableSnapshot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */