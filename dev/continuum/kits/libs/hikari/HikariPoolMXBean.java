package dev.continuum.kits.libs.hikari;

public interface HikariPoolMXBean {
  int getIdleConnections();
  
  int getActiveConnections();
  
  int getTotalConnections();
  
  int getThreadsAwaitingConnection();
  
  void softEvictConnections();
  
  void suspendPool();
  
  void resumePool();
}


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\HikariPoolMXBean.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */