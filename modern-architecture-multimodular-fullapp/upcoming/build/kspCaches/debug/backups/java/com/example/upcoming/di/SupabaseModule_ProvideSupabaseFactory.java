package com.example.upcoming.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
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
public final class SupabaseModule_ProvideSupabaseFactory implements Factory<SupabaseClient> {
  @Override
  public SupabaseClient get() {
    return provideSupabase();
  }

  public static SupabaseModule_ProvideSupabaseFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SupabaseClient provideSupabase() {
    return Preconditions.checkNotNullFromProvides(SupabaseModule.INSTANCE.provideSupabase());
  }

  private static final class InstanceHolder {
    static final SupabaseModule_ProvideSupabaseFactory INSTANCE = new SupabaseModule_ProvideSupabaseFactory();
  }
}
