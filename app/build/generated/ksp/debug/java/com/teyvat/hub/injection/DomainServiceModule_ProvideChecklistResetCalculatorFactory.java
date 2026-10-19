package com.teyvat.hub.injection;

import com.teyvat.hub.domain.service.ChecklistResetCalculator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DomainServiceModule_ProvideChecklistResetCalculatorFactory implements Factory<ChecklistResetCalculator> {
  @Override
  public ChecklistResetCalculator get() {
    return provideChecklistResetCalculator();
  }

  public static DomainServiceModule_ProvideChecklistResetCalculatorFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ChecklistResetCalculator provideChecklistResetCalculator() {
    return Preconditions.checkNotNullFromProvides(DomainServiceModule.INSTANCE.provideChecklistResetCalculator());
  }

  private static final class InstanceHolder {
    static final DomainServiceModule_ProvideChecklistResetCalculatorFactory INSTANCE = new DomainServiceModule_ProvideChecklistResetCalculatorFactory();
  }
}
