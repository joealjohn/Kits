package dev.continuum.kits.libs.utils.command.args.custom;

import dev.continuum.kits.libs.utils.command.args.exception.ArgumentParseException;
import dev.continuum.kits.libs.utils.command.impl.dispatcher.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomArgument<T> {
  @Nullable
  T parse(@NotNull CommandContext paramCommandContext, @Nullable String paramString) throws ArgumentParseException;
}


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\lib\\utils\command\args\custom\CustomArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */