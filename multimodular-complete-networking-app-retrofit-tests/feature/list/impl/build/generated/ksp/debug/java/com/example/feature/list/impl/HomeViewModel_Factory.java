package com.example.feature.list.impl;

import com.example.domain.repository.EmailRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<EmailRepository> repoProvider;

  private HomeViewModel_Factory(Provider<EmailRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<EmailRepository> repoProvider) {
    return new HomeViewModel_Factory(repoProvider);
  }

  public static HomeViewModel newInstance(EmailRepository repo) {
    return new HomeViewModel(repo);
  }
}
