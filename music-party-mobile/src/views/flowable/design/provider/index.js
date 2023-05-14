import SelfProvider from './selfProvider';

export default {
  __init__: [ 'magicPropertiesProvider' ],
  magicPropertiesProvider: [ 'type', SelfProvider ]
};
