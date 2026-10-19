package com.teyvat.hub.presentation.checklist;

import com.teyvat.hub.application.ChecklistRepository;
import com.teyvat.hub.domain.service.ChecklistResetCalculator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlin.jvm.functions.Function0;

@ScopeMetadata
@QualifierMetadata("javax.inject.Named")
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

  private final Provider<Function0<Long>> timeProvider;

  private ChecklistViewModel_Factory(Provider<ChecklistRepository> repositoryProvider,
      Provider<ChecklistResetCalculator> resetCalculatorProvider,
      Provider<Function0<Long>> timeProvider) {
    this.repositoryProvider = repositoryProvider;
    this.resetCalculatorProvider = resetCalculatorProvider;
    this.timeProvider = timeProvider;
  }

  @Override
  public ChecklistViewModel get() {
    return newInstance(repositoryProvider.get(), resetCalculatorProvider.get(), timeProvider.get());
  }

  public static ChecklistViewModel_Factory create(Provider<ChecklistRepository> repositoryProvider,
      Provider<ChecklistResetCalculator> resetCalculatorProvider,
      Provider<Function0<Long>> timeProvider) {
    return new ChecklistViewModel_Factory(repositoryProvider, resetCalculatorProvider, timeProvider);
  }

  public static ChecklistViewModel newInstance(ChecklistRepository repository,
      ChecklistResetCalculator resetCalculator, Function0<Long> timeProvider) {
    return new ChecklistViewModel(repository, resetCalculator, timeProvider);
  }
}
