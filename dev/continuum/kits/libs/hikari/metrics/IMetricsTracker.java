package dev.continuum.kits.libs.hikari.metrics;

public interface IMetricsTracker extends AutoCloseable {
  default void recordConnectionCreatedMillis(long connectionCreatedMillis) {}
  
  default void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {}
  
  default void recordConnectionUsageMillis(long elapsedBorrowedMillis) {}
  
  default void recordConnectionTimeout() {}
  
  default void close() {}
}


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\metrics\IMetricsTracker.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */