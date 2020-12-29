package com.ansorgit.plugins.bash.runner;

import com.ansorgit.plugins.bash.jetbrains.ExtendedRegexFilter;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;






















public class BashLineErrorFilter
  extends ExtendedRegexFilter
  implements Filter
{
  private static final String FILTER_REGEXP = "$FILE_PATH$: [a-zA-Z]+ $LINE$: .+";
  
  public BashLineErrorFilter(Project project) {
    super(project, "$FILE_PATH$: [a-zA-Z]+ $LINE$: .+");
  }
}
