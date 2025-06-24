package dev.continuum.kits.libs.hikari;

public interface HikariConfigMXBean {
  long getConnectionTimeout();
  
  void setConnectionTimeout(long paramLong);
  
  long getValidationTimeout();
  
  void setValidationTimeout(long paramLong);
  
  long getIdleTimeout();
  
  void setIdleTimeout(long paramLong);
  
  long getLeakDetectionThreshold();
  
  void setLeakDetectionThreshold(long paramLong);
  
  long getMaxLifetime();
  
  void setMaxLifetime(long paramLong);
  
  int getMinimumIdle();
  
  void setMinimumIdle(int paramInt);
  
  int getMaximumPoolSize();
  
  void setMaximumPoolSize(int paramInt);
  
  void setPassword(String paramString);
  
  void setUsername(String paramString);
  
  String getPoolName();
  
  String getCatalog();
  
  void setCatalog(String paramString);
}


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\libs\hikari\HikariConfigMXBean.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */