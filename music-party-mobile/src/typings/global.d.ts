interface Window {
  $snackBar?: import('@/components/provider').SnackBarApiInjection,
  $loadingOverly?: import('@/components/provider').LoadingOverlyApiInjection,
  $dialog?: import("@/components/provider").DialogApiInjection
}

declare namespace Common {
  type StrategyAction = [boolean, () => void];
}

declare const PROJECT_BUILD_TIME: string;
