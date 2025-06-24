package dev.continuum.kits.libs.utils.command.impl.dispatcher;

import dev.continuum.kits.libs.utils.command.CommandResult;
import org.jetbrains.annotations.NotNull;

public interface CommandDispatcher {
  @NotNull
  CommandResult run(@NotNull CommandContext paramCommandContext);
}


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\dispatcher\CommandDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */