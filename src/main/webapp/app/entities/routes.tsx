import React from 'react';
import PrivateRoute from 'app/shared/auth/private-route';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Template from './template';
import Predicate from './predicate';
import Mathtest from './mathtest';
import Question from './question';
import {AUTHORITIES} from "app/config/constants";
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="template/*" element={<Template />} />
        <Route path="predicate/*" element={<Predicate />} />
        <Route path="mathtest/*" element={<Mathtest />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
