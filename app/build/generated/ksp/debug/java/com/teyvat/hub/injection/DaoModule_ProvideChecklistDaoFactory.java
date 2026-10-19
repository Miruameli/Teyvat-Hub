package com.teyvat.hub.injection;

import com.teyvat.hub.data.TeyvatHubDatabase;
import com.teyvat.hub.data.local.checklist.ChecklistDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DaoModule_ProvideChecklistDaoFactory implements Factory<ChecklistDao> {
  private final Provider<TeyvatHubDatabase> databaseProvider;

  private DaoModule_ProvideChecklistDaoFactory(Provider<TeyvatHubDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ChecklistDao get() {
    return provideChecklistDao(databaseProvider.get());
  }

  public static DaoModule_ProvideChecklistDaoFactory create(
      Provider<TeyvatHubDatabase> databaseProvider) {
    return new DaoModule_ProvideChecklistDaoFactory(databaseProvider);
  }

  public static ChecklistDao provideChecklistDao(TeyvatHubDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaoModule.INSTANCE.provideChecklistDao(database));
  }
}
