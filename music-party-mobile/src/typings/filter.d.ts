declare namespace filters {
  type formatCurrency = (value: number, currency?: CurrencyConfig.Currency) => number | string;
}

interface filtersConfig {
  formatCurrency: filters.formatCurrency
}

declare module '@vue/runtime-core' {
  export interface ComponentCustomProperties {
    $filters: filtersConfig
  }
}

export {}
