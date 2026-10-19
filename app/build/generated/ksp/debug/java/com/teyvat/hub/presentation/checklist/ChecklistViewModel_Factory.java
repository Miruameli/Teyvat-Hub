package com.teyvat.hub.presentation.checklist;

import com.teyvat.hub.application.ChecklistRepository;
import com.teyvat.hub.domain.service.ChecklistResetCalculator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ChecklistViewModel_Factory implements Factory<ChecklistViewModel> {
  private final Provider<ChecklistRepository> repositoryProvider;

  private final Provider<ChecklistResetCalculator> resetCalculatorProvider;

  private ChecklistViewModel_Factory(Provider<ChecklistRepository> repositoryProvider,
      Provider<ChecklistResetCalculator> resetCalculatorProvider) {
    this.repositoryProvider = repositoryProvider;
    this.resetCalculatorProvider = resetCalculatorProvider;
  }

  @Override
  public ChecklistViewModel get() {
    return newInstance(repositoryProvider.get(), resetCalculatorProvider.get());
  }

  public static ChecklistViewModel_Factory create(Provider<ChecklistRepository> repositoryProvider,
      Provider<ChecklistResetCalculator> resetCalculatorProvider) {
    return new ChecklistViewModel_Factory(repositoryProvider, resetCalculatorProvider);
  }

  public static ChecklistViewModel newInstance(ChecklistRepository repository,
      ChecklistResetCalculator resetCalculator) {
    return new ChecklistViewModel(repository, resetCalculator);
  }
}
