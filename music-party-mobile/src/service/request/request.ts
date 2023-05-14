import {ref} from 'vue';
import type {Ref} from 'vue';
import type {AxiosInstance, AxiosRequestConfig} from 'axios';
import {useBoolean, useLoading} from '@/hooks';
import CustomAxiosInstance from './instance';

type RequestMethod = 'get' | 'post' | 'put' | 'delete';

interface RequestParam {
  url: string;
  method?: RequestMethod;
  data?: any;
  axiosConfig?: AxiosRequestConfig;
}

export function createRequest(axiosConfig: AxiosRequestConfig, backendConfig?: Service.BackendResultConfig) {
  const customInstance = new CustomAxiosInstance(axiosConfig, backendConfig);

  async function asyncRequest<T>(param: RequestParam): Promise<Service.RequestResult<T>> {
    const {url} = param;
    const method = param.method || 'get';
    const {instance} = customInstance;
    const res = (await getRequestResponse({
      instance,
      method,
      url,
      data: param.data,
      config: param.axiosConfig
    })) as Service.RequestResult<T>;

    return res;
  }

  function get<T>(url: string, config?: AxiosRequestConfig) {
    return asyncRequest<T>({url, method: 'get', axiosConfig: config});
  }

  function post<T>(url: string, data?: any, config?: AxiosRequestConfig) {
    return asyncRequest<T>({url, method: 'post', data, axiosConfig: config});
  }

  function put<T>(url: string, data?: any, config?: AxiosRequestConfig) {
    return asyncRequest<T>({url, method: 'put', data, axiosConfig: config});
  }

  function handleDelete<T>(url: string, config: AxiosRequestConfig) {
    return asyncRequest<T>({url, method: 'delete', axiosConfig: config});
  }

  return {
    get,
    post,
    put,
    delete: handleDelete
  };
}

interface RequestResultHook<T = any> {
  data: Ref<T | null>;
  error: Ref<Service.RequestError | null>;
  loading: Ref<boolean>;
  network: Ref<boolean>;
}

export function createHookRequest(axiosConfig: AxiosRequestConfig, backendConfig?: Service.BackendResultConfig) {
  const customInstance = new CustomAxiosInstance(axiosConfig, backendConfig);

  function useRequest<T>(param: RequestParam): RequestResultHook<T> {
    const {loading, startLoading, endLoading} = useLoading();
    const {bool: network, setBool: setNetwork} = useBoolean(window.navigator.onLine);

    startLoading();
    const data = ref<T | null>(null) as Ref<T | null>;
    const error = ref<Service.RequestError | null>(null);

    function handleRequestResult(response: any) {
      const res = response as Service.RequestResult<T>;
      data.value = res.data;
      error.value = res.error;
      endLoading();
      setNetwork(window.navigator.onLine);
    }

    const {url} = param;
    const method = param.method || 'get';
    const {instance} = customInstance;

    getRequestResponse({instance, method, url, data: param.data, config: param.axiosConfig}).then(
      handleRequestResult
    );

    return {
      data,
      error,
      loading,
      network
    };
  }

  function get<T>(url: string, config?: AxiosRequestConfig) {
    return useRequest<T>({url, method: 'get', axiosConfig: config});
  }

  function post<T>(url: string, data?: any, config?: AxiosRequestConfig) {
    return useRequest<T>({url, method: 'post', data, axiosConfig: config});
  }

  function put<T>(url: string, data?: any, config?: AxiosRequestConfig) {
    return useRequest<T>({url, method: 'put', data, axiosConfig: config});
  }

  function handleDelete<T>(url: string, config: AxiosRequestConfig) {
    return useRequest<T>({url, method: 'delete', axiosConfig: config});
  }

  return {
    get,
    post,
    put,
    delete: handleDelete
  };
}

async function getRequestResponse(params: {
  instance: AxiosInstance;
  method: RequestMethod;
  url: string;
  data?: any;
  config?: AxiosRequestConfig;
}) {
  const {instance, method, url, data, config} = params;

  let res: any;
  if (method === 'get' || method === 'delete') {
    res = await instance[method](url, config);
  } else {
    res = await instance[method](url, data, config);
  }
  return res;
}
