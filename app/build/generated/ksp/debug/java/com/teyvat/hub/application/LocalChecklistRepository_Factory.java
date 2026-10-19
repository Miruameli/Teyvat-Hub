package com.teyvat.hub.application;

import com.teyvat.hub.data.local.checklist.ChecklistDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
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
public final class LocalChecklistRepository_Factory implements Factory<LocalChecklistRepository> {
  private final Provider<ChecklistDao> daoProvider;

  private LocalChecklistRepository_Factory(Provider<ChecklistDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public LocalChecklistRepository get() {
    return newInstance(daoProvider.get());
  }

  public static LocalChecklistRepository_Factory create(Provider<ChecklistDao> daoProvider) {
    return new LocalChecklistRepository_Factory(daoProvider);
  }

  public static LocalChecklistRepository newInstance(ChecklistDao dao) {
    return new LocalChecklistRepository(dao);
  }
}
