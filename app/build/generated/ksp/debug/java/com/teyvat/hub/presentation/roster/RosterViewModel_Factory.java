package com.teyvat.hub.presentation.roster;

import com.teyvat.hub.application.CharacterRepository;
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
public final class RosterViewModel_Factory implements Factory<RosterViewModel> {
  private final Provider<CharacterRepository> repositoryProvider;

  private RosterViewModel_Factory(Provider<CharacterRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RosterViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static RosterViewModel_Factory create(Provider<CharacterRepository> repositoryProvider) {
    return new RosterViewModel_Factory(repositoryProvider);
  }

  public static RosterViewModel newInstance(CharacterRepository repository) {
    return new RosterViewModel(repository);
  }
}
