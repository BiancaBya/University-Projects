import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';
import CarList from './pages/car/CarList';

import '@ionic/react/css/core.css';

import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';


/* import '@ionic/react/css/palettes/dark.always.css'; */
/* import '@ionic/react/css/palettes/dark.class.css'; */
import '@ionic/react/css/palettes/dark.system.css';

import './theme/variables.css';
import {CarProvider} from "./pages/car/CarProvider";
import CarEdit from "./pages/car/CarEdit";
import {AuthProvider, Login, PrivateRoute} from "./auth";
import CarListSearch from "./pages/car/CarListSearch";

setupIonicReact();

const App: React.FC = () => (
  <IonApp>
    <IonReactRouter>
        <IonRouterOutlet>
          <AuthProvider>
            <Route path='/login' component={Login} exact={true} />
            <CarProvider>
              <PrivateRoute path="/cars" component={CarList} exact={true}/>
                <PrivateRoute path="/search" component={CarListSearch} exact={true}/>
              <PrivateRoute path="/car" component={CarEdit} exact={true}/>
              <PrivateRoute path="/car/:id" component={CarEdit} exact={true}/>
            </CarProvider>
            <Route exact path="/" render={() => <Redirect to="/cars"/>}/>
          </AuthProvider>
        </IonRouterOutlet>
    </IonReactRouter>
  </IonApp>
);

export default App;


