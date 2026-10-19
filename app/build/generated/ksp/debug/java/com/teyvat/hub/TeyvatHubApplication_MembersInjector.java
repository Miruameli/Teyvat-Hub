package com.teyvat.hub;

import com.teyvat.hub.data.DatabaseSeeder;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

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
public final class TeyvatHubApplication_MembersInjector implements MembersInjector<TeyvatHubApplication> {
  private final Provider<DatabaseSeeder> seederProvider;

  private TeyvatHubApplication_MembersInjector(Provider<DatabaseSeeder> seederProvider) {
    this.seederProvider = seederProvider;
  }

  @Override
  public void injectMembers(TeyvatHubApplication instance) {
    injectSeeder(instance, seederProvider.get());
  }

  public static MembersInjector<TeyvatHubApplication> create(
      Provider<DatabaseSeeder> seederProvider) {
    return new TeyvatHubApplication_MembersInjector(seederProvider);
  }

  @InjectedFieldSignature("com.teyvat.hub.TeyvatHubApplication.seeder")
  public static void injectSeeder(TeyvatHubApplication instance, DatabaseSeeder seeder) {
    instance.seeder = seeder;
  }
}
