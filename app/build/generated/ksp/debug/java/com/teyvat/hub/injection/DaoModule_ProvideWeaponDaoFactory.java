package com.teyvat.hub.injection;

import com.teyvat.hub.data.TeyvatHubDatabase;
import com.teyvat.hub.data.local.weapon.WeaponDao;
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
public final class DaoModule_ProvideWeaponDaoFactory implements Factory<WeaponDao> {
  private final Provider<TeyvatHubDatabase> databaseProvider;

  private DaoModule_ProvideWeaponDaoFactory(Provider<TeyvatHubDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public WeaponDao get() {
    return provideWeaponDao(databaseProvider.get());
  }

  public static DaoModule_ProvideWeaponDaoFactory create(
      Provider<TeyvatHubDatabase> databaseProvider) {
    return new DaoModule_ProvideWeaponDaoFactory(databaseProvider);
  }

  public static WeaponDao provideWeaponDao(TeyvatHubDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaoModule.INSTANCE.provideWeaponDao(database));
  }
}
