package com.example.feature.details.impl;

import com.example.domain.repository.EmailRepository;
import dagger.internal.DaggerGenerated;
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
public final class EmailDetailsViewModel_Factory {
  private final Provider<EmailRepository> repoProvider;

  private EmailDetailsViewModel_Factory(Provider<EmailRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  public EmailDetailsViewModel get(String emailId) {
    return newInstance(repoProvider.get(), emailId);
  }

  public static EmailDetailsViewModel_Factory create(Provider<EmailRepository> repoProvider) {
    return new EmailDetailsViewModel_Factory(repoProvider);
  }

  public static EmailDetailsViewModel newInstance(EmailRepository repo, String emailId) {
    return new EmailDetailsViewModel(repo, emailId);
  }
}
