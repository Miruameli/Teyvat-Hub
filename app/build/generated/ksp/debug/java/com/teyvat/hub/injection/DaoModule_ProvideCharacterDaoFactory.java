package com.teyvat.hub.injection;

import com.teyvat.hub.data.TeyvatHubDatabase;
import com.teyvat.hub.data.local.character.CharacterDao;
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
public final class DaoModule_ProvideCharacterDaoFactory implements Factory<CharacterDao> {
  private final Provider<TeyvatHubDatabase> databaseProvider;

  private DaoModule_ProvideCharacterDaoFactory(Provider<TeyvatHubDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public CharacterDao get() {
    return provideCharacterDao(databaseProvider.get());
  }

  public static DaoModule_ProvideCharacterDaoFactory create(
      Provider<TeyvatHubDatabase> databaseProvider) {
    return new DaoModule_ProvideCharacterDaoFactory(databaseProvider);
  }

  public static CharacterDao provideCharacterDao(TeyvatHubDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaoModule.INSTANCE.provideCharacterDao(database));
  }
}
