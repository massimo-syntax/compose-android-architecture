package com.example.upcoming.presentation.screens;

import com.example.upcoming.domain.repository.UpcomingRepository;
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
public final class UpcomingViewModel_Factory implements Factory<UpcomingViewModel> {
  private final Provider<UpcomingRepository> repositoryProvider;

  private UpcomingViewModel_Factory(Provider<UpcomingRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpcomingViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpcomingViewModel_Factory create(Provider<UpcomingRepository> repositoryProvider) {
    return new UpcomingViewModel_Factory(repositoryProvider);
  }

  public static UpcomingViewModel newInstance(UpcomingRepository repository) {
    return new UpcomingViewModel(repository);
  }
}
