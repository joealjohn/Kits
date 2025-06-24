package dev.continuum.kits.config;

import dev.continuum.kits.libs.utils.elements.Elements;
import dev.continuum.kits.libs.utils.model.Tuple;
import org.jetbrains.annotations.NotNull;

public interface PlaceholderReplacements {
  @NotNull
  Elements<Tuple<String, String>> replacements(@NotNull Elements<Tuple<String, String>> paramElements);
}


/* Location:              C:\Users\joeal\Downloads\Kits-2.0.9.jar!\dev\continuum\kits\config\Messages$PlaceholderReplacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */