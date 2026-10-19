package com.teyvat.hub.data;

import com.teyvat.hub.data.local.character.CharacterDao;
import com.teyvat.hub.data.local.checklist.ChecklistDao;
import com.teyvat.hub.data.local.event.GameEventDao;
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
public final class DatabaseSeeder_Factory implements Factory<DatabaseSeeder> {
  private final Provider<CharacterDao> characterDaoProvider;

  private final Provider<ChecklistDao> checklistDaoProvider;

  private final Provider<GameEventDao> eventDaoProvider;

  private DatabaseSeeder_Factory(Provider<CharacterDao> characterDaoProvider,
      Provider<ChecklistDao> checklistDaoProvider, Provider<GameEventDao> eventDaoProvider) {
    this.characterDaoProvider = characterDaoProvider;
    this.checklistDaoProvider = checklistDaoProvider;
    this.eventDaoProvider = eventDaoProvider;
  }

  @Override
  public DatabaseSeeder get() {
    return newInstance(characterDaoProvider.get(), checklistDaoProvider.get(), eventDaoProvider.get());
  }

  public static DatabaseSeeder_Factory create(Provider<CharacterDao> characterDaoProvider,
      Provider<ChecklistDao> checklistDaoProvider, Provider<GameEventDao> eventDaoProvider) {
    return new DatabaseSeeder_Factory(characterDaoProvider, checklistDaoProvider, eventDaoProvider);
  }

  public static DatabaseSeeder newInstance(CharacterDao characterDao, ChecklistDao checklistDao,
      GameEventDao eventDao) {
    return new DatabaseSeeder(characterDao, checklistDao, eventDao);
  }
}
