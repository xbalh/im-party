import auth from './auth';
import chat from './chat';
import route from './route';
import management from './management';

export default [...management, ...auth, ...route, ...chat];
