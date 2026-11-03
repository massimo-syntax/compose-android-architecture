package com.example.feature.details.impl;

import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class EmailDetailsViewModel_Factory_Impl implements EmailDetailsViewModel.Factory {
  private final EmailDetailsViewModel_Factory delegateFactory;

  EmailDetailsViewModel_Factory_Impl(EmailDetailsViewModel_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public EmailDetailsViewModel create(String emailId) {
    return delegateFactory.get(emailId);
  }

  public static Provider<EmailDetailsViewModel.Factory> create(
      EmailDetailsViewModel_Factory delegateFactory) {
    return InstanceFactory.create(new EmailDetailsViewModel_Factory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<EmailDetailsViewModel.Factory> createFactoryProvider(
      EmailDetailsViewModel_Factory delegateFactory) {
    return InstanceFactory.create(new EmailDetailsViewModel_Factory_Impl(delegateFactory));
  }
}
