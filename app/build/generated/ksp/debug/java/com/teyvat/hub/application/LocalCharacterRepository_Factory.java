package com.teyvat.hub.application;

import com.teyvat.hub.data.local.character.CharacterDao;
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
public final class LocalCharacterRepository_Factory implements Factory<LocalCharacterRepository> {
  private final Provider<CharacterDao> daoProvider;

  private LocalCharacterRepository_Factory(Provider<CharacterDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public LocalCharacterRepository get() {
    return newInstance(daoProvider.get());
  }

  public static LocalCharacterRepository_Factory create(Provider<CharacterDao> daoProvider) {
    return new LocalCharacterRepository_Factory(daoProvider);
  }

  public static LocalCharacterRepository newInstance(CharacterDao dao) {
    return new LocalCharacterRepository(dao);
  }
}
