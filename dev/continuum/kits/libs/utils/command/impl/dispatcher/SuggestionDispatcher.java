package dev.continuum.kits.libs.utils.command.impl.dispatcher;

import dev.continuum.kits.libs.utils.command.impl.suggestions.Suggestions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SuggestionDispatcher {
  @Nullable
  Suggestions suggest(@NotNull CommandContext paramCommandContext);
}


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\impl\dispatcher\SuggestionDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */