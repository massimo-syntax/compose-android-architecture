package com.example.upcoming.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
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
public final class UpcomingRepositoryImpl_Factory implements Factory<UpcomingRepositoryImpl> {
  private final Provider<SupabaseClient> supabaseProvider;

  private UpcomingRepositoryImpl_Factory(Provider<SupabaseClient> supabaseProvider) {
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public UpcomingRepositoryImpl get() {
    return newInstance(supabaseProvider.get());
  }

  public static UpcomingRepositoryImpl_Factory create(Provider<SupabaseClient> supabaseProvider) {
    return new UpcomingRepositoryImpl_Factory(supabaseProvider);
  }

  public static UpcomingRepositoryImpl newInstance(SupabaseClient supabase) {
    return new UpcomingRepositoryImpl(supabase);
  }
}
