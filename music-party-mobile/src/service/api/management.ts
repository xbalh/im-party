import {adapter} from '@/utils';
import {mockRequest} from '../request';
import {adapterOfFetchUserList, adapterOfFetchUser, deriveFetchListAdapter} from './management.adapter';

export const fetchUserList = async () => {
  const data = await mockRequest.post<ApiCommon.PageResult<ApiUserManagement.User[]> | null>('/getAllUserList');
  return adapter(adapterOfFetchUserList, data);
};
export const fetchUser = async (id: string) => {
  const data = await mockRequest.post<ApiUserManagement.User | null>(`/getUser/${id}`);
  return adapter(adapterOfFetchUser, data);
};

export const fetchFormList = async () => {
  const data = await mockRequest.post<ApiCommon.PageResult<ApiForm.Form[]> | null>(`/getAllFormList`);
  return adapter(deriveFetchListAdapter<ApiForm.Form, FormManagement.Form>(apiFrom => {
    const form: FormManagement.Form = {
      ...apiFrom
    }
    return form;
  }), data);
};
