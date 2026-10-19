package com.teyvat.hub.injection;

import com.teyvat.hub.data.TeyvatHubDatabase;
import com.teyvat.hub.data.local.team.TeamDao;
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
public final class DaoModule_ProvideTeamDaoFactory implements Factory<TeamDao> {
  private final Provider<TeyvatHubDatabase> databaseProvider;

  private DaoModule_ProvideTeamDaoFactory(Provider<TeyvatHubDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TeamDao get() {
    return provideTeamDao(databaseProvider.get());
  }

  public static DaoModule_ProvideTeamDaoFactory create(
      Provider<TeyvatHubDatabase> databaseProvider) {
    return new DaoModule_ProvideTeamDaoFactory(databaseProvider);
  }

  public static TeamDao provideTeamDao(TeyvatHubDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaoModule.INSTANCE.provideTeamDao(database));
  }
}
