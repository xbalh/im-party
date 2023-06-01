import {viteMockServe} from 'vite-plugin-mock';

export default (env: ImportMetaEnv) => {
  const prodMock = true
  const {VITE_SERVICE_ENV = 'dev'} = env;
  return viteMockServe({
    mockPath: 'mock',
    localEnabled: VITE_SERVICE_ENV === 'dev',
    prodEnabled: VITE_SERVICE_ENV === 'prod' && prodMock,
    injectCode: `
		import { setupMockServer } from '../mock';
		setupMockServer();
	`
  });
}
