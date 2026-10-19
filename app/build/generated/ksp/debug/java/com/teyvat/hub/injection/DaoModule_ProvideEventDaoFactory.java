package com.teyvat.hub.injection;

import com.teyvat.hub.data.TeyvatHubDatabase;
import com.teyvat.hub.data.local.event.GameEventDao;
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
public final class DaoModule_ProvideEventDaoFactory implements Factory<GameEventDao> {
  private final Provider<TeyvatHubDatabase> databaseProvider;

  private DaoModule_ProvideEventDaoFactory(Provider<TeyvatHubDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public GameEventDao get() {
    return provideEventDao(databaseProvider.get());
  }

  public static DaoModule_ProvideEventDaoFactory create(
      Provider<TeyvatHubDatabase> databaseProvider) {
    return new DaoModule_ProvideEventDaoFactory(databaseProvider);
  }

  public static GameEventDao provideEventDao(TeyvatHubDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaoModule.INSTANCE.provideEventDao(database));
  }
}
