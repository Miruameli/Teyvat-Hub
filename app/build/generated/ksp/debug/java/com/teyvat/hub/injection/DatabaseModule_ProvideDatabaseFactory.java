package com.teyvat.hub.injection;

import android.content.Context;
import com.teyvat.hub.data.TeyvatHubDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideDatabaseFactory implements Factory<TeyvatHubDatabase> {
  private final Provider<Context> contextProvider;

  private DatabaseModule_ProvideDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TeyvatHubDatabase get() {
    return provideDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideDatabaseFactory create(Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideDatabaseFactory(contextProvider);
  }

  public static TeyvatHubDatabase provideDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDatabase(context));
  }
}
